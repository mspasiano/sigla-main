/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class AbilUtenteUopOperBase extends AbilUtenteUopOperKey implements Keyed {
//    TUTTI_MAGAZZINI VARCHAR(1) NOT NULL
	private java.lang.String tuttiMagazzini;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER
	 **/
	public AbilUtenteUopOperBase() {
		super();
	}
	public AbilUtenteUopOperBase(java.lang.String cdUtente, java.lang.String cdUnitaOperativa, java.lang.String cdTipoOperazione) {
		super(cdUtente, cdUnitaOperativa, cdTipoOperazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tuttiMagazzini]
	 **/
	public java.lang.String getTuttiMagazzini() {
		return tuttiMagazzini;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tuttiMagazzini]
	 **/
	public void setTuttiMagazzini(java.lang.String tuttiMagazzini)  {
		this.tuttiMagazzini=tuttiMagazzini;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}