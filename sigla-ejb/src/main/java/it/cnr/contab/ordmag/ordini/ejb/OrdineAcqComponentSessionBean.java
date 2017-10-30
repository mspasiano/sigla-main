package it.cnr.contab.ordmag.ordini.ejb;
import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.comp.FatturaPassivaComponent;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.comp.OrdineAcqComponent;
import it.cnr.contab.ordmag.ordini.dto.ImportoOrdine;
import it.cnr.contab.ordmag.ordini.dto.ParametriCalcoloImportoOrdine;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Stateless(name="CNRORDMAG00_EJB_OrdineAcqComponentSession")
public class OrdineAcqComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements OrdineAcqComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new OrdineAcqComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new OrdineAcqComponentSessionBean();
}
public Boolean isUtenteAbilitatoOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(usercontext,componentObj);
	try {
		Boolean result = ((OrdineAcqComponent)componentObj).isUtenteAbilitatoOrdine(usercontext, ordine);
		component_invocation_succes(usercontext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(usercontext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(usercontext,componentObj,e);
	}
}
public Boolean isUtenteAbilitatoValidazioneOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException {
	pre_component_invocation(usercontext,componentObj);
	try {
		Boolean result = ((OrdineAcqComponent)componentObj).isUtenteAbilitatoValidazioneOrdine(usercontext, ordine);
		component_invocation_succes(usercontext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		component_invocation_failure(usercontext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(usercontext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(usercontext,componentObj,e);
	}
}

public void controllaQuadraturaObbligazioni(UserContext userContext,OrdineAcqBulk ordine) throws PersistencyException,ComponentException, javax.ejb.EJBException, RemoteException {
	pre_component_invocation(userContext,componentObj);
	try {
		((OrdineAcqComponent)componentObj).controllaQuadraturaObbligazioni(userContext, ordine);
		component_invocation_succes(userContext,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}

public void completaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws PersistencyException,ComponentException, javax.ejb.EJBException, RemoteException {
	pre_component_invocation(userContext,componentObj);
	try {
		((OrdineAcqComponent)componentObj).completaOrdine(userContext, ordine);
		component_invocation_succes(userContext,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
public it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.util.RemoteIterator result = ((OrdineAcqComponent)componentObj).cercaObbligazioni(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public OrdineAcqBulk creaOrdineDaRichieste(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine, List<RichiestaUopBulk> lista) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(userContext,componentObj);
	try {
		OrdineAcqBulk result = ((OrdineAcqComponent)componentObj).creaOrdineDaRichieste(userContext, ordine, lista);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
public OrdineAcqBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,OrdineAcqBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		OrdineAcqBulk result = ((OrdineAcqComponent)componentObj).contabilizzaDettagliSelezionati(param0,param1,param2,param3);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public OrdineAcqBulk calcolaImportoOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
	pre_component_invocation(userContext,componentObj);
	try {
		OrdineAcqBulk result = ((OrdineAcqComponent)componentObj).calcolaImportoOrdine(userContext, ordine);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
public OrdineAcqBulk cancellaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
	pre_component_invocation(userContext,componentObj);
	try {
		OrdineAcqBulk result = ((OrdineAcqComponent)componentObj).cancellaOrdine(userContext, ordine);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}

public Unita_organizzativaBulk recuperoUoPerImpegno(it.cnr.jada.UserContext userContext, OrdineAcqConsegnaBulk consegna) throws RemoteException,ComponentException, PersistencyException{
	pre_component_invocation(userContext,componentObj);
	try {
		Unita_organizzativaBulk result = ((OrdineAcqComponent)componentObj).recuperoUoPerImpegno(userContext, consegna);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
		
public OrdineAcqBulk contabilizzaConsegneSelezionate(it.cnr.jada.UserContext param0,OrdineAcqBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		OrdineAcqBulk result = ((OrdineAcqComponent)componentObj).contabilizzaConsegneSelezionate(param0,param1,param2,param3);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public ImportoOrdine calcoloImportoOrdine(UserContext userContext, ParametriCalcoloImportoOrdine parametri) throws RemoteException,ComponentException{
	pre_component_invocation(userContext,componentObj);
	try {
		ImportoOrdine result = ((OrdineAcqComponent)componentObj).calcoloImportoOrdine(parametri);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
public ImportoOrdine calcoloImportoOrdinePerMagazzino(UserContext userContext, ParametriCalcoloImportoOrdine parametri) throws RemoteException,ComponentException{
	pre_component_invocation(userContext,componentObj);
	try {
		ImportoOrdine result = ((OrdineAcqComponent)componentObj).calcoloImportoOrdinePerMagazzino(parametri);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}
}