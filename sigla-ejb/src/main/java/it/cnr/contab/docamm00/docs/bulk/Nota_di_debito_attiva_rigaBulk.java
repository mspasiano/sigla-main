package it.cnr.contab.docamm00.docs.bulk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Insert the type's description here.
 * Creation date: (10/25/2001 11:52:43 AM)
 * @author: Roberto Peli
 */
@JsonInclude(value=Include.NON_NULL)
public class Nota_di_debito_attiva_rigaBulk extends Fattura_attiva_rigaBulk {
	@JsonIgnore
	private Nota_di_debito_attivaBulk notaDiDebito;
	@JsonIgnore
	private Fattura_attiva_rigaIBulk riga_fattura_associata;

	public final static java.util.Dictionary STATO;

	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INIZIALE,"Iniziale");
		STATO.put(STATO_CONTABILIZZATO,"Addebitato");
		STATO.put(STATO_LIQUIDATO,"Liquidato");
		STATO.put(STATO_ANNULLATO,"Annullato");
		STATO.put(STATO_PAGATO,"Pagato");
	}

	/**
	 * Nota_di_credito_rigaBulk constructor comment.
	 */
	public Nota_di_debito_attiva_rigaBulk() {
		super();
	}
	/**
	 * Nota_di_credito_rigaBulk constructor comment.
	 */
	public Nota_di_debito_attiva_rigaBulk(Fattura_attiva_rigaIBulk dettaglio) {
		super();

		try {
			copyFrom(dettaglio);
		} catch (it.cnr.jada.bulk.FillException e) {
		}
	}
	public Nota_di_debito_attiva_rigaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva,java.lang.Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva,progressivo_riga);
		setNotaDiDebito(new it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva));
	}
	public void copyFrom(Fattura_attiva_rigaIBulk dettaglio) 
			throws it.cnr.jada.bulk.FillException {

		setStato_cofi(STATO_INIZIALE);
		setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

		setDt_da_competenza_coge(dettaglio.getDt_da_competenza_coge());
		setDt_a_competenza_coge(dettaglio.getDt_a_competenza_coge());
		setTariffario(dettaglio.getTariffario());
		setTi_promiscuo(dettaglio.getTi_promiscuo());
		setProgressivo_riga(new Long(getNotaDiDebito().getFattura_attiva_dettColl().size() + 1));

		setVoce_iva(dettaglio.getVoce_iva());
		setBene_servizio(dettaglio.getBene_servizio());
		boolean zeroNotValid = false;
		if (dettaglio.hasAddebiti()) {
			setQuantita(new java.math.BigDecimal(1));
			setPrezzo_unitario(new java.math.BigDecimal(0));
			setFl_iva_forzata(Boolean.FALSE);
			zeroNotValid = true;
		} else {
			setQuantita(dettaglio.getQuantita());
			setPrezzo_unitario(dettaglio.getPrezzo_unitario());
			setFl_iva_forzata(dettaglio.getFl_iva_forzata());
			if (getFl_iva_forzata() == null)
				setFl_iva_forzata(Boolean.FALSE);
			if (getFl_iva_forzata().booleanValue())
				setIm_iva(dettaglio.getIm_iva());
		}
		setDs_riga_fattura(dettaglio.getDs_riga_fattura());
		setRiga_fattura_associata(dettaglio);

		calcolaCampiDiRiga();
		java.math.BigDecimal vecchioTotale = new java.math.BigDecimal(0).setScale(0, java.math.BigDecimal.ROUND_HALF_UP);
		java.math.BigDecimal totaleDiRiga = getIm_imponibile().add(getIm_iva());
		java.math.BigDecimal nuovoImportoDisponibile = dettaglio.getIm_diponibile_nc().add(totaleDiRiga.subtract(vecchioTotale));
		if (nuovoImportoDisponibile.signum() < 0 || (nuovoImportoDisponibile.signum() == 0 && zeroNotValid)) {
			if (dettaglio.getDs_riga_fattura() == null)
				throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile � di " + dettaglio.getIm_diponibile_nc() + " EUR!");
			else 
				throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile per \"" + dettaglio.getDs_riga_fattura() + "\" � di " + dettaglio.getIm_diponibile_nc() + " EUR!");
		}
		dettaglio.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	}
	public IDocumentoAmministrativoRigaBulk getAssociatedDetail() {

		return (IDocumentoAmministrativoRigaBulk)getRiga_fattura_associata();
	}
	public java.lang.String getCd_cds() {
		it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk notaDiDebito = this.getNotaDiDebito();
		if (notaDiDebito == null)
			return null;
		return notaDiDebito.getCd_cds();
	}
	public java.lang.String getCd_cds_assncna_fin() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
		if (riga_fattura_associata == null)
			return null;
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = riga_fattura_associata.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getCd_cds();
	}
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk notaDiDebito = this.getNotaDiDebito();
		if (notaDiDebito == null)
			return null;
		return notaDiDebito.getCd_unita_organizzativa();
	}
	public java.lang.String getCd_uo_assncna_fin() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
		if (riga_fattura_associata == null)
			return null;
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = riga_fattura_associata.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getCd_unita_organizzativa();
	}
	public java.lang.Integer getEsercizio() {
		it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk notaDiDebito = this.getNotaDiDebito();
		if (notaDiDebito == null)
			return null;
		return notaDiDebito.getEsercizio();
	}
	public java.lang.Integer getEsercizio_assncna_fin() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
		if (riga_fattura_associata == null)
			return null;
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = riga_fattura_associata.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getEsercizio();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/25/2001 12:03:34 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
	 */
	public Fattura_attivaBulk getFattura_attiva() {

		return getNotaDiDebito();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/17/2001 10:32:30 AM)
	 * @return it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk
	 */
	public Nota_di_debito_attivaBulk getNotaDiDebito() {
		return notaDiDebito;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (2/13/2002 6:15:36 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
	 */
	public IDocumentoAmministrativoRigaBulk getOriginalDetail() {
		return getRiga_fattura_associata();
	}
	public java.lang.Long getPg_fattura_assncna_fin() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
		if (riga_fattura_associata == null)
			return null;
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk fattura_attivaI = riga_fattura_associata.getFattura_attivaI();
		if (fattura_attivaI == null)
			return null;
		return fattura_attivaI.getPg_fattura_attiva();
	}
	public java.lang.Long getPg_fattura_attiva() {
		it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk notaDiDebito = this.getNotaDiDebito();
		if (notaDiDebito == null)
			return null;
		return notaDiDebito.getPg_fattura_attiva();
	}
	public java.lang.Long getPg_riga_assncna_fin() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
		if (riga_fattura_associata == null)
			return null;
		return riga_fattura_associata.getProgressivo_riga();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/25/2001 2:48:15 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk
	 */
	public Fattura_attiva_rigaIBulk getRiga_fattura_associata() {
		return riga_fattura_associata;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getStato_cofiKeys() {
		return STATO;
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */

	public boolean isAddebitato() {

		return !STATO_INIZIALE.equals(getStato_cofi());
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.getNotaDiDebito().setCd_cds(cd_cds);
	}
	public void setCd_cds_assncna_fin(java.lang.String cd_cds_assncna_fin) {
		this.getRiga_fattura_associata().getFattura_attivaI().setCd_cds(cd_cds_assncna_fin);
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getNotaDiDebito().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	public void setCd_uo_assncna_fin(java.lang.String cd_uo_assncna_fin) {
		this.getRiga_fattura_associata().getFattura_attivaI().setCd_unita_organizzativa(cd_uo_assncna_fin);
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.getNotaDiDebito().setEsercizio(esercizio);
	}
	public void setEsercizio_assncna_fin(java.lang.Integer esercizio_assncna_fin) {
		this.getRiga_fattura_associata().getFattura_attivaI().setEsercizio(esercizio_assncna_fin);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/2001 5:51:50 PM)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
	 */
	public void setFattura_attiva(Fattura_attivaBulk fattura_attiva) {

		setNotaDiDebito((Nota_di_debito_attivaBulk)fattura_attiva);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/17/2001 10:32:30 AM)
	 * @param newNotaDiDebito it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk
	 */
	public void setNotaDiDebito(Nota_di_debito_attivaBulk newNotaDiDebito) {
		notaDiDebito = newNotaDiDebito;
	}
	public void setPg_fattura_assncna_fin(java.lang.Long pg_fattura_assncna_fin) {
		this.getRiga_fattura_associata().getFattura_attivaI().setPg_fattura_attiva(pg_fattura_assncna_fin);
	}
	public void setPg_fattura_attiva(java.lang.Long pg_fattura_attiva) {
		this.getNotaDiDebito().setPg_fattura_attiva(pg_fattura_attiva);
	}
	public void setPg_riga_assncna_fin(java.lang.Long pg_riga_assncna_fin) {
		this.getRiga_fattura_associata().setProgressivo_riga(pg_riga_assncna_fin);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/25/2001 2:48:15 PM)
	 * @param newRiga_fattura_associata it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk
	 */
	public void setRiga_fattura_associata(Fattura_attiva_rigaIBulk newRiga_fattura_associata) {
		riga_fattura_associata = newRiga_fattura_associata;
	}
}
