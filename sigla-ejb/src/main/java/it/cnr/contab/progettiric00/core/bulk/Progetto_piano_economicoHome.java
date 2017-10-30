package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Progetto_piano_economicoHome extends BulkHome {

	public Progetto_piano_economicoHome(java.sql.Connection conn) {
		super(Progetto_piano_economicoBulk.class,conn);
	}
	
	public Progetto_piano_economicoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Progetto_piano_economicoBulk.class,conn,persistentCache);
	}

	public java.util.Collection findProgettoPianoEconomicoList( Integer pgProgetto ) throws PersistencyException 
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		return this.fetchAll(sql);
	}

	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		V_saldi_piano_econom_progettoBulk saldoSpesa = ((V_saldi_piano_econom_progettoHome)getHomeCache().getHome(V_saldi_piano_econom_progettoBulk.class )).
				cercaSaldoPianoEconomico((Progetto_piano_economicoBulk)persistent, "S");

		V_saldi_piano_econom_progettoBulk saldoEntrata = ((V_saldi_piano_econom_progettoHome)getHomeCache().getHome(V_saldi_piano_econom_progettoBulk.class )).
				cercaSaldoPianoEconomico((Progetto_piano_economicoBulk)persistent, "E");

		((Progetto_piano_economicoBulk)persistent).setSaldoSpesa(saldoSpesa);
		((Progetto_piano_economicoBulk)persistent).setSaldoEntrata(saldoEntrata);

		return super.completeBulkRowByRow(userContext, persistent);
	}
}