package it.cnr.contab.web.rest;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.jada.comp.FatturaNonTrovataException;

import java.util.Collections;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/fatturaattiva")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.FATTURE_ATTIVE)
public class FatturaAttivaResource {
    private static final String FATTURA_ATTIVA_NON_PRESENTE = "Fattura attiva non presente.";
	private final Logger LOGGER = LoggerFactory.getLogger(FatturaAttivaResource.class);
	@Context SecurityContext securityContext;
    /**
     * POST  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @Path(value = "/ricerca")
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    public Response ricercaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception {
        LOGGER.debug("REST request per ricercare una fattura attiva." );
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();        
        try {
            Fattura_attivaBulk fatturaAttiva = fatturaComponent().ricercaFattura(userContext, userContext.getEsercizio().longValue(), 
            		userContext.getCd_cds(), userContext.getCd_unita_organizzativa(), pg);		
            return Response.ok(Optional.ofNullable(fatturaAttiva).orElseThrow(() -> new FatturaNonTrovataException(FATTURA_ATTIVA_NON_PRESENTE))).build();        	
        } catch(FatturaNonTrovataException _ex) {
        	return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PRESENTE)).build();
        }
	}
    
	private FatturaAttivaSingolaComponentSession fatturaComponent() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession");
	}    
}