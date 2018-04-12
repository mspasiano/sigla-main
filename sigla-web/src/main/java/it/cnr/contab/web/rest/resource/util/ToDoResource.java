package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.StatoDocumentoEleEnum;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.Albero_mainBulk;
import it.cnr.contab.utenze00.bulk.Albero_mainKey;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.util.ToDoLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class ToDoResource implements ToDoLocal {
    public static final String FIRMA_DIGITALE_PDG_VARIAZIONI_BP = "FirmaDigitalePdgVariazioniBP";
    public static final String FIRMA_DIGITALE_MANDATI_BP = "FirmaDigitaleMandatiBP";
    public static final String FIRMA_DIGITALE_DOC_1210_BP = "FirmaDigitaleDOC1210BP";
    public static final String CRUD_FATTURA_PASSIVA_ELETTRONICA_BP = "CRUDFatturaPassivaElettronicaBP";
    public static final String CRUD_MISSIONE_BP = "CRUDMissioneBP";
    private final Logger LOGGER = LoggerFactory.getLogger(ToDoResource.class);
    @Context
    SecurityContext securityContext;

    @EJB
    PdGVariazioniComponentSession pdGVariazioniComponentSession;
    @EJB
    GestioneLoginComponentSession gestioneLoginComponentSession;
    @EJB
    FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession;
    @EJB
    CRUDComponentSession crudComponentSession;
    @EJB
    UtenteComponentSession utenteComponentSession;
    @EJB
    MissioneComponentSession missioneComponentSession;

    public Response all(@Context HttpServletRequest request) {
        return Response.ok(
                Optional.ofNullable(getUserContext(request))
                        .filter(userContext -> {
                            try {
                                return !Optional.ofNullable(crudComponentSession.findByPrimaryKey(
                                        userContext,
                                        new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))))
                                        .filter(Unita_organizzativaBulk.class::isInstance)
                                        .map(Unita_organizzativaBulk.class::cast)
                                        .filter(Unita_organizzativaBulk::isUoEnte).isPresent();
                            } catch (ComponentException|RemoteException e) {
                                return false;
                            }
                        })
                        .map(userContext -> {
                            return Arrays.asList(ToDoBP.values()).stream()
                                    .filter(toDoBP -> {
                                        UtenteBulk utente = new UtenteBulk();
                                        utente.setCd_utente(userContext.getUser());
                                        utente.setFl_attiva_blocco(Boolean.FALSE);
                                        try {
                                            return gestioneLoginComponentSession.isBPEnableForUser(userContext, utente,
                                                    CNRUserContext.getCd_unita_organizzativa(userContext), toDoBP.name());
                                        } catch (ComponentException|RemoteException e) {
                                            return Boolean.FALSE;
                                        }
                                    }).collect(Collectors.toList());
                        })
                        .orElseGet(() -> Collections.emptyList())
        ).build();
    }

    public Response single(@Context HttpServletRequest request, ToDoBP toDoBP) {
        UserContext userContext = getUserContext(request);
        final List<ToDoDetail> result = new ArrayList<ToDoDetail>();
        try {
            final String cdNodo = getCdNodo(userContext, toDoBP.name(), toDoBP.getCdAccesso());
            switch (toDoBP) {
                case FirmaDigitalePdgVariazioniBP: {
                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(pdGVariazioniComponentSession.cercaVariazioniForDocumentale(userContext, null, new Pdg_variazioneBulk(),
                                    ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED, Boolean.TRUE))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    final int i = iterator.countElements();
                                    if (i > 0) {
                                        result.add(new ToDoDetail(
                                                cdNodo,
                                                "fa fa-fw fa-clipboard text-primary",
                                                "Varizioni al PdG",
                                                firstLabel(i),
                                                detailLabel(i, "Variazione", "Variazioni", "in attesa di firma digitale.")
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    break;
                }
                case FirmaDigitaleMandatiBP: {
                    V_mandato_reversaleBulk v_mandato_reversaleBulk = new V_mandato_reversaleBulk();
                    v_mandato_reversaleBulk.setStato_trasmissione("N");
                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(crudComponentSession.cerca(
                                    userContext,
                                    new CompoundFindClause(),
                                    v_mandato_reversaleBulk,
                                    "selectByClauseForFirmaMandati"))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    final int i = iterator.countElements();
                                    if (i > 0) {
                                        result.add(new ToDoDetail(
                                                cdNodo,
                                                "fa fa-fw fa-money text-primary",
                                                "Mandati/Reversali",
                                                firstLabel(i),
                                                detailLabel(i, "Mandato/Reversale", "Mandati/Reversali", "in attesa di predisposizione.")
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    if (Optional.ofNullable(utenteComponentSession.isUtenteAbilitatoFirma(userContext, AbilitatoFirma.DOCCONT)).isPresent()) {
                        v_mandato_reversaleBulk.setStato_trasmissione("P");
                        BulkLoaderIterator remoteIteratorDaFirmare =
                                Optional.ofNullable(crudComponentSession.cerca(
                                        userContext,
                                        new CompoundFindClause(),
                                        v_mandato_reversaleBulk,
                                        "selectByClauseForFirmaMandati"))
                                        .filter(BulkLoaderIterator.class::isInstance)
                                        .map(BulkLoaderIterator.class::cast)
                                        .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                        Optional.ofNullable(remoteIteratorDaFirmare)
                                .ifPresent(iterator -> {
                                    try {
                                        iterator.open(userContext);
                                        final int i = iterator.countElements();
                                        if (i > 0) {
                                            result.add(new ToDoDetail(
                                                    cdNodo,
                                                    "fa fa-fw fa-money text-info",
                                                    "Mandati/Reversali",
                                                    firstLabel(i),
                                                    detailLabel(i, "Mandato/Reversale", "Mandati/Reversali", "da firmare.")
                                            ));
                                        }
                                    } catch (ComponentException | RemoteException e) {
                                        throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                    } finally {
                                        iterator.ejbRemove();
                                    }
                                });
                    }
                    break;
                }
                case FirmaDigitaleDOC1210BP: {
                    Lettera_pagam_esteroBulk letteraPagamEsteroBulk = new Lettera_pagam_esteroBulk();
                    letteraPagamEsteroBulk.setStato_trasmissione("N");
                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(crudComponentSession.cerca(
                                    userContext,
                                    new CompoundFindClause(),
                                    letteraPagamEsteroBulk,
                                    "selectByClauseForFirma1210"))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    final int i = iterator.countElements();
                                    if (i > 0) {
                                        result.add(new ToDoDetail(
                                                cdNodo,
                                                "fa fa-fw fa-usd text-primary",
                                                "Lettera Pagamento Estero",
                                                firstLabel(i),
                                                detailLabel(i, "Documento", "Documenti", "1210 in attesa di predisposizione.")
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    if (Optional.ofNullable(utenteComponentSession.isUtenteAbilitatoFirma(userContext, AbilitatoFirma.DOC_1210)).isPresent()) {
                        letteraPagamEsteroBulk.setStato_trasmissione("P");
                        BulkLoaderIterator remoteIteratorDaFirmare =
                                Optional.ofNullable(crudComponentSession.cerca(
                                        userContext,
                                        new CompoundFindClause(),
                                        letteraPagamEsteroBulk,
                                        "selectByClauseForFirma1210"))
                                        .filter(BulkLoaderIterator.class::isInstance)
                                        .map(BulkLoaderIterator.class::cast)
                                        .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                        Optional.ofNullable(remoteIteratorDaFirmare)
                                .ifPresent(iterator -> {
                                    try {
                                        iterator.open(userContext);
                                        final int i = iterator.countElements();
                                        if (i > 0) {
                                            result.add(new ToDoDetail(
                                                    cdNodo,
                                                    "fa fa-fw fa-usd text-info",
                                                    "Lettera Pagamento Estero",
                                                    firstLabel(i),
                                                    detailLabel(i, "Documento", "Documenti", "1210 da firmare.")
                                            ));
                                        }
                                    } catch (ComponentException | RemoteException e) {
                                        throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                    } finally {
                                        iterator.ejbRemove();
                                    }
                                });
                    }
                    break;
                }
                case CRUDFatturaPassivaElettronicaBP: {
                    DocumentoEleTestataBulk documentoEleTestata = new DocumentoEleTestataBulk();
                    documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.AGGIORNATO.name());
                    documentoEleTestata.setFlIrregistrabile("N");

                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(fatturaElettronicaPassivaComponentSession.cerca(
                                    userContext,
                                    null,
                                    documentoEleTestata))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    final int i = iterator.countElements();
                                    if (i > 0) {
                                        result.add(new ToDoDetail(
                                                cdNodo,
                                                "fa fa-fw fa-cloud text-warning",
                                                "Fatture Elettroniche",
                                                firstLabel(i),
                                                detailLabel(i, "Fattura", "Fatture", "da completare.")
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.COMPLETO.name());
                    BulkLoaderIterator remoteIteratorDaCompletare =
                            Optional.ofNullable(fatturaElettronicaPassivaComponentSession.cerca(
                                    userContext,
                                    null,
                                    documentoEleTestata))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIteratorDaCompletare)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    final int i = iterator.countElements();
                                    if (i > 0) {
                                        result.add(new ToDoDetail(
                                                cdNodo,
                                                "fa fa-fw fa-cloud text-danger",
                                                "Fatture Elettroniche",
                                                firstLabel(i),
                                                detailLabel(i, "Fattura", "Fatture", "da registrare.")
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    break;
                }
                case CRUDMissioneBP: {
                    MissioneBulk missioneBulk = new MissioneBulk();
                    missioneBulk.setEsercizio(CNRUserContext.getEsercizio(userContext));
                    missioneBulk.setTi_provvisorio_definitivo(MissioneBulk.SALVA_PROVVISORIO);
                    BulkLoaderIterator remoteIterator =
                            Optional.ofNullable(missioneComponentSession.cerca(
                                    userContext,
                                    null,
                                    missioneBulk))
                                    .filter(BulkLoaderIterator.class::isInstance)
                                    .map(BulkLoaderIterator.class::cast)
                                    .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
                    Optional.ofNullable(remoteIterator)
                            .ifPresent(iterator -> {
                                try {
                                    iterator.open(userContext);
                                    final int i = iterator.countElements();
                                    if (i > 0) {
                                        result.add(new ToDoDetail(
                                                cdNodo,
                                                "fa fa-fw fa-briefcase text-primary",
                                                "Missioni",
                                                firstLabel(i),
                                                detailLabel(i, "Missione", "Missioni", "da confermare.")
                                        ));
                                    }
                                } catch (ComponentException | RemoteException e) {
                                    throw new RestException(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                                } finally {
                                    iterator.ejbRemove();
                                }
                            });
                    break;
                }
            }

        } catch (ComponentException | RemoteException e) {
            LOGGER.error("ERROR in todo method for: {}", toDoBP, e);
        }
        return Response.ok(result).build();
    };

    private UserContext getUserContext(HttpServletRequest request) {
        return Optional.ofNullable(securityContext.getUserPrincipal())
                .filter(UserContext.class::isInstance)
                .map(UserContext.class::cast)
                .orElseGet(() -> {
                    return Optional.ofNullable(request.getSession(false))
                            .flatMap(httpSession -> Optional.ofNullable(httpSession.getAttribute(HttpActionContext.USER_CONTEXT)))
                            .filter(UserContext.class::isInstance)
                            .map(UserContext.class::cast)
                            .orElse(null);
                });
    }

    private String firstLabel(int count) {
        return (count == 1) ? "È presente " : "Sono presenti ";
    }

    private String detailLabel(int count, String singolare, String plurale, String end) {
        return Arrays.asList(
                String.valueOf(count),
                (count == 1) ? singolare : plurale,
                end)
                .stream()
                .collect(Collectors.joining(" "));
    }

    private String getCdNodo(UserContext userContext, String businessProcess, String cdAccesso) throws RemoteException, ComponentException {
        final List<Albero_mainBulk> nodi = crudComponentSession.find(
                userContext, Albero_mainBulk.class,
                "findNodo", userContext, businessProcess, cdAccesso);
        return nodi
                .stream()
                .findAny()
                .map(Albero_mainKey::getCd_nodo)
                .orElse(null);
    }

    public class ToDoDetail {
        private String cdNodo;
        private String faClass;
        private String headerLabel;
        private String firstLabel;
        private String label;

        public ToDoDetail() {
        }

        public ToDoDetail(String cdNodo, String faClass, String headerLabel, String firstLabel, String label) {
            this.cdNodo = cdNodo;
            this.faClass = faClass;
            this.headerLabel = headerLabel;
            this.firstLabel = firstLabel;
            this.label = label;
        }

        public String getCdNodo() {
            return cdNodo;
        }

        public String getFaClass() {
            return faClass;
        }

        public String getHeaderLabel() {
            return headerLabel;
        }

        public String getLabel() {
            return label;
        }

        public String getFirstLabel() {
            return firstLabel;
        }
    }
}