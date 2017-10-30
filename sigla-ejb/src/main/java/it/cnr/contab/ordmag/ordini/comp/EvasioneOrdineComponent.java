package it.cnr.contab.ordmag.ordini.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoHome;
import it.cnr.contab.anagraf00.core.bulk.Anagrafico_terzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.contratto.bulk.Ass_contratto_uoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.ejb.CategoriaGruppoInventComponentSession;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaHome;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileHome;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperHome;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagHome;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagBulk;
import it.cnr.contab.ordmag.anag00.LuogoConsegnaMagHome;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoHome;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagHome;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagAzHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraHome;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdKey;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagHome;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoRigaMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.magazzino.ejb.MovimentiMagComponentSession;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineHome;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.TipoOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.TipoOrdineHome;
import it.cnr.contab.ordmag.ordini.dto.ImportoOrdine;
import it.cnr.contab.ordmag.ordini.dto.ParametriCalcoloImportoOrdine;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class EvasioneOrdineComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,Cloneable,Serializable {

	public final static String TIPO_TOTALE_COMPLETO = "C";
	public final static String TIPO_TOTALE_PARZIALE = "P";
	private final static int INSERIMENTO = 1;
	private final static int MODIFICA    = 2;
	private final static int CANCELLAZIONE    = 3;		
	
    public  EvasioneOrdineComponent()
    {
    }
    
    public EvasioneOrdineBulk cercaOrdini(UserContext context, EvasioneOrdineBulk filtro)
    		throws ComponentException {

    		OrdineAcqConsegnaHome home = (OrdineAcqConsegnaHome)getHome(context, OrdineAcqConsegnaBulk.class);
    		it.cnr.jada.persistency.sql.SQLBuilder sql = ricercaOrdini(context, filtro, home);

    		try {
				Collection ordini = home.fetchAll(sql);
				it.cnr.jada.bulk.BulkHome homeMag= getHome(context, MagazzinoBulk.class);
				it.cnr.jada.bulk.BulkHome homeLuogo= getHome(context, LuogoConsegnaMagBulk.class);
				it.cnr.jada.bulk.BulkHome homeUop= getHome(context, UnitaOperativaOrdBulk.class);
				it.cnr.jada.bulk.BulkHome homeRiga= getHome(context, OrdineAcqRigaBulk.class);
				it.cnr.jada.bulk.BulkHome homeIva= getHome(context, Voce_ivaBulk.class);
				
				it.cnr.jada.bulk.BulkHome homeOrdine= getHome(context, OrdineAcqBulk.class);
				it.cnr.jada.bulk.BulkHome homeBene= getHome(context, Bene_servizioBulk.class);
				it.cnr.jada.bulk.BulkHome homeUm= getHome(context, UnitaMisuraBulk.class);
				it.cnr.jada.bulk.BulkHome homeObbligazioneScad= getHome(context, Obbligazione_scadenzarioBulk.class);
				it.cnr.jada.bulk.BulkHome homeObbligazione= getHome(context, ObbligazioneBulk.class);
				for (Iterator j = ordini.iterator(); j.hasNext();) {
					OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk) j.next();
					OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)homeRiga.findByPrimaryKey(cons.getOrdineAcqRiga());
					OrdineAcqBulk ordine = (OrdineAcqBulk)homeOrdine.findByPrimaryKey(riga.getOrdineAcq());
					riga.setOrdineAcq(ordine);
					Bene_servizioBulk bene = (Bene_servizioBulk)homeBene.findByPrimaryKey(riga.getBeneServizio());
					if (cons.getObbligazioneScadenzario() != null){
						Obbligazione_scadenzarioBulk obblScad = (Obbligazione_scadenzarioBulk)homeObbligazioneScad.findByPrimaryKey(cons.getObbligazioneScadenzario());
						ObbligazioneBulk obbl = (ObbligazioneBulk)homeObbligazione.findByPrimaryKey(obblScad.getObbligazione());
						obblScad.setObbligazione(obbl);
						cons.setObbligazioneScadenzario(obblScad);
					}
					if (cons.getMagazzino() != null){
						MagazzinoBulk mag = (MagazzinoBulk)homeMag.findByPrimaryKey(cons.getMagazzino());
						cons.setMagazzino(mag);
					}
					if (cons.getUnitaOperativaOrd() != null){
						UnitaOperativaOrdBulk uop = (UnitaOperativaOrdBulk)homeUop.findByPrimaryKey(cons.getUnitaOperativaOrd());
						cons.setUnitaOperativaOrd(uop);
					}
					if (cons.getLuogoConsegnaMag() != null){
						LuogoConsegnaMagBulk luogo = (LuogoConsegnaMagBulk)homeLuogo.findByPrimaryKey(cons.getLuogoConsegnaMag());
						cons.setLuogoConsegnaMag(luogo);
					}
					if (riga.getUnitaMisura() != null){
						UnitaMisuraBulk um = (UnitaMisuraBulk)homeUm.findByPrimaryKey(riga.getUnitaMisura());
						riga.setUnitaMisura(um);
					}
					if (riga.getVoceIva() != null){
						Voce_ivaBulk iva = (Voce_ivaBulk)homeIva.findByPrimaryKey(riga.getVoceIva());
						riga.setVoceIva(iva);
					}
					riga.setBeneServizio(bene);
					cons.setOrdineAcqRiga(riga);
					
					filtro.addToRigheOrdineColl(cons);
				}
				return filtro;
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			}
    	}

    private it.cnr.jada.persistency.sql.SQLBuilder ricercaOrdini(UserContext context,
    		EvasioneOrdineBulk filtro, OrdineAcqConsegnaHome home) throws ApplicationException {
    	
    	if (filtro.getDataBolla() == null){
    		throw new it.cnr.jada.comp.ApplicationException("E' necessario valorizzare la data della bolla.");    	
    	}
    	if (filtro.getNumeroBolla() == null){
    		throw new it.cnr.jada.comp.ApplicationException("E' necessario valorizzare il numero della bolla.");    	
    	}
    	if (filtro.getDataConsegna() == null){
    		throw new it.cnr.jada.comp.ApplicationException("E' necessario valorizzare la data di consegna.");    	
    	}
    	if (filtro.getCdMagazzino() == null){
    		throw new it.cnr.jada.comp.ApplicationException("E' necessario valorizzare il magazzino.");    	
    	}
    	
    	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();

        sql.setAutoJoins(true);

        sql.addSQLClause(FindClause.AND, "ORDINE_ACQ_CONSEGNA.STATO_FATT", SQLBuilder.EQUALS, OrdineAcqConsegnaBulk.STATO_FATT_NON_ASSOCIATA);
        sql.addSQLClause(FindClause.AND, "ORDINE_ACQ_CONSEGNA.STATO", SQLBuilder.EQUALS, OrdineAcqConsegnaBulk.STATO_INSERITA);

        sql.generateJoin("ordineAcqRiga", "ORDINE_ACQ_RIGA");
    	
        sql.generateJoin(OrdineAcqRigaBulk.class, OrdineAcqBulk.class, "ordineAcq", "ORDINE_ACQ");
        sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.STATO", SQLBuilder.EQUALS, OrdineAcqBulk.STATO_DEFINITIVO);

        Optional.ofNullable(filtro.getNumerazioneMag())
        .ifPresent(numerazioneMag -> Optional.ofNullable(numerazioneMag.getCdMagazzino())
                .ifPresent(cdMagazzino -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ_CONSEGNA.CD_MAGAZZINO", SQLBuilder.EQUALS, cdMagazzino);
                })
        );
        Optional.ofNullable(filtro.getFind_data_ordine())
        .ifPresent(find_data_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.DATA_ORDINE", SQLBuilder.EQUALS, find_data_ordine);
                }
        );
        Optional.ofNullable(filtro.getFind_cd_numeratore_ordine())
        .ifPresent(find_cd_numeratore_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.CD_NUMERATORE", SQLBuilder.EQUALS, find_cd_numeratore_ordine);
                }
        );
        Optional.ofNullable(filtro.getFind_esercizio_ordine())
        .ifPresent(find_esercizio_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.ESERCIZIO", SQLBuilder.EQUALS, find_esercizio_ordine);
                }
        );
        Optional.ofNullable(filtro.getFind_numero_ordine())
        .ifPresent(find_numero_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.NUMERO", SQLBuilder.EQUALS, find_numero_ordine);
                }
        );
        Optional.ofNullable(filtro.getFind_riga_ordine())
        .ifPresent(find_riga_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ_RIGA.RIGA", SQLBuilder.EQUALS, find_riga_ordine);
                }
        );
        Optional.ofNullable(filtro.getFind_consegna_ordine())
        .ifPresent(find_consegna_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ_CONSEGNA.CONSEGNA", SQLBuilder.EQUALS, find_consegna_ordine);
                }
        );
        Optional.ofNullable(filtro.getFind_cd_uop_ordine())
        .ifPresent(find_cd_uop_ordine -> {
                    sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.CD_UOP_ORDINE", SQLBuilder.EQUALS, find_cd_uop_ordine);
                }
        );
        sql.addSQLClause(FindClause.AND, "ORDINE_ACQ.DATA_ORDINE", SQLBuilder.LESS_EQUALS, filtro.getDataConsegna());

        if (filtro.getFind_cd_precedente() != null || filtro.getFind_cd_terzo() != null || filtro.getFind_ragione_sociale() != null ){
            sql.generateJoin(OrdineAcqBulk.class, TerzoBulk.class, "fornitore", "TERZO");
        	
            Optional.ofNullable(filtro.getFind_cd_terzo())
            .ifPresent(find_cd_terzo -> {
                        sql.addSQLClause(FindClause.AND, "TERZO.CD_TERZO", SQLBuilder.EQUALS, find_cd_terzo);
                    }
            );

            Optional.ofNullable(filtro.getFind_cd_precedente())
            .ifPresent(find_cd_precedente -> {
                        sql.addSQLClause(FindClause.AND, "TERZO.CD_PRECEDENTE", SQLBuilder.EQUALS, find_cd_precedente);
                    }
            );
            Optional.ofNullable(filtro.getFind_ragione_sociale())
            .ifPresent(find_ragione_sociale -> {
            			sql.generateJoin(TerzoBulk.class, AnagraficoBulk.class, "anagrafico", "ANAGRAFICO");
                        sql.addSQLClause(FindClause.AND, "ANAGRAFICO.RAGIONE_SOCIALE", SQLBuilder.EQUALS, find_ragione_sociale);
                    }
            );
        }

        
    	return sql;
    } 

    public RemoteIterator preparaQueryBolleScaricoDaVisualizzare(UserContext userContext, List<BollaScaricoMagBulk> bolle)throws ComponentException{

		BollaScaricoMagHome homeBolla= (BollaScaricoMagHome)getHome(userContext, BollaScaricoMagBulk.class);
		return iterator(userContext,
				homeBolla.selectBolleGenerate(bolle),
				BollaScaricoMagBulk.class,
				"dafault");
    }
    public List<BollaScaricoMagBulk> evadiOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine)throws ComponentException, PersistencyException{
    	List<BollaScaricoMagBulk> listaBolleScarico = new ArrayList<>();
    	OrdineAcqComponentSession ordineComponent = (OrdineAcqComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRORDMAG00_EJB_OrdineAcqComponentSession", OrdineAcqComponentSession.class);
    	MovimentiMagComponentSession movimentiMagComponent = (MovimentiMagComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRORDMAG00_EJB_MovimentiMagComponentSession", MovimentiMagComponentSession.class);
    	List<MovimentiMagBulk> listaMovimentiScarico = new ArrayList<>();
    	List<EvasioneOrdineRigaBulk> listaRigheEvase = new ArrayList<>();
    	if (evasioneOrdine.getRigheConsegnaSelezionate() != null && !evasioneOrdine.getRigheConsegnaSelezionate().isEmpty()){
    		List<OrdineAcqBulk> listaOrdiniConConsegneEvase = new ArrayList<OrdineAcqBulk>();
    		for (Iterator i = evasioneOrdine.getRigheConsegnaSelezionate().iterator(); i.hasNext();) {
    			OrdineAcqConsegnaBulk consegna = ((OrdineAcqConsegnaBulk)i.next());
    			if (consegna.getQuantita() == null){
    				throw new ApplicationException("Indicare la quantit� da evadere per la consegna "+consegna.getConsegnaOrdineString());
    			}
    			if (consegna.isQuantitaEvasaMinoreOrdine() && consegna.getOperazioneQuantitaEvasaMinore() == null){
    				throw new ApplicationException("Per la consegna "+consegna.getConsegnaOrdineString()+" � necessario indicare se sdoppiare la riga o evaderla forzatamente");
    			}
    			OrdineAcqBulk ordineConsegna = new OrdineAcqBulk(consegna.getCdCds(),consegna.getCdUnitaOperativa(),consegna.getEsercizio(),
    					consegna.getCdNumeratore(),consegna.getNumero());
    			OrdineAcqBulk ordine = null;
    			Boolean ordineEsistente = false;
    			for (Iterator k = listaOrdiniConConsegneEvase.iterator(); k.hasNext();) {
    				ordine = ((OrdineAcqBulk)k.next());
    				if (ordine.equalsByPrimaryKey(ordineConsegna)){
    					break;
    				}
    			}
    			if (!ordineEsistente){
    				ordine = (OrdineAcqBulk)getTempHome(userContext, OrdineAcqBulk.class).findByPrimaryKey(ordineConsegna);
    				if (ordine != null){
						try {
							ordine = (OrdineAcqBulk)ordineComponent.inizializzaBulkPerModifica(userContext, ordine);
						} catch (RemoteException e) {
							throw handleException(e);
						}
						listaOrdiniConConsegneEvase.add(ordine);
    				}
    			}

    			evasioneOrdine.setStato(OrdineAcqConsegnaBulk.STATO_INSERITA);
    			evasioneOrdine.setToBeCreated();
    			EvasioneOrdineRigaBulk evasioneOrdineRiga = new EvasioneOrdineRigaBulk();
    			evasioneOrdineRiga.setEvasioneOrdine(evasioneOrdine);
    			evasioneOrdineRiga.setOrdineAcqConsegna(consegna);
    			evasioneOrdineRiga.setStato(OrdineAcqConsegnaBulk.STATO_INSERITA);
    			evasioneOrdineRiga.setQuantitaEvasa(consegna.getQuantita());
    			evasioneOrdineRiga.setRiga(new Long (listaRigheEvase.size() + 1));
    			evasioneOrdineRiga.setToBeCreated();
    			listaRigheEvase.add(evasioneOrdineRiga);

    			try {
					listaMovimentiScarico = movimentiMagComponent.caricoDaOrdine(userContext, evasioneOrdine, consegna, ordine, evasioneOrdineRiga, listaMovimentiScarico);
				} catch (RemoteException e) {
					throw new ComponentException(e);
				}

    			for (Iterator k = listaOrdiniConConsegneEvase.iterator(); k.hasNext();) {
    				OrdineAcqBulk ordineAcq = ((OrdineAcqBulk)k.next());
        			for (Object bulkRiga :ordineAcq.getRigheOrdineColl()) {
        				OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)bulkRiga;
        				if (riga.equalsByPrimaryKey(consegna.getOrdineAcqRiga())){
        					BulkList listaConsegneRiga = new BulkList<>(riga.getRigheConsegnaColl());
                			for (Object bulkCons : listaConsegneRiga) {
                				OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk)bulkCons;
                				if (cons.equalsByPrimaryKey(consegna)){
                					if (cons.getStato().equals(OrdineAcqConsegnaBulk.STATO_EVASA)){
                	    				throw new ApplicationException("La consegna "+consegna.getConsegnaOrdineString()+" � stata gi� evasa");
                					}
                					consegna.setStato(OrdineAcqConsegnaBulk.STATO_EVASA);
                					consegna.setToBeUpdated();
                					riga.getRigheConsegnaColl().removeByPrimaryKey(cons);
                					riga.getRigheConsegnaColl().add(consegna);
                	    			if (consegna.isQuantitaEvasaMinoreOrdine() && consegna.isOperazioneCreaNuovaConsegna()){
                	    				OrdineAcqConsegnaBulk newConsegna = (OrdineAcqConsegnaBulk)consegna.clone();
                	    				newConsegna = (OrdineAcqConsegnaBulk)newConsegna.inizializza();
                	    				newConsegna.setVecchiaConsegna(consegna.getConsegna());
                	    				newConsegna.setQuantita(consegna.getQuantitaOriginaria().subtract(consegna.getQuantita()));
                	    				newConsegna.setCrudStatus(INSERIMENTO);
                	    				riga.addToRigheConsegnaColl(newConsegna);
                	    				ordine.addToOrdineObbligazioniHash(newConsegna.getObbligazioneScadenzario(), newConsegna);
                	    			}
                				}
            				}
        				}
        			}
    			}
    		}
			for (Iterator k = listaOrdiniConConsegneEvase.iterator(); k.hasNext();) {
				OrdineAcqBulk ordine = ((OrdineAcqBulk)k.next());
				try {
					ordineComponent.modificaConBulk(userContext, ordine);
				} catch (RemoteException e) {
					throw handleException(e);
				}
				
			}
    		evasioneOrdine.setListaRigheConsegnaEvase(new BulkList<>(listaRigheEvase));
    		evasioneOrdine = (EvasioneOrdineBulk)creaConBulk(userContext, evasioneOrdine);
    		
    		if (!listaMovimentiScarico.isEmpty()){
        		try {
        			listaBolleScarico = movimentiMagComponent.generaBollaScarico(userContext, listaMovimentiScarico);
				} catch (RemoteException e) {
					throw handleException(e);
				}
    		}
			
    	}
		return listaBolleScarico;
    }

    private Bene_servizioBulk recuperoBeneServizio(it.cnr.jada.UserContext userContext, String cdBeneServizio)
    		throws ComponentException, PersistencyException {
    	Bene_servizioHome home = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
    	Bene_servizioBulk bene = (Bene_servizioBulk)home.findByPrimaryKey(new Bene_servizioBulk(cdBeneServizio));
    	return bene;
    }
    private void assegnaProgressivo(UserContext userContext,EvasioneOrdineBulk evasioneOrdine) throws ComponentException {

	try {
		NumeratoriOrdMagComponentSession progressiviSession = (NumeratoriOrdMagComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRORDMAG_EJB_NumeratoriOrdMagComponentSession", NumeratoriOrdMagComponentSession.class);
		NumerazioneMagBulk numerazione = new NumerazioneMagBulk(evasioneOrdine.getCdCds(), evasioneOrdine.getCdMagazzino(), evasioneOrdine.getEsercizio(), evasioneOrdine.getCdNumeratoreMag());
		evasioneOrdine.setNumero(progressiviSession.getNextPG(userContext, numerazione));
	} catch (Throwable t) {
		throw handleException(evasioneOrdine, t);
	}
}
public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	return creaConBulk(userContext, bulk, null);
}
	public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
			throws it.cnr.jada.comp.ComponentException {

		EvasioneOrdineBulk evasione= (EvasioneOrdineBulk) bulk;
		assegnaProgressivo(userContext, evasione);
		evasione = (EvasioneOrdineBulk)super.creaConBulk(userContext, evasione);
		return evasione;
	}

//	public OrdineAcqBulk calcolaImportoOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws it.cnr.jada.comp.ComponentException{
//		if (ordine.getCambio() == null || ordine.getDivisa() == null || ordine.getDivisa().getCd_divisa() == null ){
//			throw new it.cnr.jada.comp.ApplicationException("Campi di testata ordine necessari per il calcolo dell'importo non valorizzati.");
//		}
//		ParametriCalcoloImportoOrdine parametriTestata = new ParametriCalcoloImportoOrdine();
//		parametriTestata.setCambio(ordine.getCambio());
//		parametriTestata.setDivisa(ordine.getDivisa());
//		parametriTestata.setDivisaRisultato(getEuro(userContext));
//		parametriTestata.setPercProrata(ordine.getPercProrata());
//		ordine.setImImponibile(BigDecimal.ZERO);
//		ordine.setImIva(BigDecimal.ZERO);
//		ordine.setImIvaD(BigDecimal.ZERO);
//		ordine.setImTotaleOrdine(BigDecimal.ZERO);
//    	for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//    		OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//    		if (riga == null){
//    			throw new it.cnr.jada.comp.ApplicationException("Dettaglio vuoto.");
//    		}
//    		if (riga.getCoefConv() == null || riga.getPrezzoUnitario() == null || riga.getVoceIva() == null || riga.getVoceIva().getCd_voce_iva() == null ){
//    			throw new it.cnr.jada.comp.ApplicationException("Campi di dettaglio ordine necessari per il calcolo dell'importo non valorizzati.");
//    		}
//    		ParametriCalcoloImportoOrdine parametriRiga = (ParametriCalcoloImportoOrdine)parametriTestata.clone();
//    		parametriRiga.setCoefacq(riga.getCoefConv());
//    		parametriRiga.setPrezzo(riga.getPrezzoUnitario());
//    		parametriRiga.setSconto1(riga.getSconto1());
//    		parametriRiga.setSconto2(riga.getSconto2());
//    		parametriRiga.setSconto3(riga.getSconto3());
//    		parametriRiga.setVoceIva(riga.getVoceIva());
//    		riga.setImImponibile(BigDecimal.ZERO);
//    		riga.setImImponibileDivisa(BigDecimal.ZERO);
//    		riga.setImIva(BigDecimal.ZERO);
//    		riga.setImIvaDivisa(BigDecimal.ZERO);
//    		riga.setImIvaD(BigDecimal.ZERO);
//    		riga.setImIvaNd(BigDecimal.ZERO);
//    		riga.setImTotaleRiga(BigDecimal.ZERO);
//    		if (riga.getRigheConsegnaColl() == null || riga.getRigheConsegnaColl().isEmpty()){
//        		if (riga.getDspQuantita() != null){
//            		gestioneSalvataggioRigaConsegnaSingola(riga);
//        		} else {
//        			throw new it.cnr.jada.comp.ApplicationException("Campi di dettaglio ordine necessari per il calcolo dell'importo non valorizzati.");
//        		}
//    		}
//    		for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//    			OggettoBulk consbulk= (OggettoBulk) c.next();
//    			OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//    			ParametriCalcoloImportoOrdine parametriCons = (ParametriCalcoloImportoOrdine)parametriRiga.clone();
//        		if (cons.getQuantita() == null ){
//        			throw new it.cnr.jada.comp.ApplicationException("Campi di consegna ordine necessari per il calcolo dell'importo non valorizzati.");
//        		}
//    			parametriCons.setQtaOrd(cons.getQuantita());
//    			parametriCons.setArrAliIva(cons.getArrAliIva());
//    			ImportoOrdine importo = calcoloImportoOrdine(parametriCons);
//    			cons.setImImponibile(importo.getImponibile());
//    			cons.setImImponibileDivisa(importo.getImponibile());
//    			cons.setImIva(importo.getImportoIva());
//    			cons.setImIvaDivisa(importo.getImportoIva());
//    			cons.setImIvaD(importo.getImportoIvaDetraibile());
//    			cons.setImIvaNd(importo.getImportoIvaInd());
//    			cons.setImTotaleConsegna(importo.getTotale());
//    			cons.setToBeUpdated();
//    			riga.setImImponibile(riga.getImImponibile().add(cons.getImImponibile()));
//    			riga.setImImponibileDivisa(riga.getImImponibileDivisa().add(cons.getImImponibileDivisa()));
//    			riga.setImIva(riga.getImIva().add(cons.getImIva()));
//    			riga.setImIvaDivisa(riga.getImIvaDivisa().add(cons.getImIvaDivisa()));
//    			riga.setImIvaD(riga.getImIvaD().add(cons.getImIvaD()));
//    			riga.setImIvaNd(riga.getImIvaNd().add(cons.getImIvaNd()));
//    			riga.setImTotaleRiga(riga.getImTotaleRiga().add(cons.getImTotaleConsegna()));
//    			riga.setToBeUpdated();
//    		}
//    	}
//		impostaTotaliOrdine(ordine);
//		return ordine;
//	}
//
//	public void impostaTotaliOrdine(OrdineAcqBulk ordine) {
//		BigDecimal imponibile = BigDecimal.ZERO, iva = BigDecimal.ZERO, ivaD = BigDecimal.ZERO, totale = BigDecimal.ZERO;
//		for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//			OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//			if (riga != null){
//				for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//					OggettoBulk consbulk= (OggettoBulk) c.next();
//					OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//					imponibile = imponibile.add(cons.getImImponibile());
//					iva = iva.add(cons.getImIva());
//					ivaD = ivaD.add(cons.getImIvaD());
//					totale = totale.add(cons.getImTotaleConsegna());
//				}
//			}
//		}
//		ordine.setImImponibile(imponibile);
//		ordine.setImIva(iva);
//		ordine.setImIvaD(ivaD);
//		ordine.setImTotaleOrdine(totale);
//	}
//	
//	
//	private void validaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws it.cnr.jada.comp.ComponentException{
//		controlloEsistenzaRigheOrdine(ordine);
//    	for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//    		OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//    		if (riga != null){
//    			gestioneSalvataggioRigaConsegnaSingola(riga);
//            	for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//            		OggettoBulk consbulk= (OggettoBulk) c.next();
//            		OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//            		if (cons.getObbligazioneScadenzario() == null || cons.getObbligazioneScadenzario().getPg_obbligazione() == null){
//                		cons.setObbligazioneScadenzario(riga.getDspObbligazioneScadenzario());
//            		}
//            		controlliValiditaConsegna(cons);
//            	}
//    		}
//    	}
//    	controllaQuadraturaObbligazioni(userContext, ordine);
//    	controlloCongruenzaObbligazioni(userContext, ordine);
//    	controlloCongruenzaFornitoreContratto(userContext, ordine);
//    }
//
//	private void controlloEsistenzaRigheOrdine(OrdineAcqBulk ordine) throws ApplicationException {
//		if (ordine.getRigheOrdineColl() == null || ordine.getRigheOrdineColl().size() == 0){
//			throw new ApplicationException ("Non � possibile salvare un ordine senza dettagli.");
//		} else {
//    		boolean esisteRigaValida = false;
//	    	for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//	    		OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//	    		if (!riga.isToBeDeleted()){
//	    			esisteRigaValida = true;
//	    		}
//	    	}
//	    	if (!esisteRigaValida){
//				throw new ApplicationException ("Non � possibile salvare un ordine senza dettagli.");
//	    	}
//		}
//			
//			
//			
//	}
//
//	private void controlliValiditaConsegna(OrdineAcqConsegnaBulk consegna)throws it.cnr.jada.comp.ComponentException{
//		if (!consegna.isConsegnaMagazzino()){
//			if (consegna.getCdUopDest() == null){
//				throw new ApplicationException("E' necessario indicare l'unit� operativa di destinazione per la riga "+consegna.getRiga()+".");
//			}
//		} else {
//			if (consegna.getCdUopDest() != null){
//				throw new ApplicationException("Per una consegna a magazzino non � possibile selezionare l'unit� operativa di destinazione per la riga "+consegna.getRiga()+".");
//			}
//		}
//		if (consegna.getDtPrevConsegna() != null && consegna.getDtPrevConsegna().before(consegna.getOrdineAcqRiga().getOrdineAcq().getDataOrdine())){
//			throw new ApplicationException("La data di prevista consegna non pu� essere precedente alla data dell'ordine per la riga "+consegna.getRiga()+".");
//		}
//
//	}
//	
//	private void controlloCongruenzaFornitoreContratto(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine)
//			throws ComponentException, ApplicationException {
//		if (ordine.getPgContratto() != null){
//    		ContrattoBulk contratto;
//			try {
//				contratto = retrieveContratto(userContext, ordine);
//	    		if (contratto != null){
//	        		if (!ordine.getFornitore().equalsByPrimaryKey(contratto.getFigura_giuridica_esterna())){
//						throw new ApplicationException ("Fornitore del contratto "+contratto.getFig_giu_esterna_codice()+" diverso dal fornitore indicato sull'ordine.");
//	        		}
//	    		}
//			} catch (PersistencyException e) {
//				throw new ComponentException(e);
//			}
//    	}
//	}
//
//	private void controlloCongruenzaObbligazioni(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine)
//			throws ComponentException, ApplicationException {
//		CategoriaGruppoInventComponentSession h= (CategoriaGruppoInventComponentSession)
//				EJBCommonServices.createEJB(
//							"CNRDOCAMM00_EJB_CategoriaGruppoInventComponentSession",
//							CategoriaGruppoInventComponentSession.class);
//		for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//    		OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//    		if (riga != null){
//            	for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//            		OggettoBulk consbulk= (OggettoBulk) c.next();
//            		OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//            		if (cons.getPgObbligazioneScad() != null){
//            			try {
//							ObbligazioneBulk obb = retrieveObbligazione(userContext, cons);
//							if (obb != null){
//								if (obb.getPg_contratto() != null){
//									if (!obb.getContratto().equalsByPrimaryKey(ordine.getContratto())){
//										throw new ApplicationException ("Contratto dell'impegno "+obb.getEsercizio_originale()+"/"+obb.getPg_obbligazione()+" diverso dal contratto indicato sull'ordine.");
//									}
//								}
//								controlloCongruenzaVoceCategoriaGruppo(userContext, h, riga, obb);
//								controlloCongruenzaUoImpegno(userContext, cons, obb);
//							}
//            			} catch (PersistencyException e) {
//							throw new ComponentException(e);
//						}
//            		}
//            	}
//    		}
//    	}
//	}
//
//	private void controlloCongruenzaVoceCategoriaGruppo(it.cnr.jada.UserContext userContext,
//			CategoriaGruppoInventComponentSession h, OrdineAcqRigaBulk riga, ObbligazioneBulk obb)
//			throws ComponentException, PersistencyException, ApplicationException {
//		if (obb.getElemento_voce() != null){
//			Bene_servizioBulk bene = recuperoBeneServizio(userContext, riga.getCdBeneServizio());
//			if (bene != null){
//				java.util.List titoliCapitoliCatGrp;
//				try {
//					titoliCapitoliCatGrp = h.findAssVoceFList(userContext, bene.getCategoria_gruppo());
//					if (titoliCapitoliCatGrp == null)
//						throw new it.cnr.jada.comp.ApplicationException("Alla categoria " + bene.getCd_categoria_gruppo() + "\" non � stato attribuita l'associazione al capitolo di spesa");
//					boolean trovataVoce = false;
//					for (java.util.Iterator k = titoliCapitoliCatGrp.iterator(); k.hasNext();) {
//						Categoria_gruppo_voceBulk assVoce = (Categoria_gruppo_voceBulk)k.next();
//						if (obb.getElemento_voce().equalsByPrimaryKey(assVoce.getElemento_voce())){
//							trovataVoce = true;
//						}
//					}
//					if (!trovataVoce){
//						throw new ApplicationException ("Per la riga "+riga.getRiga()+" la voce dell'obbligazione collegata "+obb.getElemento_voce().getCd_elemento_voce()+" non � associata alla categoria/gruppo del bene/servizio "+riga.getCdBeneServizio());
//					}
//				} catch (IntrospectionException | RemoteException e) {
//					throw new ComponentException(e);
//				}
//			}
//		}
//	}
//
//	private void controlloCongruenzaUoImpegno(it.cnr.jada.UserContext userContext,
//			OrdineAcqConsegnaBulk cons, ObbligazioneBulk obb)
//			throws ComponentException, PersistencyException, ApplicationException {
//		if (obb.getCd_unita_organizzativa() != null){
//			Unita_organizzativaBulk uoOrdine = recuperoUoPerImpegno(userContext, cons);
//			if (uoOrdine != null && !uoOrdine.getCd_unita_organizzativa().equals(obb.getCd_unita_organizzativa())){
//				throw new ApplicationException ("Per la consegna "+cons.getConsegna()+" della riga "+cons.getRiga()+ " la uo dell'obbligazione non � corretta.");
//			}
//		}
//	}
//
//	private void gestioneSalvataggioRigaConsegnaSingola(OrdineAcqRigaBulk riga) throws ApplicationException {
//		if ((riga.isToBeCreated() && riga.getRigheConsegnaColl() == null || riga.getRigheConsegnaColl().isEmpty()) 
//				){
//			if (riga.getDspQuantita() == null){
//				throw new ApplicationException ("E' necessario indicare la quantit�.");
//			}
//			if (riga.getDspDtPrevConsegna() == null){
//				throw new ApplicationException ("E' necessario indicare la data di prevista consegna.");
//			}
//			if (riga.getDspTipoConsegna() == null){
//				throw new ApplicationException ("E' necessario indicare il tipo di consegna.");
//			} else {
//				if ((!riga.getDspTipoConsegna().equals(Bene_servizioBulk.TIPO_CONSEGNA_MAGAZZINO)) && (riga.getDspUopDest() == null || riga.getDspUopDest().getCdUnitaOperativa() == null)){
//					throw new ApplicationException ("E' necessario indicare l'unit� operativa per i tipi consegna in 'Transito' o 'Fuori Magazzino'.");
//				}
//				
//			}
//			if (riga.getDspMagazzino() == null || riga.getDspMagazzino().getCdMagazzino() == null){
//				throw new ApplicationException ("E' necessario indicare il magazzino.");
//			}
//			if (riga.getDspLuogoConsegna() == null || riga.getDspLuogoConsegna().getCdLuogoConsegna() == null){
//				throw new ApplicationException ("E' necessario indicare il luogo di consegna.");
//			}
//			OrdineAcqConsegnaBulk consegna = null;
//			if (riga.isToBeCreated()){
//				consegna = new OrdineAcqConsegnaBulk();
//				consegna.setOrdineAcqRiga(riga);
//				consegna.setStato(OrdineAcqConsegnaBulk.STATO_INSERITA);
//				consegna.setRiga(riga.getRiga());
//				consegna.setConsegna(1);
//				consegna.setToBeCreated();
//			} else {
//				consegna = (OrdineAcqConsegnaBulk)riga.getRigheConsegnaColl().get(0);
//				riga.getRigheConsegnaColl().remove(consegna);
//				consegna.setToBeUpdated();
//			}
//			consegna.setStato(OrdineAcqConsegnaBulk.STATO_INSERITA);
//			consegna.setStatoFatt(OrdineAcqConsegnaBulk.STATO_FATT_NON_ASSOCIATA);
//			consegna.setImImponibile(riga.getImImponibile());
//			consegna.setImImponibileDivisa(riga.getImImponibileDivisa());
//			consegna.setImIva(riga.getImIva());
//			consegna.setImIvaDivisa(riga.getImIvaDivisa());
//			consegna.setImTotaleConsegna(riga.getImTotaleRiga());
//			consegna.setImIvaD(riga.getImIvaD());
//			consegna.setImIvaNd(riga.getImIvaNd());
//
//			consegna.setLuogoConsegnaMag(riga.getDspLuogoConsegna());
//			consegna.setMagazzino(riga.getDspMagazzino());
//			consegna.setDtPrevConsegna(riga.getDspDtPrevConsegna());
//			consegna.setQuantita(riga.getDspQuantita());
//			consegna.setTipoConsegna(riga.getDspTipoConsegna());
//			consegna.setUnitaOperativaOrd(riga.getDspUopDest());
//			riga.getRigheConsegnaColl().add(consegna);
//		}
//	}
//	
//public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
//
//	return bulk;
//
//}
//
@Override
public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
	return inizializzaEvasioneOrdine(usercontext, oggetto);
}

//@Override
//public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
//		throws ComponentException {
//	OrdineAcqBulk ordine = (OrdineAcqBulk)super.inizializzaBulkPerModifica(usercontext, oggettobulk);
//
//	ordine.setIsAbilitatoTuttiMagazzini(isAbilitatoTuttiMagazzini(usercontext, ordine));
//	
//	it.cnr.jada.bulk.BulkHome homeRiga= getHome(usercontext, OrdineAcqRigaBulk.class);
//    it.cnr.jada.persistency.sql.SQLBuilder sql= homeRiga.createSQLBuilder();
//    sql.addClause("AND", "numero", sql.EQUALS, ordine.getNumero());
//    sql.addClause("AND", "cdCds", sql.EQUALS, ordine.getCdCds());
//    sql.addClause("AND", "cdUnitaOperativa", sql.EQUALS, ordine.getCdUnitaOperativa());
//    sql.addClause("AND", "esercizio", sql.EQUALS, ordine.getEsercizio());
//    sql.addClause("AND", "cdNumeratore", sql.EQUALS, ordine.getCdNumeratore());
//	sql.addOrderBy("cd_cds");
//	sql.addOrderBy("cd_unita_operativa");
//	sql.addOrderBy("esercizio");
//	sql.addOrderBy("cd_numeratore");
//	sql.addOrderBy("numero");
//	sql.addOrderBy("riga");
//
//    try {
//    	ordine.setRigheOrdineColl(new it.cnr.jada.bulk.BulkList(homeRiga.fetchAll(sql)));
//
//    	for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//    		OggettoBulk rigabulk= (OggettoBulk) i.next();
//    		OrdineAcqRigaBulk riga= (OrdineAcqRigaBulk) rigabulk;
//    		if (riga.getBeneServizio() != null){
//    			Bene_servizioBulk bene = recuperoBeneServizio(usercontext, riga.getCdBeneServizio());
//    			riga.setBeneServizio(bene);
//    			riga.setTipoConsegnaDefault(bene.getTipoGestione());
//    		}
//    		if (riga.getUnitaMisura() != null){
//    			UnitaMisuraHome home = (UnitaMisuraHome)getHome(usercontext, UnitaMisuraBulk.class);
//    			UnitaMisuraBulk um = (UnitaMisuraBulk)home.findByPrimaryKey(new UnitaMisuraBulk(riga.getCdUnitaMisura()));
//    			riga.setUnitaMisura(um);
//    		}
//    		if (riga.getVoceIva() != null){
//    			Voce_ivaHome home = (Voce_ivaHome)getHome(usercontext, Voce_ivaBulk.class);
//    			Voce_ivaBulk voce = (Voce_ivaBulk)home.findByPrimaryKey(new Voce_ivaBulk(riga.getCdVoceIva()));
//    			riga.setVoceIva(voce);
//    		}
//    		it.cnr.jada.bulk.BulkHome homeConsegna= getHome(usercontext, OrdineAcqConsegnaBulk.class);
//    	    it.cnr.jada.persistency.sql.SQLBuilder sqlConsegna= homeConsegna.createSQLBuilder();
//    	    sqlConsegna.addClause("AND", "numero", sql.EQUALS, ordine.getNumero());
//    	    sqlConsegna.addClause("AND", "cdCds", sql.EQUALS, ordine.getCdCds());
//    	    sqlConsegna.addClause("AND", "cdUnitaOperativa", sql.EQUALS, ordine.getCdUnitaOperativa());
//    	    sqlConsegna.addClause("AND", "esercizio", sql.EQUALS, ordine.getEsercizio());
//    	    sqlConsegna.addClause("AND", "cdNumeratore", sql.EQUALS, ordine.getCdNumeratore());
//    	    sqlConsegna.addClause("AND", "riga", sql.EQUALS, riga.getRiga());
//    		sqlConsegna.addOrderBy("consegna");
//        	riga.setRigheConsegnaColl(new it.cnr.jada.bulk.BulkList(homeConsegna.fetchAll(sqlConsegna)));
//        	Obbligazione_scadenzarioBulk scadenzaComune = null;
//        	Boolean esisteScadenzaComune = false;
//        	for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//        		OggettoBulk consbulk= (OggettoBulk) c.next();
//        		OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//        		if (cons.getLuogoConsegnaMag() != null){
//        			LuogoConsegnaMagHome home = (LuogoConsegnaMagHome)getHome(usercontext, LuogoConsegnaMagBulk.class);
//        			LuogoConsegnaMagBulk luogo = (LuogoConsegnaMagBulk)home.findByPrimaryKey(new LuogoConsegnaMagBulk(cons.getCdCdsLuogo(), cons.getCdLuogoConsegna()));
//        			cons.setLuogoConsegnaMag(luogo);
//        		}
//        		if (cons.getMagazzino() != null){
//        			MagazzinoBulk mag = recuperoMagazzino(usercontext, cons);
//        			cons.setMagazzino(mag);
//        		}
//        		if (cons.getObbligazioneScadenzario() != null){
//        			Obbligazione_scadenzarioBulk scad = retrieveObbligazioneScadenzario(usercontext, cons);
//        			cons.setObbligazioneScadenzario(scad);
//        			if (scadenzaComune == null || scadenzaComune.equalsByPrimaryKey(scad)){
//            			esisteScadenzaComune = true;
//        				scadenzaComune = scad;
//        			} else {
//            			esisteScadenzaComune = false;
//        			}
//        		} else {
//        			esisteScadenzaComune = false;
//        		}
//        		if (cons.getUnitaOperativaOrd() != null){
//        			UnitaOperativaOrdBulk uop = recuperoUopDest(usercontext, cons);
//        			cons.setUnitaOperativaOrd(uop);
//        		}
//        		if (riga.getRigheConsegnaColl().size() == 1){
//        			riga.setDspDtPrevConsegna(cons.getDtPrevConsegna());
//        			riga.setDspLuogoConsegna(cons.getLuogoConsegnaMag());
//        			riga.setDspMagazzino(cons.getMagazzino());
//        			riga.setDspQuantita(cons.getQuantita());
//        			riga.setDspTipoConsegna(cons.getTipoConsegna());
//        			riga.setDspUopDest(cons.getUnitaOperativaOrd());
//        		}
//        	}
//        	if (esisteScadenzaComune){
//            	riga.setDspObbligazioneScadenzario(scadenzaComune);
//        	}
//    	}
//
//    } catch (PersistencyException e) {
//    	throw handleException(e);
//    }
//        
//    rebuildObbligazioni(usercontext, ordine);
//    return inizializzaOrdine(usercontext, (OggettoBulk)ordine, false);
//}
//
//private MagazzinoBulk recuperoMagazzino(UserContext usercontext, OrdineAcqConsegnaBulk cons) throws ComponentException, PersistencyException {
//	MagazzinoHome home = (MagazzinoHome)getHome(usercontext, MagazzinoBulk.class);
//	MagazzinoBulk mag = (MagazzinoBulk)home.findByPrimaryKey(new MagazzinoBulk(cons.getCdCdsMag(), cons.getCdMagazzino()));
//	return mag;
//}
//
//private UnitaOperativaOrdBulk recuperoUopDest(UserContext usercontext, OrdineAcqConsegnaBulk cons)
//		throws ComponentException, PersistencyException {
//	UnitaOperativaOrdHome home = (UnitaOperativaOrdHome)getHome(usercontext, UnitaOperativaOrdBulk.class);
//	UnitaOperativaOrdBulk uop = (UnitaOperativaOrdBulk)home.findByPrimaryKey(new UnitaOperativaOrdBulk(cons.getCdUnitaOperativa()));
//	return uop;
//}
//
//private UnitaOperativaOrdBulk recuperoUop(UserContext usercontext, UnitaOperativaOrdBulk uop)
//		throws ComponentException, PersistencyException {
//	UnitaOperativaOrdHome home = (UnitaOperativaOrdHome)getHome(usercontext, UnitaOperativaOrdBulk.class);
//	UnitaOperativaOrdBulk uo = (UnitaOperativaOrdBulk)home.findByPrimaryKey(new UnitaOperativaOrdBulk(uop.getCdUnitaOperativa()));
//	return uo;
//}
//
//private Obbligazione_scadenzarioBulk retrieveObbligazioneScadenzario(UserContext usercontext,
//		OrdineAcqConsegnaBulk cons) throws ComponentException, PersistencyException {
//	Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome)getHome(usercontext, Obbligazione_scadenzarioBulk.class);
//	Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk)home.findByPrimaryKey(new Obbligazione_scadenzarioBulk(cons.getCdCdsObbl(), cons.getEsercizioObbl(), cons.getEsercizioOrigObbl(), cons.getPgObbligazione(), cons.getPgObbligazioneScad()));
//	return scad;
//}
//
//private ContrattoBulk retrieveContratto(UserContext usercontext,
//		OrdineAcqBulk ordine) throws ComponentException, PersistencyException {
//	ContrattoHome home = (ContrattoHome)getHome(usercontext, ContrattoBulk.class);
//	ContrattoBulk bulk = (ContrattoBulk)home.findByPrimaryKey(new ContrattoBulk(ordine.getEsercizioContratto(), ordine.getStato(), ordine.getPgContratto()));
//	return bulk;
//}
//
//private ObbligazioneBulk retrieveObbligazione(UserContext usercontext,
//		OrdineAcqConsegnaBulk cons) throws ComponentException, PersistencyException {
//	Obbligazione_scadenzarioBulk scad = retrieveObbligazioneScadenzario(usercontext, cons);
//	ObbligazioneHome home = (ObbligazioneHome)getHome(usercontext, ObbligazioneBulk.class);
//	ObbligazioneBulk obbl = (ObbligazioneBulk)home.findByPrimaryKey(new ObbligazioneBulk(scad.getCd_cds(), scad.getEsercizio(), scad.getEsercizio_originale(), scad.getPg_obbligazione()));
//	return obbl;
//}
//
//@Override
//public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
//		throws ComponentException {
//	oggettobulk = super.inizializzaBulkPerRicerca( usercontext, oggettobulk );
//	return inizializzaOrdine(usercontext, oggettobulk, false);
//}
//
//@Override
//public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
//		throws ComponentException {
//	OggettoBulk oggetto = super.inizializzaBulkPerRicercaLibera(usercontext, oggettobulk);
//	return inizializzaOrdine(usercontext, oggetto, false);
//}
//protected Boolean isCdrUo(UserContext userContext) throws ComponentException, PersistencyException {
//	V_struttura_organizzativaHome homeStr =(V_struttura_organizzativaHome)getHome(userContext, V_struttura_organizzativaBulk.class );
//	SQLBuilder sqlStr =homeStr.createSQLBuilder();
//	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
//	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDS",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
//	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
//	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS, CNRUserContext.getCd_cdr(userContext));
//	sqlStr.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
//	sqlStr.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.FL_CDR_UO", SQLBuilder.EQUALS, "Y");
//
//	List listStr=homeStr.fetchAll(sqlStr);
//	if (listStr != null && listStr.size() == 1){
//		return true;
//	} else {
//		return false;
//	}
//}
//
//public SQLBuilder selectBeneServizioByClause(UserContext userContext, OrdineAcqRigaBulk riga, 
//		Bene_servizioBulk bene, 
//		CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
//	Bene_servizioHome beneHome = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
//	SQLBuilder sql = beneHome.selectByClause(userContext, compoundfindclause);
//	sql.addSQLClause("AND", "FL_VALIDO", SQLBuilder.EQUALS, Bene_servizioBulk.STATO_VALIDO);
//	
//	return sql;
//}
//
//private java.util.List recuperoListaCapitoliSelezionabili(UserContext userContext, OrdineAcqConsegnaBulk consegna)
//		throws ComponentException, PersistencyException, ApplicationException, IntrospectionException, RemoteException {
//	CategoriaGruppoInventComponentSession session = (CategoriaGruppoInventComponentSession)EJBCommonServices.createEJB(
//			"CNRDOCAMM00_EJB_CategoriaGruppoInventComponentSession",
//			CategoriaGruppoInventComponentSession.class);
//
//	java.util.List listaCapitoli = new ArrayList<>();
//	if (consegna.getOrdineAcqRiga().getCdBeneServizio() != null){
//		Bene_servizioBulk bene = recuperoBeneServizio(userContext, consegna.getOrdineAcqRiga().getCdBeneServizio());
//		if (bene != null) {
//			java.util.List titoliCapitoliCatGrp = session.findAssVoceFList(userContext, bene.getCategoria_gruppo());
//			if (titoliCapitoliCatGrp == null)
//				throw new it.cnr.jada.comp.ApplicationException("Alla categoria " + bene.getCategoria_gruppo().getCd_categoria_gruppo() + "\" non � stato attribuita l'associazione al capitolo di spesa");
//			for (java.util.Iterator k = titoliCapitoliCatGrp.iterator(); k.hasNext();) {
//				Categoria_gruppo_voceBulk assVoce = (Categoria_gruppo_voceBulk)k.next();
//				listaCapitoli.add(assVoce.getElemento_voce());
//			}
//		} else {
//			throw new it.cnr.jada.comp.ApplicationException("Bene/servizio non valido per il dettaglio " + ((consegna.getOrdineAcqRiga().getRiga() == null) ? "" : "\"" + consegna.getOrdineAcqRiga().getRiga() + "\"") + "!");
//		}
//	} else {
//		throw new it.cnr.jada.comp.ApplicationException("Valorizzare il bene/servizio per il dettaglio " + ((consegna.getOrdineAcqRiga().getRiga() == null) ? "" : "\"" + consegna.getOrdineAcqRiga().getRiga() + "\"") + "!");
//	}
//	return listaCapitoli;
//}
//
//public SQLBuilder selectDspMagazzinoByClause(UserContext userContext, OrdineAcqRigaBulk riga, 
//		MagazzinoBulk mag, 
//		CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
//	MagazzinoHome magHome = (MagazzinoHome)getHome(userContext, MagazzinoBulk.class);
//	SQLBuilder sql = recuperoMagazziniAbilitati(userContext, riga.getOrdineAcq(), magHome, compoundfindclause);
//	
//	return sql;
//}
//
//public SQLBuilder selectMagazzinoByClause(UserContext userContext, OrdineAcqConsegnaBulk cons, 
//		MagazzinoBulk mag, 
//		CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
//	MagazzinoHome magHome = (MagazzinoHome)getHome(userContext, MagazzinoBulk.class);
//	SQLBuilder sql = recuperoMagazziniAbilitati(userContext, cons.getOrdineAcqRiga().getOrdineAcq(), magHome, compoundfindclause);
//	
//	return sql;
//}
//
//private SQLBuilder recuperoMagazziniAbilitati(UserContext userContext, OrdineAcqBulk ord,
//		MagazzinoHome magHome, CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException {
//	SQLBuilder sql = magHome.selectByClause(userContext, compoundfindclause);
//	if (!isAbilitatoTuttiMagazzini(userContext, ord)){
//		sql.addTableToHeader("ABIL_UTENTE_UOP_OPER_MAG", "B");		
//		sql.addSQLJoin("MAGAZZINO.CD_CDS", "B.CD_CDS");
//		sql.addSQLJoin("MAGAZZINO.CD_MAGAZZINO", "B.CD_MAGAZZINO");
//		sql.addSQLClause("AND", "B.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_ORDINE);
//		sql.addSQLClause("AND", "B.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, ord.getCdUnitaOperativa());
//		sql.addSQLClause("AND", "B.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());
//	}
//	return sql;
//}
//
//public SQLBuilder selectVoceIvaByClause(UserContext userContext, OrdineAcqRigaBulk riga, 
//		Voce_ivaBulk voceIva, 
//		CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
//	Voce_ivaHome voceIvaHome = (Voce_ivaHome)getHome(userContext, Voce_ivaBulk.class);
//	SQLBuilder sql = voceIvaHome.selectByClause(userContext, compoundfindclause);
//	if (riga.getBeneServizio() == null){
//		throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare il Codice Iva! E' necessario prima selezionare il bene/servizio.");
//	}
//	Bene_servizioBulk bene = riga.getBeneServizio();
//	if (bene.getVoce_iva() == null || bene.getVoce_iva().getCd_voce_iva() == null){
//		Bene_servizioHome beneHome = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
//		try {
//			bene = (Bene_servizioBulk)beneHome.findByPrimaryKey(userContext, bene);
//		} catch (PersistencyException e) {
//			throw new ComponentException(e);
//		}
//	}
//	
//	sql.addSQLClause("AND", "CD_VOCE_IVA", SQLBuilder.EQUALS, bene.getCd_voce_iva());
//	
//	return sql;
//}
//
//public SQLBuilder selectLuogoConsegnaMagByClause(UserContext userContext, OrdineAcqConsegnaBulk cons, 
//		LuogoConsegnaMagBulk luogo, 
//		CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
//	LuogoConsegnaMagHome luogoHome = (LuogoConsegnaMagHome)getHome(userContext, LuogoConsegnaMagBulk.class);
//	SQLBuilder sql = luogoHome.selectByClause(userContext, compoundfindclause);
//	if (cons.getMagazzino() == null){
//		throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare il Codice Iva! E' necessario prima selezionare il bene/servizio.");
//	}
//	MagazzinoBulk mag = cons.getMagazzino();
//	if (mag.getLuogoConsegnaMag() == null || mag.getLuogoConsegnaMag().getCdLuogoConsegna() == null){
//		MagazzinoHome magHome = (MagazzinoHome)getHome(userContext, MagazzinoBulk.class);
//		try {
//			mag = (MagazzinoBulk)magHome.findByPrimaryKey(userContext, mag);
//		} catch (PersistencyException e) {
//			throw new ComponentException(e);
//		}
//	}
//	
//	sql.addSQLClause("AND", "CD_LUOGO_CONSEGNA", SQLBuilder.EQUALS, mag.getCdLuogoConsegna());
//	
//	return sql;
//}
//
//public SQLBuilder selectTipoOrdineByClause(UserContext userContext, OrdineAcqBulk ord, 
//		TipoOrdineBulk tipo, 
//		CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
//	TipoOrdineHome tipoHome = (TipoOrdineHome)getHome(userContext, TipoOrdineBulk.class);
//	SQLBuilder sql = tipoHome.selectByClause(userContext, compoundfindclause);
//	return sql;
//}
//
//protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException
//{
//	SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
//	AbilUtenteUopOperHome abilHome = (AbilUtenteUopOperHome) getHomeCache(userContext).getHome(AbilUtenteUopOperBulk.class);
//	OrdineAcqBulk ordineAcqBulk = (OrdineAcqBulk)bulk;
//	SQLBuilder sqlExists = null;
//	sqlExists = abilHome.createSQLBuilder();
//	sqlExists.addSQLJoin("ORDINE_ACQ.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
//	if (!ordineAcqBulk.getIsForFirma()){
//		sqlExists.openParenthesis("AND");
//		sqlExists.addSQLClause("OR", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_ORDINE);
//		sqlExists.addSQLClause("OR", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_APPROVAZIONE_ORDINE);
//		sqlExists.closeParenthesis();
//	} else {
//		sqlExists.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_FIRMA_ORDINE);
//		sql.openParenthesis("AND");
//		sql.addSQLClause("OR", "ORDINE_ACQ.STATO", SQLBuilder.EQUALS, OrdineAcqBulk.STATO_ALLA_FIRMA);
//		sql.addSQLClause("OR", "ORDINE_ACQ.STATO", SQLBuilder.EQUALS, OrdineAcqBulk.STATO_DEFINITIVO);
//		sql.addSQLClause("OR", "ORDINE_ACQ.STATO", SQLBuilder.EQUALS, OrdineAcqBulk.STATO_INVIATO_ORDINE);
//		sql.closeParenthesis();
//	}
//	sqlExists.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());
//
//	sql.addSQLExistsClause("AND", sqlExists);
//	sql.addSQLClause("AND", "ORDINE_ACQ.STATO", SQLBuilder.NOT_EQUALS, OrdineAcqBulk.STATO_ANNULLATO);
//	sql.addOrderBy("cd_cds");
//	sql.addOrderBy("cd_unita_operativa");
//	sql.addOrderBy("esercizio");
//	sql.addOrderBy("cd_numeratore");
//	sql.addOrderBy("numero");
//	return sql;
//}
//
private OggettoBulk inizializzaEvasioneOrdine(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	EvasioneOrdineBulk bulk = (EvasioneOrdineBulk)oggettobulk;
	try {
		EvasioneOrdineHome home = (EvasioneOrdineHome) getHomeCache(usercontext).getHome(EvasioneOrdineBulk.class);
		bulk.setCdCds( ((CNRUserContext) usercontext).getCd_cds());
		UnitaOperativaOrdHome uopHome = (UnitaOperativaOrdHome)getHome(usercontext, UnitaOperativaOrdBulk.class);
		SQLBuilder sql = home.selectUnitaOperativaAbilitataByClause(usercontext, bulk, uopHome, new UnitaOperativaOrdBulk(), new CompoundFindClause());
		List listUop=uopHome.fetchAll(sql);
		if (listUop != null && (listUop.size() == 1 || isPresenteUnaUop(listUop))){
			bulk.setUnitaOperativaAbilitata((UnitaOperativaOrdBulk)listUop.get(0));
		}
		assegnaNumeratoreMag(usercontext, bulk, home);
	} catch (PersistencyException e){
		throw new ComponentException(e);
	}
	return bulk;
}

private Boolean isPresenteUnaUop(List listUop) throws ComponentException {
	UnitaOperativaOrdKey key = null;
	for (Object oggettoBulk : listUop){
		UnitaOperativaOrdBulk uop = (UnitaOperativaOrdBulk)oggettoBulk;
		if (key ==null){
			key = (UnitaOperativaOrdKey)uop.getKey();
		} else {
			if (!key.equals((UnitaOperativaOrdKey)uop.getKey())){
				return false;
			}
		}
	}
	if (key != null){
		return true;
	}
	return false;
}

//private void impostaDatiDivisaCambioDefault(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException {
//	ordine.setDivisa(getEuro(usercontext));
//	ordine.setCambio(BigDecimal.ONE);
//}
//
private void assegnaNumeratoreMag(UserContext usercontext, EvasioneOrdineBulk bulk, EvasioneOrdineHome home)
		throws PersistencyException, ComponentException {
	if (bulk.getCdNumeratoreMag() == null && bulk.getCdUnitaOperativa() != null){
			NumerazioneMagHome numerazioneHome = (NumerazioneMagHome)getHome(usercontext, NumerazioneMagBulk.class);
			SQLBuilder sql = home.selectNumerazioneMagByClause(usercontext, bulk, numerazioneHome, new NumerazioneMagBulk(), new CompoundFindClause());
			List listNum=numerazioneHome.fetchAll(sql);
			if (listNum != null && listNum.size() == 1){
				NumerazioneMagBulk num = (NumerazioneMagBulk)listNum.get(0);
				MagazzinoBulk mag  = ((MagazzinoBulk)((MagazzinoHome)getHomeCache(usercontext).getHome(MagazzinoBulk.class)).findByPrimaryKey(usercontext, num.getMagazzino()));
				num.setMagazzino(mag);
				bulk.setNumerazioneMag(num);
			}
	}
}
//public Boolean isUtenteAbilitatoOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException{
//	return isUtenteAbilitato(usercontext, ordine, TipoOperazioneOrdBulk.OPERAZIONE_ORDINE);
//}
//
//public Boolean isUtenteAbilitatoValidazioneOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException{
//	return isUtenteAbilitato(usercontext, ordine, TipoOperazioneOrdBulk.OPERAZIONE_APPROVAZIONE_ORDINE);
//}
//
//private Boolean isUtenteAbilitato(UserContext usercontext, OrdineAcqBulk ordine, String tipoOperazione) throws ComponentException {
//	if (ordine.getCdUnitaOperativa() != null){
//		AbilUtenteUopOperBulk abil = recuperoAbilUtenteUo(usercontext, ordine, tipoOperazione);
//		if (abil != null){
//			return true;
//		}
//		return false;
//	}
//	return true;
//}
//
//public void completaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws PersistencyException, ComponentException{
//	OrdineAcqHome home = (OrdineAcqHome) getHomeCache(userContext).getHome(OrdineAcqBulk.class);
//	assegnaNumeratoreOrd(userContext, ordine, home);
//	UnitaOperativaOrdHome uopHome = (UnitaOperativaOrdHome)getHome(userContext, UnitaOperativaOrdBulk.class);
//}
//
//public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
//		throws it.cnr.jada.comp.ComponentException {
//	OrdineAcqBulk ordine= (OrdineAcqBulk)bulk;
//	validaOrdine(userContext, ordine);
//	controlliCambioStato(userContext,ordine);
//	calcolaImportoOrdine(userContext, ordine);
//    manageDeletedElements(userContext, ordine, status);
//	aggiornaObbligazioni(userContext,ordine,status);
//	verificaCoperturaContratto( userContext,ordine);
//	return (OrdineAcqBulk)super.modificaConBulk(userContext, bulk);
//}
//
//private void controlliCambioStato(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException{
//	OrdineAcqBulk ordineDB;
//	try {
//		ordineDB = (OrdineAcqBulk)getTempHome(usercontext, OrdineAcqBulk.class).findByPrimaryKey(
//				new OrdineAcqBulk(
//						ordine.getCdCds(),
//						ordine.getCdUnitaOperativa(),
//						ordine.getEsercizio(),
//						ordine.getCdNumeratore(),
//						ordine.getNumero()
//		                ));
//		if (ordineDB != null && !ordineDB.getStato().equals(ordine.getStato())){
//			if (ordineDB.isOrdineInserito()){
//				if (!ordine.isOrdineInviatoApprovazione()){
//					throw new it.cnr.jada.comp.ApplicationException("Non � possibile indicare uno stato diverso da 'in approvazione'");
//				}
//			} else if (ordineDB.isOrdineDefinitivo()){
//				if (!ordine.isOrdineInviatoFornitore()){
//					throw new it.cnr.jada.comp.ApplicationException("Non � possibile indicare uno stato diverso da inviato al fornitore");
//				}
//			} else if (ordineDB.isOrdineAllaFirma()){
//				if (!(ordine.isStatoDefinitivo() || ordine.isStatoInApprovazione())){
//					throw new it.cnr.jada.comp.ApplicationException("Non � possibile indicare uno stato diverso da definito o in approvazione");
//				}
//			} else if (ordineDB.isOrdineInviatoApprovazione()){
//				AbilUtenteUopOperHome abilHome = (AbilUtenteUopOperHome) getHomeCache(usercontext).getHome(AbilUtenteUopOperBulk.class);
//				if (!abilHome.isUtenteAbilitato(usercontext, TipoOperazioneOrdBulk.OPERAZIONE_APPROVAZIONE_ORDINE, ordine.getCdUnitaOperativa())){
//					throw new it.cnr.jada.comp.ApplicationException("Utente non abilitato ad operare su ordini in approvazione");
//				}
//				if (!(ordine.isStatoAllaFirma() || ordine.isStatoInserito())){
//					throw new it.cnr.jada.comp.ApplicationException("Non � possibile indicare uno stato diverso da inserito o alla firma");
//				}
//			} else if (ordineDB.isOrdineInviatoFornitore()){
//				throw new it.cnr.jada.comp.ApplicationException("Non � possibile cambiare lo stato di un ordine inviato al fornitore");
//			}
//		}
//	} catch (PersistencyException e) {
//		throw new ComponentException(e);
//	} catch (IntrospectionException e) {
//		throw new ComponentException(e);
//	}
//}
//
//@Override
//public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
//	return modificaConBulk(usercontext, oggettobulk, null);
//}
//
//
//private Boolean isUoImpegnoDaUopDestinazione(UserContext userContext) throws ComponentException {
//
//	
//	String uoDestinazione = null;
//	try {
//		 uoDestinazione = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, CNRUserContext.getEsercizio(userContext), "*", Configurazione_cnrBulk.PK_ORDINI, Configurazione_cnrBulk.SK_ORDINE_IMPEGNO_UO_DESTINAZIONE);
//		if (uoDestinazione != null && uoDestinazione.equals("Y")){
//			return true;
//		}
//		
//	} catch (javax.ejb.EJBException e) {
//		handleException(e);
//	} catch (java.rmi.RemoteException e) {
//		handleException(e);
//	}
//
//	return false;
//}
//
//
//
//private DivisaBulk getEuro(UserContext userContext) throws ComponentException {
//
//	String cd_euro = null;
//	try {
//		cd_euro = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, new Integer(0), "*", "CD_DIVISA", "EURO");
//		if (cd_euro == null)
//			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire un ordine, immettere tale valore.");
//	} catch (javax.ejb.EJBException e) {
//		handleException(e);
//	} catch (java.rmi.RemoteException e) {
//		handleException(e);
//	}
//
//	DivisaBulk valuta = null;
//	
//	try {
//		java.util.List divise = getHome(userContext, DivisaBulk.class).find(new it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk(cd_euro));
//		if (divise == null || divise.isEmpty())
//			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire un ordine, immettere tale valore.");
//		valuta = (DivisaBulk)divise.get(0);
//		if (valuta == null)
//			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire un ordine, immettere tale valore.");
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		handleException(e);
//	}
//	return valuta;
//}
//
///**
// * Pre:  Ricerca CIG
// * Post: Il CIG pu� essere collegato ad un contratto solo se vengono rispettate le seguenti regole:				
//		CD_TERZO_RUP del CIG � il medesimo del contratto che si sta inserendo quindi : 
//		CIG. CD_TERZO_RUP = CONTRATTO. CD_TERZO_RESP
//		Il CIG non deve risultare associato ad altri contratti.
// */
//public SQLBuilder selectCigByClause (UserContext userContext, OrdineAcqBulk ordine, CigBulk cig, CompoundFindClause clause)	throws ComponentException, PersistencyException
//{
//	if (clause == null) 
//	  clause = cig.buildFindClauses(null);
//	SQLBuilder sql = getHome(userContext, cig).createSQLBuilder();
//	if(ordine.getResponsabileProcPers() == null || ordine.getResponsabileProcPers().getCd_terzo() == null)
//	   throw new ApplicationException("Per effettuare la ricerca valorizzare il campo Responsabile!");  
//	sql.addSQLClause(FindClause.AND, "CD_TERZO_RUP", SQLBuilder.EQUALS, ordine.getResponsabileProcPers().getCd_terzo());
//	sql.addClause(FindClause.AND, "FL_VALIDO", SQLBuilder.EQUALS, Boolean.TRUE);
//	if (clause != null) 
//	  sql.addClause(clause);
//	return sql;
//}
//
///**
// * Pre:  Ricerca Figura giuridica interna
// * Post: Limitazione ai terzi di tipo Unit� Organizzativa
// */
//public SQLBuilder selectTerzoCdrByClause (UserContext userContext, OggettoBulk bulk, TerzoBulk terzo,CompoundFindClause clause)	throws ComponentException, PersistencyException
//{
//	if (clause == null) 
//	  clause = terzo.buildFindClauses(null);
//	SQLBuilder sql = getHome(userContext, terzo).createSQLBuilder();
//	sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.ISNOTNULL, null);
//	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
//	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
//	  sql.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
//	}		
//	if (clause != null) 
//	  sql.addClause(clause);
//	return sql;
//}	
//public SQLBuilder selectFornitoreByClause(UserContext userContext,  OggettoBulk bulk, TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException {
//	
//	TerzoHome home = (TerzoHome)getHome(userContext, TerzoBulk.class, "V_TERZO_CF_PI");
//	SQLBuilder sql = home.createSQLBuilder();
//	sql.addSQLClause("AND","DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
//	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA", SQLBuilder.ISNULL, null);
//	sql.addClause(clauses); 
//	return sql;
//}
///**
// * Pre:  Ricerca Tipo Provvedimento
// * Post: Limitazione ai tipi non annullati
// */
//public SQLBuilder selectProcedureAmministrativeByClause (UserContext userContext, OggettoBulk bulk, Procedure_amministrativeBulk procedura_amministrativa,CompoundFindClause clause)	throws ComponentException, PersistencyException
//{
//	if (clause == null) 
//	  clause = procedura_amministrativa.buildFindClauses(null);
//	SQLBuilder sql = getHome(userContext, procedura_amministrativa).createSQLBuilder();
//	sql.openParenthesis("AND");
//	sql.addClause("OR", "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_FORNITURA_SERVIZI);
//	sql.addClause("OR", "ti_proc_amm", SQLBuilder.EQUALS, Procedure_amministrativeBulk.TIPO_GENERICA);
//	sql.closeParenthesis();
//	sql.addClause("AND", "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
//	if (clause != null) 
//	  sql.addClause(clause);
//	return sql;
//}	
//public ImportoOrdine calcoloImportoOrdine(ParametriCalcoloImportoOrdine parametri) throws ApplicationException{
//	BigDecimal prezzo = Utility.nvl(parametri.getPrezzoRet(), parametri.getPrezzo());
//	BigDecimal cambio = Utility.nvl(parametri.getCambioRet(), parametri.getCambio());
//	if (parametri.getDivisa() == null || parametri.getDivisaRisultato() == null || 
//			parametri.getDivisa().getCd_divisa() == null || parametri.getDivisaRisultato().getCd_divisa() == null){
//		throw new it.cnr.jada.comp.ApplicationException("E' necessario indicare le divise.");
//	}
//	if (!parametri.getDivisa().getCd_divisa().equals(parametri.getDivisaRisultato().getCd_divisa())){
//		if (parametri.getDivisaRisultato().getFl_calcola_con_diviso().booleanValue())
//			prezzo = Utility.divide(prezzo, cambio);
//		else
//			prezzo= prezzo.multiply(cambio).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//
//	}
//	BigDecimal sconto1 = Utility.nvl(Utility.nvl(parametri.getSconto1Ret(), parametri.getSconto1()));
//	BigDecimal sconto2 = Utility.nvl(Utility.nvl(parametri.getSconto2Ret(), parametri.getSconto2()));
//	BigDecimal sconto3 = Utility.nvl(Utility.nvl(parametri.getSconto3Ret(), parametri.getSconto3()));
//	BigDecimal prezzoScontato = prezzo.
//									multiply(BigDecimal.ONE.subtract(sconto1.divide(Utility.CENTO))).
//									multiply(BigDecimal.ONE.subtract(sconto2.divide(Utility.CENTO))).
//									multiply(BigDecimal.ONE.subtract(sconto3.divide(Utility.CENTO)));
//	BigDecimal imponibile = prezzoScontato.multiply(parametri.getQtaOrd());
//	Voce_ivaBulk voceIva = null;
//	if (parametri.getVoceIvaRet() != null && parametri.getVoceIvaRet().getPercentuale() != null){
//		voceIva = parametri.getVoceIvaRet();
//	} else {
//		voceIva = parametri.getVoceIva();
//	}
//	BigDecimal importoIva = Utility.round2Decimali((Utility.divide(imponibile, Utility.CENTO)).multiply(voceIva.getPercentuale())); 
//	BigDecimal ivaNonDetraibile = Utility.round2Decimali(importoIva.multiply((Utility.CENTO.subtract(voceIva.getPercentuale_detraibilita()))));
//	BigDecimal ivaPerCalcoloProrata = importoIva.subtract(ivaNonDetraibile);
//	BigDecimal ivaDetraibile = Utility.round2Decimali(ivaPerCalcoloProrata.multiply(Utility.nvl(parametri.getPercProrata())));
//	ivaNonDetraibile = ivaNonDetraibile.add((ivaPerCalcoloProrata.subtract(ivaDetraibile)));
//	
//	if (ivaDetraibile.compareTo(BigDecimal.ZERO) == 0 || ivaNonDetraibile.compareTo(BigDecimal.ZERO) > 0){
//		ivaNonDetraibile = ivaNonDetraibile.add(Utility.nvl(parametri.getArrAliIva()));
//	}else {
//		ivaDetraibile = ivaDetraibile.add(Utility.nvl(parametri.getArrAliIva()));
//	}
//	importoIva = importoIva.add(ivaDetraibile);
//	ImportoOrdine importoOrdine = new ImportoOrdine();
//	importoOrdine.setImponibile(Utility.round2Decimali(imponibile));
//	importoOrdine.setImportoIva(Utility.round2Decimali(importoIva));
//	importoOrdine.setImportoIvaInd(Utility.round2Decimali(ivaNonDetraibile));
//	importoOrdine.setImportoIvaDetraibile(Utility.round2Decimali(ivaDetraibile));
//	importoOrdine.setArrAliIva(BigDecimal.ZERO);
//	return importoOrdine;
//}
//
//public OrdineAcqBulk contabilizzaDettagliSelezionati(
//		UserContext context,
//		OrdineAcqBulk ordine,
//		java.util.Collection dettagliSelezionati,
//		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneSelezionata)
//		throws ComponentException {
//
//		if (obbligazioneSelezionata != null && dettagliSelezionati != null) {
//			if (!dettagliSelezionati.isEmpty()) {
//				for (java.util.Iterator i = dettagliSelezionati.iterator(); i.hasNext();) {
//					OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)i.next();
//
//	                validaScadenze(ordine, obbligazioneSelezionata);
//	                
//	                riga.setDspObbligazioneScadenzario(obbligazioneSelezionata);
//					riga.setToBeUpdated();
//					for (Object bulk : riga.getRigheConsegnaColl()){
//						OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk)bulk;
//						if (cons.getObbligazioneScadenzario() == null || cons.getEsercizioOrigObbl() == null || cons.getObbligazioneScadenzario().equalsByPrimaryKey(riga.getDspObbligazioneScadenzario())){
//							ordine.addToOrdineObbligazioniHash(obbligazioneSelezionata, cons);
//						} else {
//							throw new it.cnr.jada.DetailedRuntimeException("L'impegno sulla riga di consegna � diverso dall'impegno indicato sulla riga d'ordine.");
//						}
//					}
//				}
//				ordine.addToOrdineAss_totaliMap(obbligazioneSelezionata, calcolaTotalePer(
//											(Vector)ordine.getOrdineObbligazioniHash().get(obbligazioneSelezionata),
//											false));
//			} else {
//				ordine.addToOrdineObbligazioniHash(obbligazioneSelezionata, null);
//			}
//			try {
//				ObbligazioneAbstractComponentSession session = (ObbligazioneAbstractComponentSession)EJBCommonServices.createEJB(
//																"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
//																ObbligazioneAbstractComponentSession.class);
//				session.lockScadenza(context, obbligazioneSelezionata);
//			} catch (Throwable t) {
//				throw handleException(ordine, t);
//			}
//		}
//		return ordine;
//	}
//
//public OrdineAcqBulk contabilizzaConsegneSelezionate(
//		UserContext context,
//		OrdineAcqBulk ordine,
//		java.util.Collection dettagliSelezionati,
//		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneSelezionata)
//				throws ComponentException {
//
//	if (obbligazioneSelezionata != null && dettagliSelezionati != null) {
//		if (!dettagliSelezionati.isEmpty()) {
//			for (java.util.Iterator i = dettagliSelezionati.iterator(); i.hasNext();) {
//				OrdineAcqConsegnaBulk cons = (OrdineAcqConsegnaBulk)i.next();
//
//				validaScadenze(ordine, obbligazioneSelezionata);
//
//				cons.setObbligazioneScadenzario(obbligazioneSelezionata);
//				cons.setToBeUpdated();
//				if (cons.getObbligazioneScadenzario() != null && cons.getEsercizioOrigObbl() != null){
//					ordine.addToOrdineObbligazioniHash(obbligazioneSelezionata, cons);
//				}
//				ordine.addToOrdineAss_totaliMap(obbligazioneSelezionata, calcolaTotalePer(
//						(Vector)ordine.getOrdineObbligazioniHash().get(obbligazioneSelezionata),
//						false));
//			}
//			try {
//				ObbligazioneAbstractComponentSession session = (ObbligazioneAbstractComponentSession)EJBCommonServices.createEJB(
//						"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
//						ObbligazioneAbstractComponentSession.class);
//				session.lockScadenza(context, obbligazioneSelezionata);
//			} catch (Throwable t) {
//				throw handleException(ordine, t);
//			}
//		}
//	}
//	return ordine;
//}
//
//private java.math.BigDecimal calcolaTotalePer(
//		java.util.List selectedModels,
//		boolean escludiIVA)
//		throws it.cnr.jada.comp.ApplicationException {
//
//		java.math.BigDecimal importo = new java.math.BigDecimal(0);
//		boolean escludiIVAInt=false;
//		boolean escludiIVAOld=escludiIVA;	
//		if (selectedModels != null) {
//			for (java.util.Iterator i = selectedModels.iterator(); i.hasNext();) {
//				escludiIVA=escludiIVAOld;	
//				OrdineAcqConsegnaBulk riga = (OrdineAcqConsegnaBulk)i.next();
//				importo = importo.add(
//					(escludiIVA	) ?
//						riga.getImImponibile() :
//						riga.getImTotaleConsegna());
//			}
//		}
//
//		importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//		return importo;
//	}
//
//private void validaScadenze(OrdineAcqBulk ordine, Obbligazione_scadenzarioBulk newScad) throws ComponentException{
//	Iterator it;
//	
//	Vector scadCanc = ordine.getDocumentiContabiliCancellati();
//	if (scadCanc != null) {
//		it = scadCanc.iterator();
//	 
//		while(it.hasNext()) {
//			Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) it.next();
//			if(scad.getObbligazione() instanceof ObbligazioneResBulk){
//				if (scad.getObbligazione().equalsByPrimaryKey(newScad.getObbligazione()) && ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica()!=null
//					&& ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica().getPg_modifica()!=null) {
//					throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'impegno residuo "+scad.getPg_obbligazione()+" poich� � stata effettuata una modifica in questo ordine!");									
//				}
//			}
//		}
//	}
//		
//    ObbligazioniTable obbligazioniHash= ordine.getObbligazioniHash();
//    if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {
//
//    	for (java.util.Enumeration e= obbligazioniHash.keys(); e.hasMoreElements();) {
//    		Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) e.nextElement();
//    		if(scad.getObbligazione() instanceof ObbligazioneResBulk){
//    			if (scad.getObbligazione().equalsByPrimaryKey(newScad.getObbligazione()) && ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica()!=null
//					&& ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica().getPg_modifica()!=null) {
//    				throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'impegno residuo "+scad.getPg_obbligazione()+" poich� � stata effettuata una modifica in questo ordine!");									
//    			}
//    		}
//    	}
//    }
//}
//private void aggiornaObbligazioni(
//		UserContext userContext,
//		OrdineAcqBulk ordine,
//		OptionRequestParameter status)
//		throws ComponentException {
//
//		if (ordine != null) {
//			ObbligazioniTable obbligazioniHash = ordine.getOrdineObbligazioniHash();
//			if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {
//				Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome)getHome(userContext, Obbligazione_scadenzarioBulk.class);
//
//				for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable)obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements();)
//					aggiornaSaldi(
//						userContext, 
//						ordine, 
//						(IDocumentoContabileBulk)e.nextElement(),
//						status);
//					
//				PrimaryKeyHashtable obblTemporanee = getDocumentiContabiliTemporanei(userContext, ((ObbligazioniTable)obbligazioniHash.clone()).keys());
//				for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements();) {
//					ObbligazioneBulk obblT = (ObbligazioneBulk)e.nextElement();
//
//					aggiornaSaldi(userContext, ordine, obblT, status);
//					
//					aggiornaObbligazioniTemporanee(userContext, obblT);
//					obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable(obblTemporanee);
//					for (Iterator i = ((Vector)obblTemporanee.get(obblT)).iterator(); i.hasNext();) 
//						((ObbligazioneBulk)i.next()).setPg_obbligazione(obblT.getPg_obbligazione());
//				}
//				ObbligazioniTable newObbligazioniHash = new ObbligazioniTable(obbligazioniHash);
//
//				ordine.setOrdineObbligazioniHash(newObbligazioniHash);
//				for (java.util.Enumeration e = ((ObbligazioniTable)newObbligazioniHash.clone()).keys(); e.hasMoreElements();) {
//					Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
//					java.math.BigDecimal im_ass = null;
//					im_ass = calcolaTotaleObbligazione(userContext, scadenza, ordine);
//					scadenza.setFlAssociataOrdine(true);
//					scadenza.setIm_associato_doc_amm(im_ass);
//					updateImportoAssociatoDocAmm(userContext, scadenza);
//				}
//			}
//		}
//	}
//private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(
//		UserContext userContext, 
//		java.util.Enumeration scadenze) throws ComponentException {
//
//		it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliNonTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//		if (scadenze != null)
//			while (scadenze.hasMoreElements()) {
//				IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk)scadenze.nextElement();
//				if (!scadenza.getFather().isTemporaneo()) {
//					if (!documentiContabiliNonTemporanei.containsKey(scadenza.getFather())) {
//						Vector allInstances = new java.util.Vector();
//						allInstances.addElement(scadenza.getFather());
//						documentiContabiliNonTemporanei.put(scadenza.getFather(), allInstances);
//					} else {
//						((Vector)documentiContabiliNonTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
//					}
//				}
//			}
//		return documentiContabiliNonTemporanei;
//	}
//private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliTemporanei(UserContext userContext, java.util.Enumeration scadenze) throws ComponentException {
//
//	it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//	if (scadenze != null)
//		while (scadenze.hasMoreElements()) {
//			IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk)scadenze.nextElement();
//			if (scadenza.getFather().isTemporaneo()) {
//				if (!documentiContabiliTemporanei.containsKey(scadenza.getFather())) {
//					Vector allInstances = new java.util.Vector();
//					allInstances.addElement(scadenza.getFather());
//					documentiContabiliTemporanei.put(scadenza.getFather(), allInstances);
//				} else {
//					((Vector)documentiContabiliTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
//				}
//			}
//		}
//	return documentiContabiliTemporanei;
//}
//private void aggiornaSaldi(
//		it.cnr.jada.UserContext uc,
//		OrdineAcqBulk ordine,
//		IDocumentoContabileBulk docCont,
//		OptionRequestParameter status) 
//	 	throws ComponentException{
//
//		try {
//			if (docCont != null && ordine != null && ordine.getDefferredSaldi() != null) {
//				IDocumentoContabileBulk key = ordine.getDefferredSaldoFor(docCont);
//				if (key != null) {
//					java.util.Map values = (java.util.Map)ordine.getDefferredSaldi().get(key);
//					String jndiName = null;
//					Class clazz = null;
//					DocumentoContabileComponentSession session = null;
//					if (docCont instanceof ObbligazioneBulk) {
//						jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
//						clazz = ObbligazioneAbstractComponentSession.class;
//						session = 
//							(ObbligazioneAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//							jndiName,clazz);
//					}
//					if (session != null) {
//						session.aggiornaSaldiInDifferita(uc, key, values, status);
//						ordine.getDefferredSaldi().remove(key);
//					}
//				}
//			}
//		} catch (javax.ejb.EJBException e) {
//			throw handleException(ordine, e);
//		} catch (java.rmi.RemoteException e) {
//			throw handleException(ordine, e);
//		}
//	}
//
//private void aggiornaObbligazioniTemporanee(UserContext userContext,ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {
//
//	try {
//		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
//		Long pg = null;
//		pg = numHome.getNextPg(userContext,
//						obbligazioneTemporanea.getEsercizio(), 
//						obbligazioneTemporanea.getCd_cds(), 
//						obbligazioneTemporanea.getCd_tipo_documento_cont(), 
//						obbligazioneTemporanea.getUser());
//		ObbligazioneHome home = (ObbligazioneHome)getHome(userContext, obbligazioneTemporanea);
//		home.confirmObbligazioneTemporanea(userContext, obbligazioneTemporanea, pg);
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(obbligazioneTemporanea, e);
//	} catch (it.cnr.jada.persistency.IntrospectionException e) {
//		throw handleException(obbligazioneTemporanea, e);
//	}	
//}
//
//private java.math.BigDecimal calcolaTotaleObbligazione(
//		it.cnr.jada.UserContext userContext,
//		Obbligazione_scadenzarioBulk scadenza,
//		OrdineAcqBulk ordine)
//				throws it.cnr.jada.comp.ComponentException {
//
//	ObbligazioniTable obbligazioniHash = ordine.getOrdineObbligazioniHash();
//	Vector dettagli = (Vector)obbligazioniHash.get(scadenza);
//	java.math.BigDecimal impTotaleDettagli = calcolaTotalePer(dettagli, false);
//	return impTotaleDettagli;
//}
//public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
//		it.cnr.jada.UserContext userContext, 
//		IScadenzaDocumentoContabileBulk scadenza)
//		throws it.cnr.jada.comp.ComponentException {
//
//		try {
//			((IScadenzaDocumentoContabileHome)getHome(userContext, scadenza.getClass())).aggiornaImportoAssociatoADocAmm(userContext,scadenza);
//		} catch (it.cnr.jada.persistency.PersistencyException exc) {
//			throw handleException((OggettoBulk)scadenza, exc);
//		} catch (it.cnr.jada.bulk.BusyResourceException exc) {
//			throw handleException((OggettoBulk)scadenza, exc);
//		} catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
//			throw handleException((OggettoBulk)scadenza, exc);
//		}
//
//		return scadenza;
//	}
//private void rebuildObbligazioni(UserContext aUC, OrdineAcqBulk ordine) throws ComponentException {
//
//	if (ordine == null) return;
//	
//	BulkList righe = ordine.getRigheOrdineColl();
//	if (righe != null) {
//
//		for (Iterator i = righe.iterator(); i.hasNext(); ) {
//			OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)i.next();
//        	for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//        		OggettoBulk consbulk= (OggettoBulk) c.next();
//        		OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//
//        		Obbligazione_scadenzarioBulk scadenza = cons.getObbligazioneScadenzario();
//
//        		if (cons.getObbligazioneScadenzario() != null) {
//        			if (ordine.getOrdineObbligazioniHash() == null ||
//        					ordine.getOrdineObbligazioniHash().getKey(scadenza) == null) {
//        				scadenza = caricaScadenzaObbligazionePer(aUC, scadenza);
//        			}
//        			ordine.addToOrdineObbligazioniHash(scadenza, cons);
//        		}
//        	}
//		}
//	}
//	try {
//		getHomeCache(aUC).fetchAll(aUC);
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(ordine, e);
//	}
//}
//private Obbligazione_scadenzarioBulk caricaScadenzaObbligazionePer(
//		UserContext context,
//		Obbligazione_scadenzarioBulk scadenza)
//		throws ComponentException {
//
//		if (scadenza != null) {
//			try {
//				it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession)
//																			it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//																						"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
//																						it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class);
//				ObbligazioneBulk obbligazione = (ObbligazioneBulk)h.inizializzaBulkPerModifica(context, scadenza.getObbligazione());
//				BulkList scadenze = obbligazione.getObbligazione_scadenzarioColl();
//				scadenza = (Obbligazione_scadenzarioBulk)scadenze.get(scadenze.indexOfByPrimaryKey(scadenza));
//			} catch (java.rmi.RemoteException e) {
//				throw handleException(scadenza, e);
//			} catch (javax.ejb.EJBException e) {
//				throw handleException(scadenza, e);
//			}
//			return scadenza;
//		}
//		return null;
//	}
//private void manageDocumentiContabiliCancellati(
//		UserContext userContext, 
//		OrdineAcqBulk ordine,
//		OptionRequestParameter status)
//		throws ComponentException {
//
//		if (ordine != null) {
//			if (ordine.getDocumentiContabiliCancellati() != null &&
//				!ordine.getDocumentiContabiliCancellati().isEmpty()) {
//
//					PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
//																			userContext,
//																			ordine.getOrdineObbligazioniHash().keys());
//					Vector scadenzeConfermate = new Vector();
//					java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
//					while (e.hasMoreElements()) {
//						OggettoBulk obj = (OggettoBulk)e.nextElement();
//						if (obj instanceof ObbligazioneBulk)
//							scadenzeConfermate.add(obj);
//					}
//					aggiornaObbligazioniSuCancellazione(
//						userContext,
//						ordine,
//						ordine.getDocumentiContabiliCancellati().elements(),
//						scadenzeConfermate,
//						status);
//			}
//		}
//	}
//private void aggiornaObbligazioniSuCancellazione(
//		UserContext userContext,
//		OrdineAcqBulk ordine,
//		java.util.Enumeration scadenzeDaCancellare,
//		java.util.Collection scadenzeConfermate,
//		OptionRequestParameter status)
//		throws ComponentException {
//
//		if (scadenzeDaCancellare != null) {
//
//			it.cnr.jada.bulk.PrimaryKeyHashtable obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//			for (java.util.Enumeration e = scadenzeDaCancellare; e.hasMoreElements();) {
//				OggettoBulk oggettoBulk = (OggettoBulk)e.nextElement();
//				if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
//					Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)oggettoBulk;
//					if (scadenza.getObbligazione().isTemporaneo()) {
//						if (!obblTemporanee.containsKey(scadenza.getObbligazione())) {
//							Vector allInstances = new java.util.Vector();
//							allInstances.addElement(scadenza);
//							obblTemporanee.put(scadenza.getObbligazione(), allInstances);
//						} else {
//							((Vector)obblTemporanee.get(scadenza.getObbligazione())).add(scadenza);
//						}
//					} else if (!ordine.isToBeCreated() && OggettoBulk.NORMAL == scadenza.getCrudStatus()) {
//						PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, ordine.getObbligazioniHash().keys());
//						if (!obbligs.containsKey(scadenza.getObbligazione()))
//							aggiornaSaldi(
//								userContext, 
//								ordine, 
//								scadenza.getObbligazione(),
//								status);
//						scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
//						scadenza.setFlAssociataOrdine(false);
//						updateImportoAssociatoDocAmm(userContext, scadenza);
//					}
//	            	/**
//	            	 * Devo aggiornare i Saldi per quelle scadenze modificate e riportate
//	            	 * ma poi scollegate dal documento 
//	            	 * Marco Spasiano 05/05/2006
//	            	 */
//	                aggiornaSaldi(userContext, ordine, scadenza.getObbligazione(), status);
//				}
//			}
//			for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements();) {
//				ObbligazioneBulk obblT = (ObbligazioneBulk)e.nextElement();
//
//				PrimaryKeyHashtable obbligs = getDocumentiContabiliTemporanei(userContext, ordine.getObbligazioniHash().keys());
//				if (!obbligs.containsKey(obblT))
//					aggiornaSaldi(
//							userContext, 
//							ordine, 
//							obblT,
//							status);
//
//				if (scadenzeConfermate == null || !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(scadenzeConfermate, obblT))
//					aggiornaObbligazioniTemporanee(userContext, obblT);
//			}
//		}
//	}
//private OrdineAcqBulk manageDeletedElements(
//		UserContext userContext, 
//		OrdineAcqBulk ordine,
//		OptionRequestParameter status)
//		throws ComponentException {
//
//		if (ordine != null) {
//			manageDocumentiContabiliCancellati(userContext, ordine, status);
//		}
//		return ordine;
//	}
//public void controllaQuadraturaObbligazioni(UserContext aUC,OrdineAcqBulk ordine)
//		throws ComponentException {
//
//		if (ordine != null ) {
//			ObbligazioniTable obbligazioniHash = ordine.getOrdineObbligazioniHash();
//			if (obbligazioniHash != null) {
//				for (java.util.Enumeration e = obbligazioniHash.keys(); e.hasMoreElements();) {
//					Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
//					
//					try {
//						scadenza = (Obbligazione_scadenzarioBulk)getTempHome(aUC, Obbligazione_scadenzarioBulk.class).findByPrimaryKey(
//								new Obbligazione_scadenzarioBulk(
//										scadenza.getCd_cds(),
//										scadenza.getEsercizio(),
//										scadenza.getEsercizio_originale(),
//										scadenza.getPg_obbligazione(),
//										scadenza.getPg_obbligazione_scadenzario()
//						                ));
//					} catch (PersistencyException e1) {
//						throw new ComponentException(e1);
//					}
//
//					
//					
//					java.math.BigDecimal totale = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//					java.math.BigDecimal delta = null;
//					totale = calcolaTotaleObbligazione(aUC, scadenza, ordine);
//					delta = scadenza.getIm_scadenza().subtract(totale);
//					if (delta.compareTo(new java.math.BigDecimal(0)) > 0) {
//						StringBuffer sb = new StringBuffer();
//						sb.append("Attenzione: La scadenza ");
//						sb.append(scadenza.getDs_scadenza());
//						sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
//						sb.append(" � stata coperta solo per ");
//						sb.append(totale.doubleValue() + " EUR!");
//						throw new it.cnr.jada.comp.ApplicationException(sb.toString());
//					} else if (delta.compareTo(new java.math.BigDecimal(0)) < 0) {
//						StringBuffer sb = new StringBuffer();
//						sb.append("Attenzione: La scadenza ");
//						sb.append(scadenza.getDs_scadenza());
//						sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
//						sb.append(" � scoperta per ");
//						sb.append(delta.abs().doubleValue() + " EUR!");
//						throw new it.cnr.jada.comp.ApplicationException(sb.toString());
//					}					
//				}
//			}
//		}
//	}
//
//public OrdineAcqBulk creaOrdineDaRichieste(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine, List<RichiestaUopBulk> lista) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
//	for (RichiestaUopBulk richiesta : lista){
//		for (Object riga : richiesta.getRigheRichiestaColl()){
//			RichiestaUopRigaBulk rigaRichiesta = (RichiestaUopRigaBulk)riga;
//			creaRigaOrdine(userContext,ordine, rigaRichiesta);
//		}
//	}
//	return ordine;
//}
//private void creaRigaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine, RichiestaUopRigaBulk rigaRichiesta) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
//	boolean trovataRiga = false;
//	for (Object riga : ordine.getRigheOrdineColl()){
//		OrdineAcqRigaBulk rigaOrdine = (OrdineAcqRigaBulk)riga;
//	}
//	if (!trovataRiga){
//		OrdineAcqRigaBulk rigaOrdine = new OrdineAcqRigaBulk();
//		rigaOrdine = (OrdineAcqRigaBulk)rigaOrdine.inizializzaPerInserimento(userContext);
//		rigaOrdine.setBeneServizio(rigaRichiesta.getBeneServizio());
//		rigaOrdine.setDsBeneServizio(rigaRichiesta.getDsBeneServizio());
//		rigaOrdine.setCdBeneServizio(rigaRichiesta.getCdBeneServizio());
//		Bene_servizioBulk bene;
//		try {
//			bene = recuperoBeneServizio(userContext, rigaRichiesta.getCdBeneServizio());
//			if (bene != null){
//				rigaOrdine.setVoceIva(bene.getVoce_iva());
//			}
//		} catch (PersistencyException e) {
//			throw new ComponentException(e);
//		}
//		rigaOrdine.setUnitaMisura(rigaRichiesta.getUnitaMisura());
//		rigaOrdine.setCoefConv(rigaRichiesta.getCoefConv());
//		rigaOrdine.setNotaRiga(rigaRichiesta.getNotaRiga());
//		rigaOrdine.setDspTipoConsegna(Bene_servizioBulk.TIPO_CONSEGNA_TRANSITO);
//		rigaOrdine.setDspUopDest(rigaRichiesta.getRichiestaUop().getUnitaOperativaOrd());
//		OrdineAcqConsegnaBulk consegna = new OrdineAcqConsegnaBulk();
//		consegna.inizializzaConsegnaNuovaRiga();
//		consegna.setTipoConsegna(Bene_servizioBulk.TIPO_CONSEGNA_TRANSITO);
//		consegna.setUnitaOperativaOrd(rigaRichiesta.getRichiestaUop().getUnitaOperativaOrd());
//		consegna.setQuantita(rigaRichiesta.getQuantitaAutorizzata());
//		consegna.setDtPrevConsegna(rigaOrdine.getDspDtPrevConsegna());
//		rigaOrdine.addToRigheConsegnaColl(consegna);
//		ordine.addToRigheOrdineColl(rigaOrdine);
//	}
//}
//
//public SQLBuilder selectContrattoByClause(UserContext userContext, OrdineAcqBulk ordine, ContrattoBulk contratto, CompoundFindClause clauses) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
//{
//	Parametri_cdsHome paramHome = (Parametri_cdsHome)getHome(userContext, Parametri_cdsBulk.class);
//	Parametri_cdsBulk param_cds;
//	try {
//		param_cds =
//			(Parametri_cdsBulk) paramHome.findByPrimaryKey(
//				new Parametri_cdsBulk(
//						ordine.getCd_cds(),
//						ordine.getEsercizio()));
//	} catch (PersistencyException e) {
//		throw new ComponentException(e);
//	}
//	
//	SQLBuilder sql = getHome(userContext,ContrattoBulk.class).createSQLBuilder();
//	
//	if (clauses != null) 
//	  sql.addClause(clauses);
//	sql.openParenthesis("AND");  
//	  sql.addSQLClause("AND","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_PASSIVO);
//	  sql.addSQLClause("OR","NATURA_CONTABILE",SQLBuilder.EQUALS, ContrattoBulk.NATURA_CONTABILE_ATTIVO_E_PASSIVO);
//	sql.closeParenthesis();  
//	if(param_cds != null && param_cds.getFl_contratto_cessato().booleanValue()){
//		sql.openParenthesis("AND");  
//		  sql.addSQLClause("AND","STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
//		  sql.addSQLClause("OR","STATO",SQLBuilder.EQUALS, ContrattoBulk.STATO_CESSSATO);
//		sql.closeParenthesis();		
//	}	
//	else  
//	  sql.addSQLClause("AND", "STATO", sql.EQUALS, ContrattoBulk.STATO_DEFINITIVO);
//
//	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
//	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
//	  sql.openParenthesis("AND");
//		sql.addSQLClause("AND","CONTRATTO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
//		SQLBuilder sqlAssUo = getHome(userContext,Ass_contratto_uoBulk.class).createSQLBuilder();		   
//		sqlAssUo.addSQLJoin("CONTRATTO.ESERCIZIO","ASS_CONTRATTO_UO.ESERCIZIO");
//		sqlAssUo.addSQLJoin("CONTRATTO.PG_CONTRATTO","ASS_CONTRATTO_UO.PG_CONTRATTO");
//		sqlAssUo.addSQLClause("AND","ASS_CONTRATTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
//		sql.addSQLExistsClause("OR",sqlAssUo);
//	  sql.closeParenthesis();  		 
//	}
//	sql.addTableToHeader("TERZO");
//	sql.addSQLJoin("CONTRATTO.FIG_GIUR_EST", SQLBuilder.EQUALS,"TERZO.CD_TERZO");
//	sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
//		
//	if((ordine.getFornitore() != null && ordine.getFornitore().getCd_terzo()!=null)){
//		sql.openParenthesis("AND");
//	    sql.openParenthesis("AND");
//	    sql.addSQLClause(FindClause.AND, "FIG_GIUR_EST",SQLBuilder.EQUALS,ordine.getFornitore().getCd_terzo());
//		AnagraficoHome anagraficoHome = (AnagraficoHome) getHome(userContext, AnagraficoBulk.class);
//		sql.closeParenthesis();
//		try {
//			for (Iterator<Anagrafico_terzoBulk> i = anagraficoHome.findAssociatiStudio(ordine.getFornitore().getAnagrafico()).iterator(); i.hasNext();) {
//				sql.openParenthesis("OR");
//					Anagrafico_terzoBulk associato = i.next();
//					sql.addSQLClause("OR", "CONTRATTO.FIG_GIUR_EST",SQLBuilder.EQUALS, associato.getCd_terzo());
//				sql.closeParenthesis();
//			}
//		} catch (IntrospectionException e) { 
//		}
//	  sql.closeParenthesis();
//	}
//	/*
//    sql.openParenthesis("AND");	   
//	  sql.addSQLClause("AND","TRUNC(NVL(DT_FINE_VALIDITA,SYSDATE)) >= TRUNC(SYSDATE)");
//	  sql.addSQLClause("OR","(DT_PROROGA IS NOT NULL AND TRUNC(DT_PROROGA) >= TRUNC(SYSDATE))");
//	sql.closeParenthesis();
//	*/  
//	return sql;
//}
//public void verificaCoperturaContratto (UserContext aUC,OrdineAcqBulk ordine, int flag) throws ComponentException
//{
//	if (ordine.getContratto() != null && ordine.getContratto().getPg_contratto() != null){
//	  try {	
//		  ContrattoHome contrattoHome = (ContrattoHome)getHome(aUC, ContrattoBulk.class);
//		  SQLBuilder sql = contrattoHome.calcolaTotOrdini(aUC,ordine.getContratto());
//		  BigDecimal totale = null; 
//			try {
//				java.sql.ResultSet rs = null;
//				LoggableStatement ps = null;
//				try {
//					ps = sql.prepareStatement(getConnection(aUC));
//					try {
//						rs = ps.executeQuery();
//						if (rs.next())
//						totale = rs.getBigDecimal(1);
//					} catch (java.sql.SQLException e) {
//						throw handleSQLException(e);
//					} finally {
//						if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
//					}
//				} finally {
//					if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
//				}
//			} catch (java.sql.SQLException ex) {
//				throw handleException(ex);
//			}
//		  if (flag == INSERIMENTO){
//			    totale = totale.add(ordine.getImTotaleOrdine());			  
//		  } else if (flag == MODIFICA){
//				OrdineAcqBulk ordineDB;
//				try {
//					ordineDB = (OrdineAcqBulk)getTempHome(aUC, OrdineAcqBulk.class).findByPrimaryKey(
//							new OrdineAcqBulk(
//									ordine.getCdCds(),
//									ordine.getCdUnitaOperativa(),
//									ordine.getEsercizio(),
//									ordine.getCdNumeratore(),
//									ordine.getNumero()
//					                ));
//				} catch (PersistencyException e) {
//					throw new ComponentException(e);
//				}
//
//			  totale = totale.subtract(Utility.nvl(ordineDB.getImTotaleOrdine())).add(Utility.nvl(ordine.getImTotaleOrdine()));
//		  }
//		  if (totale != null ){
//			  if (totale.compareTo(ordine.getContratto().getIm_contratto_passivo()) > 0){
//				  throw handleException( new ApplicationException("La somma degli ordini associati supera l'importo definito nel contratto."));
//			  }
//		  }
//	  } catch (IntrospectionException e1) {
//		  throw new it.cnr.jada.comp.ComponentException(e1);
//	  } catch (PersistencyException e1) {
//		  throw new it.cnr.jada.comp.ComponentException(e1);
//	  }
//  }		
//
//}
//public void verificaCoperturaContratto (UserContext aUC,OrdineAcqBulk ordine) throws ComponentException
//{
//	verificaCoperturaContratto (aUC,ordine, MODIFICA);
//}
//public OrdineAcqBulk cancellaOrdine(
//	    UserContext aUC,
//	    OrdineAcqBulk ordine)
//	    throws ComponentException {
//	    try {
//			for (java.util.Iterator i= ordine.getRigheOrdineColl().iterator(); i.hasNext();) {
//				OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//				if (riga.getDspObbligazioneScadenzario() != null && riga.getDspObbligazioneScadenzario().getPg_obbligazione() != null){
//					  throw new ApplicationException("Scollegare prima gli impegni collegati all'ordine prima di procedere alla cancellazione.");
//				}
//				if (riga != null){
//					for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
//						OggettoBulk consbulk= (OggettoBulk) c.next();
//						OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
//						if (cons.getObbligazioneScadenzario() != null && cons.getObbligazioneScadenzario().getPg_obbligazione() != null){
//							  throw new ApplicationException("Scollegare prima gli impegni collegati all'ordine prima di procedere alla cancellazione.");
//						}
//					}
//				}
//			}
//	            
//			ordine.setAnnullato(DateServices.getDt_valida(aUC));
//			ordine.setToBeUpdated();
//			makeBulkPersistent( aUC, ordine);
//			return ordine;
//	    } catch (Exception e) {
//	        throw handleException(e);
//	    }
//
//		}
//public Unita_organizzativaBulk recuperoUoPerImpegno
//(
//		UserContext aUC,
//		OrdineAcqConsegnaBulk consegna)
//				throws ComponentException {
//	try {
//		if (!isUoImpegnoDaUopDestinazione(aUC)){
//			return recuperoUoOrdinante(aUC, consegna);
//		} else {
//			if (!consegna.isToBeDeleted()) {
//				if (!consegna.isConsegnaMagazzino()){
//					if (consegna.getCdUopDest() != null){
//						UnitaOperativaOrdBulk uop = recuperoUopDest(aUC, consegna);
//						if (uop != null){
//							return uop.getUnitaOrganizzativa();
//						}
//					} else {
//						throw new ApplicationException("E' necessario indicare l'unit� operativa per la consegna "+consegna.getConsegna() + " della riga "+consegna.getRiga());
//					}
//				} else {
//					if (consegna.getCdMagazzino() != null && consegna.getCdCdsMag() != null){
//						MagazzinoBulk magazzino = recuperoMagazzino(aUC, consegna);
//						if (magazzino != null && magazzino.getCdUnitaOperativa() != null){
//							UnitaOperativaOrdBulk uop = recuperoUop(aUC, magazzino.getUnitaOperativaOrd());
//							if (uop != null){
//								return uop.getUnitaOrganizzativa();
//							}
//						} else {
//							return recuperoUoOrdinante(aUC, consegna);
//						}
//					} else {
//						throw new ApplicationException("E' necessario indicare il magazzino per la consegna "+consegna.getConsegna() + " della riga "+consegna.getRiga());
//					}
//				}
//			}
//		}
//	} catch (Exception e) {
//		throw handleException(e);
//	}
//	return null;
//}
//
//private Unita_organizzativaBulk recuperoUoOrdinante(UserContext aUC, OrdineAcqConsegnaBulk consegna)
//		throws ComponentException, PersistencyException {
//	UnitaOperativaOrdBulk uop = recuperoUop(aUC, consegna.getOrdineAcqRiga().getOrdineAcq().getUnitaOperativaOrd());
//	if (uop != null){
//		return uop.getUnitaOrganizzativa();
//	}
//	throw new ApplicationException("Non � stato possibile recuperare l'unita' organizzativa ordinante");
//}
}