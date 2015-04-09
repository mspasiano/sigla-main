package it.cnr.contab.util.servlet;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.Parametri_enteHome;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spoolerHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.excel.bulk.Excel_spoolerBulk;
import it.cnr.jada.excel.bulk.Excel_spoolerHome;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 	1.0
 * @author Marco Spasiano
 */
public class UtilServlet extends HttpServlet {
	private static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";
	private transient final static Logger logger = LoggerFactory.getLogger(UtilServlet.class);
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
		throws ServletException, IOException {
		httpservletresponse.setHeader("Cache-Control", "private");
		httpservletresponse.setHeader("Pragma", "no-cache");
		httpservletresponse.setDateHeader("Expires", 0L);
		String s = httpservletrequest.getParameter("campo");
		String aggiornaGECO = httpservletrequest.getParameter("aggiornaGECO");
		String esercizio = httpservletrequest.getParameter("esercizio");
		String cds = httpservletrequest.getParameter("cds");
		String getDirTemp = httpservletrequest.getParameter("tmp.dir.SIGLAWeb");
		if(s == null && aggiornaGECO == null && getDirTemp == null){
			httpservletresponse.getWriter().println("Richiesta non autorizzata: specificare " + (new URL("http", httpservletrequest.getServerName(), httpservletrequest.getServerPort(), httpservletrequest.getRequestURI())).toExternalForm() + "?campo=xyz");
			httpservletresponse.setStatus(401);
		}else if(s == null && aggiornaGECO != null && aggiornaGECO.equalsIgnoreCase("Y")){
			aggiornaGECO(esercizio,cds);
		}else if(s == null && aggiornaGECO == null && getDirTemp != null && getDirTemp.equalsIgnoreCase("Y")){
			httpservletresponse.setContentType("text/plain");
			httpservletresponse.getWriter().println(getServletContext().getRealPath("/"));	
		}else{
		  try{	
			Connection conn = null;
			LoggableStatement statement = null;
			ResultSet rs = null;
			try{
				conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
				conn.setAutoCommit(false);
				it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
				Parametri_enteHome parametri_enteHome = (Parametri_enteHome)homeCache.getHome(Parametri_enteBulk.class);
				statement = parametri_enteHome.selectRigaAttiva().prepareStatement(conn);
				rs = statement.executeQuery();
				if (rs.next()){					
					String output = new String (""); 
					String valore = rs.getString(s);
					if (!(valore == null || "".equals(valore))){
					  for(int i=0; i < valore.length(); i++){
						char k = valore.charAt(i);
						if (k == ' ')
						  output = output + "&nbsp;";
						else if (k == 10)
						  output = output + "<br>";  
						else
						  output = output + k;  						
					  }					
					}
					if (s.equals("BOX_COMUNICAZIONI"))
						output = "<body bgcolor=\"#C4CAD4\"><em><font color=\"#6666aa\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">" + output + "</font></em></body>";
					else
						output = "<body bgcolor=\"#C4CAD4\"><em><font color=\"#6666aa\" size=\"2\" face=\"Arial, Helvetica, sans-serif\">" + output + "</font></em></body>";
					httpservletresponse.setContentType("text/html");
					httpservletresponse.getWriter().println(output);	
				}					
			}
			finally {
				rs.close();	
				statement.close();
				conn.commit();
				conn.close();
			}
		  }			
		  catch(Throwable _ex){
				httpservletresponse.getWriter().println("Errore interno del server");
				httpservletresponse.setStatus(500);
				return;
		  }
		}
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
			doGet(req, resp);
	}
	public void init()
		throws ServletException
	{
		final GregorianCalendar dataInizio = (GregorianCalendar) GregorianCalendar.getInstance();
		class PrintThread
			implements Runnable
		{

			public void run()
			{
				while (true){
				  try{
					Thread.sleep(1000*60*20);
					if (new java.text.SimpleDateFormat("HH").format(new java.util.Date()).equalsIgnoreCase("02")){
						deletePrintSpooler();
						deleteExcel();
						aggiornaGECO(String.valueOf(dataInizio.get(GregorianCalendar.YEAR)), null);
						deleteMessaggi();
					}
				  }
				  catch(Throwable e){
					System.out.println("UtilServlet.init()" + e.getMessage());
				  }
				}				
			}
			PrintThread()
			{
			}
		}
		new Thread(new PrintThread()).start();		
		
		
		String version  = "01.001.000";
		InputStream is = getServletContext().getResourceAsStream(MANIFEST_PATH);
		if (is != null) {
			try {
				Manifest manifest = new Manifest(is);
				Attributes attributes = manifest.getMainAttributes();

				version = attributes.getValue("Implementation-Version");
			} catch (IOException e) {
				logger.warn("IOException", e);
			}
		}
		String APPLICATION_TITLE = "SIGLA - Sistema Informativo per la Gestione delle Linee di Attivit�";
		String APPLICATION_VERSION = "Documenti contabili/amministrativi transact. " + version;
		String APPLICATION_TITLE_VERSION = APPLICATION_TITLE + " - " + APPLICATION_VERSION;

		getServletContext().setAttribute("VERSION", version);
		getServletContext().setAttribute("APPLICATION_TITLE", APPLICATION_TITLE);
		getServletContext().setAttribute("APPLICATION_VERSION", APPLICATION_VERSION);
		getServletContext().setAttribute("APPLICATION_TITLE_VERSION", APPLICATION_TITLE_VERSION);
	}
	
	private void aggiornaGECO(String esercizio, String cds) {
		UserContext userContext = new CNRUserContext("GECO",null,esercizio!=null?Integer.valueOf(esercizio):null,null,cds,null);
		try {
			((ProgettoRicercaPadreComponentSession) EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession")).aggiornaGECO(userContext);
		} catch (Exception e) {
			String text = "Errore interno del Server Utente:"+CNRUserContext.getUser(userContext);
			SendMail.sendErrorMail(text,e.toString());
		}
	}
	
	public void deletePrintSpooler()
	{
		try{	
		  LoggableStatement statement = null;
		  ResultSet rs = null;
		  Connection conn = null;
		  try{
			  conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
			  conn.setAutoCommit(false); 
			  it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
			  Print_spoolerHome print_spoolerHome = (Print_spoolerHome)homeCache.getHome(Print_spoolerBulk.class);
			  statement = print_spoolerHome.selectJobsToDelete().prepareStatement(conn);
			  rs = statement.executeQuery();
			  while (rs.next()){
			  	try{
				  StringBuffer reportServerURL = new StringBuffer(rs.getString("SERVER"));
				  HttpClient httpclient = new HttpClient();
				  reportServerURL.append("/").append(rs.getString("UTCR"));
				  reportServerURL.append("/").append(rs.getString("NOME_FILE"));
				  HttpMethod method = new DeleteMethod(reportServerURL.toString());
				  method.setRequestHeader("Accept-Language", Locale.getDefault().toString());
				  httpclient.executeMethod(method);
				  //Cancelliamo la riga sul DB
				  Print_spoolerBulk bulk = (Print_spoolerBulk)print_spoolerHome.findByPrimaryKey(new Print_spoolerBulk(new Long(rs.getLong("PG_STAMPA"))));
				  print_spoolerHome.deleteRiga(bulk, null);
				} catch(java.net.UnknownHostException e) {
				} catch(java.net.ConnectException e){
				}catch(IOException e) {
					//In questo caso Il file � gi� stato cancellato e quindi cancello la riga
					Print_spoolerBulk bulk = (Print_spoolerBulk)print_spoolerHome.findByPrimaryKey(new Print_spoolerBulk(new Long(rs.getLong("PG_STAMPA"))));
					print_spoolerHome.deleteRiga(bulk, null);					
				}				  				  
				  	
			  }					
		  }
		  finally {
		  	  if (rs != null)
			    rs.close();	
			  if (statement != null)  
			    statement.close();
			  if (conn != null){
				conn.commit();
				conn.close();
			  }
		  }
		}
		catch(EJBException ex){
		}
		catch(Throwable _ex){
			System.out.println("UtilServlet.init()" +  _ex.getMessage());
		}
		
	}
	public void deleteExcel()
	{
		try{	
		  LoggableStatement statement = null;
		  ResultSet rs = null;
		  Connection conn = null;
		  try{
			  conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
			  conn.setAutoCommit(false); 
			  it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
			  Excel_spoolerHome excel_spoolerHome = (Excel_spoolerHome)homeCache.getHome(Excel_spoolerBulk.class);
			  statement = excel_spoolerHome.selectJobsToDelete().prepareStatement(conn);
			  rs = statement.executeQuery();
			  while (rs.next()){
				try{
				  StringBuffer reportServerURL = new StringBuffer(rs.getString("SERVER"));
				  reportServerURL.append("?user=");
				  reportServerURL.append(java.net.URLEncoder.encode(rs.getString("UTCR")));
				  reportServerURL.append("&file=");
				  reportServerURL.append(java.net.URLEncoder.encode(rs.getString("NOME_FILE")));
				  reportServerURL.append("&command=delete");
				  java.net.URLConnection urlConn = new java.net.URL(reportServerURL.toString()).openConnection();
				  urlConn.setUseCaches(false);
				  urlConn.connect();
				  java.io.InputStream is = urlConn.getInputStream();
				  is.close();
				  //Cancelliamo la riga sul DB
				  Excel_spoolerBulk bulk = (Excel_spoolerBulk)excel_spoolerHome.findByPrimaryKey(new Excel_spoolerBulk(new Long(rs.getLong("PG_ESTRAZIONE"))));
				  excel_spoolerHome.deleteRiga(bulk);
				} catch(java.net.UnknownHostException e) {
				} catch(java.net.ConnectException e){
				}catch(IOException e) {
					//In questo caso Il file � gi� stato cancellato e quindi cancello la riga
					Excel_spoolerBulk bulk = (Excel_spoolerBulk)excel_spoolerHome.findByPrimaryKey(new Excel_spoolerBulk(new Long(rs.getLong("PG_ESTRAZIONE"))));
					excel_spoolerHome.deleteRiga(bulk);					
				}				  				  				  	
			  }					
		  }
		  finally {
			  if (rs != null)
				rs.close();	
			  if (statement != null)  
				statement.close();
			  if (conn != null){
				conn.commit();
				conn.close();
			  }
		  }
		}
		catch(EJBException ex){
		}
		catch(Throwable _ex){
			System.out.println("UtilServlet.init()" +  _ex.getMessage());
		}
	}	
	public void deleteMessaggi()
	{
		try{	
		  LoggableStatement statement = null;
		  ResultSet rs = null;
		  Connection conn = null;
		  try{
			  conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
			  conn.setAutoCommit(false); 
			  it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
			  it.cnr.contab.messaggio00.bulk.MessaggioHome messaggioHome = (it.cnr.contab.messaggio00.bulk.MessaggioHome)homeCache.getHome(it.cnr.contab.messaggio00.bulk.MessaggioBulk.class);
			  statement = messaggioHome.selectJobsToDelete().prepareStatement(conn);
			  rs = statement.executeQuery();
			  while (rs.next()){
				  //Cancelliamo la riga sul DB
				  it.cnr.contab.messaggio00.bulk.MessaggioBulk bulk =(it.cnr.contab.messaggio00.bulk.MessaggioBulk)messaggioHome.findByPrimaryKey(new it.cnr.contab.messaggio00.bulk.MessaggioBulk(new Long(rs.getLong("PG_MESSAGGIO"))));
				  messaggioHome.deleteRiga(bulk);
			  }
		  }
		  finally {
		  	  if (rs != null)
			    rs.close();	
			  if (statement != null)  
			    statement.close();
			  if (conn != null){
				conn.commit();
				conn.close();
			  }
		  }
		}
		catch(EJBException ex){
		}
		catch(Throwable _ex){
			System.out.println("UtilServlet.init()" +  _ex.getMessage());
		}
		
	}
}
