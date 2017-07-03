package it.cnr.contab.docamm00.bp;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.actions.CRUDFatturaPassivaAction;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.config.StoragePropertyNames;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.RegimeFiscaleType;
import org.apache.commons.io.IOUtils;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

public class CRUDFatturaPassivaElettronicaBP extends AllegatiCRUDBP<AllegatoFatturaBulk, DocumentoEleTestataBulk> implements FatturaPassivaElettronicaBP{
	private static final long serialVersionUID = 1L;
	private TipoIntegrazioneSDI tipoIntegrazioneSDI = TipoIntegrazioneSDI.PEC;
	private final SimpleDetailCRUDController crudDocEleLineaColl = 
			new SimpleDetailCRUDController("RifDocEleLineaColl",DocumentoEleLineaBulk.class,"docEleLineaColl",this);
	private final SimpleDetailCRUDController crudDocEleIVAColl = 
			new SimpleDetailCRUDController("RifDocEleIVAColl",DocumentoEleIvaBulk.class,"docEleIVAColl",this);
	private final SimpleDetailCRUDController crudDocEleAllegatiColl = 
			new SimpleDetailCRUDController("RifDocEleAllegatiColl",DocumentoEleAllegatiBulk.class,"docEleAllegatiColl",this);
	private final SimpleDetailCRUDController crudDocEleTributiColl = 
			new SimpleDetailCRUDController("RifDocEleTributiColl",DocumentoEleTributiBulk.class,"docEleTributiColl",this);
	private final SimpleDetailCRUDController crudDocEleScontoMaggColl = 
			new SimpleDetailCRUDController("RifDocEleScontoMaggColl",DocumentoEleScontoMaggBulk.class,"docEleScontoMaggColl",this);
	private final SimpleDetailCRUDController crudDocEleAcquistoColl = 
			new SimpleDetailCRUDController("RifDocEleAcquistoColl",DocumentoEleAcquistoBulk.class,"docEleAcquistoColl",this);
	private final SimpleDetailCRUDController crudDocEleDdtColl = 
			new SimpleDetailCRUDController("RifDocEleDdtColl",DocumentoEleDdtBulk.class,"docEleDdtColl",this);
	private Unita_organizzativaBulk uoScrivania;
	private boolean esercizioAperto;

	public CRUDFatturaPassivaElettronicaBP() {
		super();
	}

	public CRUDFatturaPassivaElettronicaBP(String s) {
		super(s);
	}

	public boolean isPrintButtonEnabled() {
		return getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null;
	}
	
	public boolean isCompilaButtonEnabled() {
		return isEditable() && getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isCompilabile() &&
				!((DocumentoEleTestataBulk)getModel()).isIrregistrabile() &&
				((DocumentoEleTestataBulk)getModel()).getDocumentoEleTrasmissione().getUnitaOrganizzativa().equalsByPrimaryKey(uoScrivania) &&
				this.isEsercizioAperto();
		
	}

	public boolean isVisualizzaFatturaButtonEnabled() {
		return getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isRegistrata();
	}
	
	public boolean isRifiutaButtonEnabled() {
		return isEditable() && getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				((DocumentoEleTestataBulk)getModel()).isRifiutabile();
	}

	public boolean isEsitoRifiutatoButtonHidden() {
		return !(getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC) &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isRifiutata());
	}

	public boolean isEsitoAccettatoButtonHidden() {
		return !(getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC) &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				((DocumentoEleTestataBulk)getModel()).isRegistrata());
	}

	public boolean isReinviaEsitoButtonHidden() {
		return !(getModel() != null && ((DocumentoEleTestataBulk)getModel()).getIdentificativoSdi() != null &&
				tipoIntegrazioneSDI.equals(TipoIntegrazioneSDI.PEC) &&
				((DocumentoEleTestataBulk)getModel()).getCrudStatus() == OggettoBulk.NORMAL &&
				!((DocumentoEleTestataBulk)getModel()).isRicevutaDecorrenzaTermini() &&
				((DocumentoEleTestataBulk)getModel()).getStatoNotificaEsito() != null &&
				((DocumentoEleTestataBulk)getModel()).getStatoNotificaEsito().equalsIgnoreCase(DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_SCARTATO_SDI));		
	}
	@Override
	protected Button[] createToolbar() {
		Button[] buttons = super.createToolbar();
		List<Button> toolbar = new ArrayList<Button>();
		toolbar.addAll(Arrays.asList(buttons));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.download"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.downloadFatturaFirmata"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.rifiuta"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.accetta"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.visuallizza.fattura"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.esito.rifiutato"));
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.esito.accettato"));
		toolbar.get(toolbar.size() - 1).setSeparator(true);
		toolbar.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.esito.reinvia"));
		return toolbar.toArray(new Button[toolbar.size()]);
	}
	
	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		super.initialize(actioncontext);
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.AGGIORNATO.name());
    	documentoEleTestata.setFlIrregistrabile("N");
    	setStatus(SEARCH);
		setUoScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext));
		try {
			String integrazioneSDI = Utility.createConfigurazioneCnrComponentSession().
				getVal01(actioncontext.getUserContext(), null, null, 
						Configurazione_cnrBulk.PK_INTEGRAZIONE_SDI, Configurazione_cnrBulk.SK_INTEGRAZIONE_SDI);
			if (integrazioneSDI != null)
				tipoIntegrazioneSDI = TipoIntegrazioneSDI.valueOf(integrazioneSDI); 
			setEsercizioAperto(((it.cnr.contab.config00.ejb.EsercizioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",	EsercizioComponentSession.class)).isEsercizioAperto(actioncontext.getUserContext()));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (EJBException e) {
			throw handleException(e);
		}		
	}

	private void setUoScrivania(Unita_organizzativaBulk uoScrivania) {
		this.uoScrivania = uoScrivania;
	}

	public boolean isUoEnte(){
		return (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
    }

    public void writeFormFieldTrasmissione(JspWriter jspwriter, String s) throws IOException{
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.getDocumentoEleTrasmissione().getBulkInfo().writeFormField(
    			this, jspwriter, documentoEleTestata.getDocumentoEleTrasmissione(), 
    			null, s, getInputPrefix(), 1, 1, getStatus(), 
    			isInputReadonly(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
	}

    public void writeFormLabelTrasmissione(JspWriter jspwriter, String s)
    		throws IOException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.getDocumentoEleTrasmissione().getBulkInfo().writeFormLabel(
    			this, jspwriter, documentoEleTestata.getDocumentoEleTrasmissione(), null, s, null, this.getParentRoot().isBootstrap());
    }
	
    public void writeFormInputTrasmissione(JspWriter jspwriter, String s)
    		throws IOException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
    	documentoEleTestata.getDocumentoEleTrasmissione().getBulkInfo().writeFormInput(
    			jspwriter, documentoEleTestata.getDocumentoEleTrasmissione(), null, s, isInputReadonly(), null, null, getInputPrefix(), getStatus(), getFieldValidationMap(), this.getParentRoot().isBootstrap());
    }

	public SimpleDetailCRUDController getCrudDocEleLineaColl() {
		return crudDocEleLineaColl;
	}

	public SimpleDetailCRUDController getCrudDocEleIVAColl() {
		return crudDocEleIVAColl;
	}

	public SimpleDetailCRUDController getCrudDocEleAllegatiColl() {
		return crudDocEleAllegatiColl;
	}

	public SimpleDetailCRUDController getCrudDocEleTributiColl() {
		return crudDocEleTributiColl;
	}

	public SimpleDetailCRUDController getCrudDocEleScontoMaggColl() {
		return crudDocEleScontoMaggColl;
	}

	public SimpleDetailCRUDController getCrudDocEleAcquistoColl() {
		return crudDocEleAcquistoColl;
	}

	public SimpleDetailCRUDController getCrudDocEleDdtColl() {
		return crudDocEleDdtColl;
	}

	@Override
	protected void resetTabs(ActionContext actioncontext) {
		setTab( "tab", "tabEleTrasmissione");
	}
	
	@Override
	public boolean isDeleteButtonHidden() {
		return true;
	}
	
	@Override
	public boolean isNewButtonHidden() {
		return true;
	}
	
	public void scaricaEsito(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		StorageObject fatturaFolder = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef());
		List<StorageObject> files = SpringUtil.getBean("storeService", StoreService.class).getChildren(fatturaFolder.getKey());
		for (StorageObject storageObject : files) {
			if (storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains(
					documentoEleTestata.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.RIFIUTATO)?
        					StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO.value():
        						StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO.value())){
				HttpServletResponse response = ((HttpActionContext)actioncontext).getResponse();
				OutputStream os = response.getOutputStream();
				response.setContentType("application/octetstream");
				response.setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
				IOUtils.copy(SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject.getKey()), os);
				os.flush();
			}
		}
	}	
	
	public void scaricaFatturaHtml(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		StorageObject fatturaFolder = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef());
		List<StorageObject> files = SpringUtil.getBean("storeService", StoreService.class).getChildren(fatturaFolder.getKey());
		for (StorageObject storageObject : files) {
			if (storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains("P:sigla_fatture_attachment:trasmissione_fattura")){
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Source xslDoc = null;
				if (documentoEleTestata.getDocumentoEleTrasmissione().getFormatoTrasmissione().equals("FPA12")){
					xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.2.xsl"));
				} else if (documentoEleTestata.getDocumentoEleTrasmissione().getFormatoTrasmissione().equals("SDI11")){
					xslDoc = new StreamSource(this.getClass().getResourceAsStream("/it/cnr/contab/docamm00/bp/fatturapa_v1.1.xsl"));
				} else {
					throw new ApplicationException("Il formato trasmissione indicato da SDI non rientra tra i formati attesi");
				}
				Source xmlDoc = new StreamSource(SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject.getKey()));
				HttpServletResponse response = ((HttpActionContext)actioncontext).getResponse();
				OutputStream os = response.getOutputStream();
				response.setContentType("text/html");
				Transformer trasform = tFactory.newTransformer(xslDoc);
				trasform.transform(xmlDoc, new StreamResult(os));
				os.flush();
			}
		}
	}	

	public void scaricaFatturaFirmata(ActionContext actioncontext) throws IOException, ServletException, TransformerException, ApplicationException {
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		StorageObject fatturaFolder = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(documentoEleTestata.getDocumentoEleTrasmissione().getCmisNodeRef());
		List<StorageObject> files = SpringUtil.getBean("storeService", StoreService.class).getChildren(fatturaFolder.getKey());
		for (StorageObject storageObject : files) {
			if (storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()).contains("P:sigla_fatture_attachment:fattura_elettronica_xml_post_firma")){
				InputStream is = SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject);
				((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
				((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
				OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
				((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
				byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
				int buflength;
				while ((buflength = is.read(buffer)) > 0) {
					os.write(buffer,0,buflength);
				}
				is.close();
				os.flush();
			}
		}
	}	
	
	public String getNomeFileAllegato() {
		DocumentoEleAllegatiBulk allegato = (DocumentoEleAllegatiBulk)getCrudDocEleAllegatiColl().getModel();
		if (allegato != null && allegato.getCmisNodeRef() != null)
			return allegato.getNomeAttachment();
		return null;
	}
	
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		DocumentoEleAllegatiBulk allegato = (DocumentoEleAllegatiBulk)getCrudDocEleAllegatiColl().getModel();
		StorageObject storageObject = SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(allegato.getCmisNodeRef());
		InputStream is = SpringUtil.getBean("storeService", StoreService.class).getResource(storageObject.getKey());
		((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
		OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
		((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
		byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
		int buflength;
		while ((buflength = is.read(buffer)) > 0) {
			os.write(buffer,0,buflength);
		}
		is.close();
		os.flush();
	}

	public void reinviaEsito(ActionContext actioncontext, DocumentoEleTestataBulk documentoEleTestata) throws BusinessProcessException, ValidationException {
    	try {
        	((FatturaElettronicaPassivaComponentSession)createComponentSession()).
        		notificaEsito(actioncontext.getUserContext(), tipoIntegrazioneSDI, documentoEleTestata);
        	documentoEleTestata.setStatoNotificaEsito(null);
        	documentoEleTestata.setToBeUpdated();
        	update(actioncontext);    	
    	} catch (Exception e) {
    		throw handleException(e);
		}		
	}	
	
	public void rifiutaFattura(ActionContext actioncontext, DocumentoEleTestataBulk documentoEleTestata) throws BusinessProcessException, ValidationException {
		StatoDocumentoEleEnum statoDocumentoEleEnum = documentoEleTestata.getStatoDocumentoEle();
    	try {
        	documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.RIFIUTATO.name());
        	documentoEleTestata.setToBeUpdated();
        	((FatturaElettronicaPassivaComponentSession)createComponentSession()).
    		notificaEsito(actioncontext.getUserContext(), tipoIntegrazioneSDI, documentoEleTestata);
    	} catch (Exception e) {
    		documentoEleTestata.setStatoDocumento(statoDocumentoEleEnum.name());
    		throw handleException(e);
		}
    	setModel(actioncontext, documentoEleTestata);    	
    	update(actioncontext);    	
	}

	@Override
	protected void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag) throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);
    	DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) oggettobulk;
    	if (!documentoEleTestata.isEditabile())
    		setStatus(VIEW);		
	}

	@Override
	public void setTab(String tabName, String pageName) {
		super.setTab(tabName, pageName);
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		if (documentoEleTestata != null) {
			if (pageName.equalsIgnoreCase("tabAllegati")){
				setStatus(EDIT);
			} else {
				if (getStatus() == EDIT && !documentoEleTestata.isEditabile())
					setStatus(VIEW);				
			}			
		}
	}
	
	public OggettoBulk completaFatturaPassiva(ActionContext context, Fattura_passivaBulk fatturaPassivaBulk, CRUDFatturaPassivaBP nbp, Fattura_passivaBulk fatturaPassivaDiRiferimento) throws BusinessProcessException {
    	try {    		
			CRUDFatturaPassivaAction action = new CRUDFatturaPassivaAction();
			DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
			fatturaPassivaBulk.setDocumentoEleTestata(documentoEleTestata);
			fatturaPassivaBulk = 
					((FatturaPassivaComponentSession)nbp.createComponentSession()).
					caricaAllegatiBulk(context.getUserContext(), fatturaPassivaBulk);
			fatturaPassivaBulk.setTi_fattura(documentoEleTestata.getTipoDocumentoSIGLA());
	    	fatturaPassivaBulk.setNr_fattura_fornitore(documentoEleTestata.getNumeroDocumento());
	    	fatturaPassivaBulk.setDt_fattura_fornitore(documentoEleTestata.getDataDocumento());
	    	fatturaPassivaBulk.setEsercizio_fattura_fornitore(CNRUserContext.getEsercizio(context.getUserContext()));//TODO
	    	fatturaPassivaBulk.setData_protocollo(documentoEleTestata.getDocumentoEleTrasmissione().getDataRicezione());
	    	Calendar date = Calendar.getInstance();
	    	date.setTimeInMillis(documentoEleTestata.getDataDocumento().getTime());
	    	date.add(Calendar.MONTH, 1);
	    	
	    	fatturaPassivaBulk.setDs_fattura_passiva(documentoEleTestata.getCausale());
	    	fatturaPassivaBulk.setFl_intra_ue(Boolean.FALSE);
	    	fatturaPassivaBulk.setFl_extra_ue(Boolean.FALSE);
	    	fatturaPassivaBulk.setFl_san_marino_senza_iva(Boolean.FALSE);
	    	fatturaPassivaBulk.setFl_fattura_compenso(existsTributi(documentoEleTestata));
	    	    	
	    	//TODO eliminata su richiesta di Patrizia fatturaPassivaBulk.setDt_scadenza(new java.sql.Timestamp(date.getTime().getTime())); 
	    	GregorianCalendar gcDataMinima = new GregorianCalendar(), gcDataMassima = new GregorianCalendar();
	    	gcDataMinima.setTime(calcolaDataMinimaCompetenza(documentoEleTestata));
	    	gcDataMassima.setTime(calcolaDataMassimaCompetenza(documentoEleTestata));
	    	
    		if (!fatturaPassivaBulk.getFl_fattura_compenso() && gcDataMinima.get(Calendar.YEAR)<gcDataMassima.get(Calendar.YEAR))
    			gcDataMinima.setTime(DateUtils.firstDateOfTheYear(gcDataMassima.get(Calendar.YEAR)));

        	fatturaPassivaBulk.setDt_da_competenza_coge(new Timestamp(gcDataMinima.getTime().getTime()));
	    	fatturaPassivaBulk.setDt_a_competenza_coge(new Timestamp(gcDataMassima.getTime().getTime()));
	    	if(fatturaPassivaBulk.getTi_fattura().equalsIgnoreCase(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO)){
	    		fatturaPassivaBulk.setIm_totale_fattura(documentoEleTestata.getImportoDocumento().abs());
	    		fatturaPassivaBulk.setIm_importo_totale_fattura_fornitore_euro(documentoEleTestata.getImportoDocumento().abs());
	    	}else{
	    		fatturaPassivaBulk.setIm_totale_fattura(documentoEleTestata.getImportoDocumento());
	    		fatturaPassivaBulk.setIm_importo_totale_fattura_fornitore_euro(documentoEleTestata.getImportoDocumento());
	    	}
	    	action.doCalcolaTotaleFatturaFornitoreInEur(context);
	    	
	    	action.doBringBackSearchFornitore(context, fatturaPassivaBulk, documentoEleTestata.getDocumentoEleTrasmissione().getPrestatore());

	    	fatturaPassivaBulk = (Fattura_passivaBulk) nbp.getModel();
	    	if (documentoEleTestata.getModalitaPagamento() != null) {
	    		fatturaPassivaBulk.setModalita_pagamento(documentoEleTestata.getModalitaPagamento().getRif_modalita_pagamento());
	    		action.doOnModalitaPagamentoChange(context);	    		
	    	}
			fatturaPassivaBulk = (Fattura_passivaBulk) nbp.getModel();
			if (fatturaPassivaDiRiferimento == null) {
				FatturaPassivaRigaCRUDController dettaglioController = nbp.getDettaglio();			
				for (DocumentoEleLineaBulk documentoEleLinea : documentoEleTestata.getDocEleLineaColl()) {
					Fattura_passiva_rigaBulk rigaFattura = documentoEleTestata.getInstanceRiga();			
					int i = dettaglioController.addDetail(rigaFattura);
					dettaglioController.setDirty(true);
					dettaglioController.setModelIndex(context, i);
					rigaFattura.setBene_servizio(documentoEleLinea.getBeneServizio());
					if(documentoEleLinea.getLineaDescrizione().length()>199)
						rigaFattura.setDs_riga_fattura(documentoEleLinea.getLineaDescrizione().substring(0, 199));
					else
						rigaFattura.setDs_riga_fattura(documentoEleLinea.getLineaDescrizione());
					rigaFattura.setVoce_iva(recuperaCodiceIVA(documentoEleTestata, documentoEleLinea));
					rigaFattura.setQuantita(documentoEleLinea.getLineaQuantita());
					action.doOnQuantitaChange(context);
					rigaFattura.setPrezzo_unitario(documentoEleLinea.getLineaPrezzounitario());
					action.doCalcolaTotaliDiRiga(context);
					if (documentoEleTestata.getModalitaPagamento() != null)
						rigaFattura.setModalita_pagamento(documentoEleTestata.getModalitaPagamento().getRif_modalita_pagamento());

			    	//TODO eliminata su richiesta di Patrizia fatturaPassivaBulk.setDt_scadenza(new java.sql.Timestamp(date.getTime().getTime())); 
			    	GregorianCalendar gcDataMinimaRiga = new GregorianCalendar(), gcDataMassimaRiga = new GregorianCalendar();
			    	gcDataMinimaRiga.setTime(documentoEleLinea.getInizioDatacompetenza()==null?gcDataMinima.getTime():documentoEleLinea.getInizioDatacompetenza());
			    	gcDataMassimaRiga.setTime(documentoEleLinea.getFineDatacompetenza()==null?gcDataMassima.getTime():documentoEleLinea.getFineDatacompetenza());
			    	
			    	if (fatturaPassivaBulk.getFl_fattura_compenso()) {
						rigaFattura.setDt_da_competenza_coge(new Timestamp(gcDataMinimaRiga.getTime().getTime()));
						rigaFattura.setDt_a_competenza_coge(new Timestamp(gcDataMassimaRiga.getTime().getTime()));	
			    	} else {
			    		if (gcDataMinimaRiga.get(Calendar.YEAR)<gcDataMinima.get(Calendar.YEAR))
			    			gcDataMinimaRiga = gcDataMinima;
			    		if (gcDataMassimaRiga.get(Calendar.YEAR)<gcDataMinima.get(Calendar.YEAR))
			    			gcDataMassimaRiga = gcDataMinima;
						rigaFattura.setDt_da_competenza_coge(new Timestamp(gcDataMinimaRiga.getTime().getTime()));
						rigaFattura.setDt_a_competenza_coge(new Timestamp(gcDataMassimaRiga.getTime().getTime()));	
			    	}
				}				
			}
			nbp.initializeModelForEditAllegati(context, fatturaPassivaBulk);
			return fatturaPassivaBulk;
		} catch (RemoteException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);		
		}			
	}

	private Voce_ivaBulk recuperaCodiceIVA(DocumentoEleTestataBulk documentoEleTestata, DocumentoEleLineaBulk documentoEleLinea) {
		for (DocumentoEleIvaBulk documentoEleIVA : documentoEleTestata.getDocEleIVAColl()) {
			if ((documentoEleLinea.getLineaAliquotaiva() != null && 
					documentoEleIVA.getAliquotaIva() != null && 
					documentoEleLinea.getLineaAliquotaiva().equals(documentoEleIVA.getAliquotaIva())) ||
					(documentoEleLinea.getLineaNatura() != null && 
					documentoEleIVA.getNatura() != null && 
					documentoEleLinea.getLineaNatura().equals(documentoEleIVA.getNatura())))
			return documentoEleIVA.getVoceIva();
		}		
		return null;
	}

	private Timestamp calcolaDataMinimaCompetenza(
			DocumentoEleTestataBulk documentoEleTestata) {
		java.sql.Timestamp inizioDatacompetenza = EJBCommonServices.getServerDate();
		for (DocumentoEleLineaBulk documentoEleLinea : documentoEleTestata.getDocEleLineaColl()) {
			if (inizioDatacompetenza == null)
				inizioDatacompetenza = documentoEleLinea.getInizioDatacompetenza();
			else
				inizioDatacompetenza = DateUtils.min(inizioDatacompetenza, documentoEleLinea.getInizioDatacompetenza());
		}		
		return inizioDatacompetenza;
	}
	private Timestamp calcolaDataMassimaCompetenza(
			DocumentoEleTestataBulk documentoEleTestata) {
		java.sql.Timestamp fineDatacompetenza = null;
		for (DocumentoEleLineaBulk documentoEleLinea : documentoEleTestata.getDocEleLineaColl()) {
			if (fineDatacompetenza == null)
				fineDatacompetenza = documentoEleLinea.getFineDatacompetenza();
			else
				fineDatacompetenza = DateUtils.max(fineDatacompetenza, documentoEleLinea.getFineDatacompetenza());
		}		
		if (fineDatacompetenza == null)
			fineDatacompetenza = EJBCommonServices.getServerDate();
		return fineDatacompetenza;
	}

	private Boolean existsTributi(DocumentoEleTestataBulk documentoEleTestata) {
		for (DocumentoEleTributiBulk documentoEleTributi : documentoEleTestata.getDocEleTributiColl()) {
			if (documentoEleTributi.getImporto() != null && BigDecimal.ZERO.compareTo(documentoEleTributi.getImporto()) != 0)
				return Boolean.TRUE;
		}	
		if (documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale()!= null && 
			(documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_02.name()) ||
			 documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_19.name()))		
		   )
			return Boolean.TRUE;
			
	return Boolean.FALSE;
	}

	@Override
	protected String getStorePath(DocumentoEleTestataBulk documentoEleTestata, boolean create) throws BusinessProcessException {
		return Optional.ofNullable(documentoEleTestata)
				.map(DocumentoEleTestataBulk::getDocumentoEleTrasmissione)
				.map(DocumentoEleTrasmissioneBase::getCmisNodeRef)
				.map(s -> {
					return Optional.ofNullable(SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(s))
							.map(StorageObject::getPath)
							.orElse(null);
				})
				.orElse(null);
	}

	@Override
	protected Class<AllegatoFatturaBulk> getAllegatoClass() {
		return AllegatoFatturaBulk.class;
	}	
	
	@Override
	protected boolean excludeChild(StorageObject storageObject) {
        if (Stream.of(crudDocEleAllegatiColl.getDetails().stream().toArray())
                .filter(DocumentoEleAllegatiBulk.class::isInstance)
                .map(DocumentoEleAllegatiBulk.class::cast)
                .map(DocumentoEleAllegatiBulk::getCmisNodeRef)
                .anyMatch(s -> s.equals(storageObject.getKey()))){
            return true;
        }
        if (storageObject.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value())
                .equalsIgnoreCase("D:sigla_fatture_attachment:document"))
            return true;
        return super.excludeChild(storageObject);
	}
	
	@Override
	protected void completeAllegato(AllegatoFatturaBulk allegato) throws ApplicationException {
        Optional.ofNullable(SpringUtil.getBean("storeService", StoreService.class).getStorageObjectBykey(allegato.getStorageKey()))
                .map(storageObject -> storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
                .map(strings -> strings.stream())
                .ifPresent(stringStream -> {
                    stringStream
                            .filter(s -> AllegatoFatturaBulk.aspectNamesDecorrenzaTerminiKeys.get(s) != null)
                            .findFirst()
                            .ifPresent(s -> allegato.setAspectName(s));
                });
        super.completeAllegato(allegato);
	}
	
	@Override
	public String getAllegatiFormName() {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) getModel();
		if (documentoEleTestata.isRicevutaDecorrenzaTermini())
			return "decorrenzaTermini";
		return super.getAllegatiFormName();
	}
	
	@Override
	public void save(ActionContext actioncontext) throws ValidationException,
			BusinessProcessException {
		for (Object obj : getCrudArchivioAllegati().getDetails()) {
			AllegatoFatturaBulk allegatoFatturaBulk = (AllegatoFatturaBulk)obj;
			if (allegatoFatturaBulk != null && allegatoFatturaBulk.getAspectName() != null && 
					allegatoFatturaBulk.getAspectName().equalsIgnoreCase("P:sigla_fatture_attachment:comunicazione_non_registrabilita")) {
				((DocumentoEleTestataBulk) getModel()).setFlIrregistrabile("S");				
			}
		}		
		super.save(actioncontext);
	}

	public boolean isEsercizioAperto() {
		return esercizioAperto;
	}

	public void setEsercizioAperto(boolean esercizioAperto) {
		this.esercizioAperto = esercizioAperto;
	}
}