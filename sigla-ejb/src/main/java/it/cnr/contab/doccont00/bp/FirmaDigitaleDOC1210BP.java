package it.cnr.contab.doccont00.bp;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;

/**
 *
 * @author mspasiano
 * @date 30-11-2015
 *
 *
 */
public class FirmaDigitaleDOC1210BP extends AbstractFirmaDigitaleDocContBP {
	private static final long serialVersionUID = 1L;

	public FirmaDigitaleDOC1210BP() {
		super();
	}

	public FirmaDigitaleDOC1210BP(String s) {
		super(s);
	}

	@Override
	public void openIterator(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			Unita_organizzativaBulk uoScrivania = CNRUserInfo.getUnita_organizzativa(actioncontext);
			CompoundFindClause compoundfindclause = new CompoundFindClause();
			compoundfindclause.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS,
					((CNRUserContext) actioncontext.getUserContext()).getEsercizio());
			if (uoScrivania.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)!=0) {
				compoundfindclause.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS,
						((CNRUserContext) actioncontext.getUserContext()).getCd_cds());
				if (!uoScrivania.isUoCds())
					compoundfindclause.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS,
							((CNRUserContext) actioncontext.getUserContext()).getCd_unita_organizzativa());
			}
			setBaseclause(compoundfindclause);
			Lettera_pagam_esteroBulk lettera = (Lettera_pagam_esteroBulk) getModel();
			Lettera_pagam_esteroBulk letteraAll = new Lettera_pagam_esteroBulk();
			letteraAll.setStato_trasmissione(null);

			setIterator(actioncontext, find(actioncontext, compoundfindclause, lettera.getStato_trasmissione().equalsIgnoreCase(StatoTrasmissione.ALL) ? letteraAll : lettera));
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	@Override
	protected void aggiornaStato(ActionContext actioncontext, String stato, StatoTrasmissione...bulks) throws ComponentException, RemoteException {
		for (StatoTrasmissione statoTrasmissione : bulks) {
			Lettera_pagam_esteroBulk lettera = new Lettera_pagam_esteroBulk(statoTrasmissione.getCd_cds(),statoTrasmissione.getCd_unita_organizzativa(),
					statoTrasmissione.getEsercizio(), statoTrasmissione.getPg_documento_cont());
			lettera = (Lettera_pagam_esteroBulk) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(), lettera);
			if (!statoTrasmissione.getStato_trasmissione().equals(lettera.getStato_trasmissione()))
				throw new ApplicationException("Risorsa non pi� valida, eseguire nuovamente la ricerca!");
			lettera.setStato_trasmissione(stato);
			if (stato.equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))
				lettera.setDt_firma(EJBCommonServices.getServerTimestamp());
			else
				lettera.setDt_firma(null);
			lettera.setToBeUpdated();
			getComponentSession().modificaConBulk(actioncontext.getUserContext(), lettera);
		}
	}

	@Override
	public StatoTrasmissione getStatoTrasmissione(ActionContext actioncontext, Integer esercizio, String tipo, String cds, String uo, Long numero_documento) {
		return new Lettera_pagam_esteroBulk(cds,uo, esercizio,numero_documento);
	}

	private PDField valorizzaField(PDAcroForm pdAcroForm, String fieldName, String fieldValue, boolean autosize) throws IOException {
        PDField field = pdAcroForm.getField(fieldName);
        if (field != null) {
            if (field instanceof PDCheckBox) {
                if (Boolean.valueOf(fieldValue))
                    ((PDCheckBox)field).check();
                else
                    ((PDCheckBox)field).unCheck();
            } else {
                field.setValue(
                        Optional.ofNullable(fieldValue)
                            .map(s -> s.replace("\r", "\n"))
                            .orElse(""));
            }
        }
        return field;
	}

	@Override
	@SuppressWarnings({ "unused", "unchecked" })
	public void predisponiPerLaFirma(ActionContext actioncontext) throws BusinessProcessException{
		try {
			List<Lettera_pagam_esteroBulk> selectedElements = getSelectedElements(actioncontext);
			if (selectedElements == null || selectedElements.isEmpty())
				throw new ApplicationException("Selezionare almeno un elemento!");
			Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			for (Lettera_pagam_esteroBulk lettera : selectedElements) {
				PDDocument document = PDDocument.load(this.getClass().getResourceAsStream("1210.pdf"));
				PDDocumentCatalog pdCatalog = document.getDocumentCatalog();
				PDAcroForm pdAcroForm = pdCatalog.getAcroForm();
                List<PDField> fields = new ArrayList<PDField>();
				fields.add(valorizzaField(pdAcroForm, "LUOGO", "ROMA", false));
                fields.add(valorizzaField(pdAcroForm, "DATA", new SimpleDateFormat("dd/MM/yyyy").format(lettera.getDt_registrazione()), false));
                fields.add(valorizzaField(pdAcroForm, "NUM_RIF", String.valueOf(lettera.getPg_lettera()) + " - " + lettera.getCd_unita_organizzativa(), false));
                fields.add(valorizzaField(pdAcroForm, "BONIFICO_MEZZO_"+lettera.getBonifico_mezzo(), "X", false));
                fields.add(valorizzaField(pdAcroForm, "DIVISA", lettera.getDivisa(), false));
                fields.add(valorizzaField(pdAcroForm, "IMPORTO", new java.text.DecimalFormat("#,##0.00").format(lettera.getIm_pagamento()), false));
                fields.add(valorizzaField(pdAcroForm, "IMPORTO_LETTERE", Utility.NumberToText(lettera.getIm_pagamento()), false));
                fields.add(valorizzaField(pdAcroForm, "BENEFICIARIO_1", lettera.getBeneficiario(), true));
                fields.add(valorizzaField(pdAcroForm, "NUM_CONTO", lettera.getNum_conto_ben(), true));
                fields.add(valorizzaField(pdAcroForm, "IBAN", lettera.getIban(), false));
                fields.add(valorizzaField(pdAcroForm, "PRESSO_TRAMITE", lettera.getIndirizzo(), true));
                fields.add(valorizzaField(pdAcroForm, "SWIFT_BIC_ADDRESS", lettera.getIndirizzo_swift(), false));
                fields.add(valorizzaField(pdAcroForm, "MOTIVO_PAGAMENTO", lettera.getMotivo_pag(), true));
                fields.add(valorizzaField(pdAcroForm, "AMMONTARE_DEBITO_"+lettera.getAmmontare_debito(), "X", false));
                fields.add(valorizzaField(pdAcroForm, "CONTO_PROVVISORIO_"+lettera.getAmmontare_debito(), lettera.getConto_debito(), false));
                fields.add(valorizzaField(pdAcroForm, "COMMISSIONI_SPESE_"+lettera.getCommissioni_spese(), "X", false));
                fields.add(valorizzaField(pdAcroForm, "COMMISSIONI_SPESE_ESTERE_"+lettera.getCommissioni_spese_estere(), "X", false));

                pdAcroForm.flatten(fields.stream().filter(pdField -> Optional.ofNullable(pdField).isPresent()).collect(Collectors.toList()), true);

                ByteArrayOutputStream output = new ByteArrayOutputStream();
				document.save(output);
				document.close();
				SpringUtil.getBean("storeService", StoreService.class).restoreSimpleDocument(
						lettera,
						new ByteArrayInputStream(output.toByteArray()),
						"application/pdf",
						lettera.getCMISFolderName() + ".pdf",
						lettera.getStorePath(),
						true);
				aggiornaStato(actioncontext, MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO, lettera);
			}
			setMessage("Predisposizione effettuata correttamente.");
		} catch (ApplicationException e) {
			setMessage(e.getMessage());
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
	}

	@Override
	protected AbilitatoFirma getAbilitatoFirma() {
		return AbilitatoFirma.DOC_1210;
	}
	@Override
	protected String getTitoloFirma() {
		return "firma per il controllo di ragioneria";
	}
}