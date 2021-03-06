--------------------------------------------------------
--  DDL for View VP_DPDG_CDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DPDG_CDR" ("ESERCIZIO", "CD_CDS", "DS_CDS", "CD_TIPO_UNITA", "CD_RESPONSABILE_CDS", "CD_RESPONSABILE_AMM_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "FL_UO_CDS", "FL_RUBRICA", "FL_PRESIDENTE_AREA", "CD_AREA_RICERCA", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_PROPRIO_CDR", "CD_RESPONSABILE_CDR", "INDIRIZZO", "CD_CDR_AFFERENZA", "STATO_PIANO", "ANNOTAZIONI", "FL_RIBALTATO_SU_AREA", "CD_LINEA_ATTIVITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "PG_DETT", "DT_REGISTRAZIONE", "DESCRIZIONE", "STATO", "ORIGINE", "CATEGORIA_DETTAGLIO", "FL_SOLA_LETTURA", "CD_CENTRO_RESPONSABILITA_CLGE", "CD_LINEA_ATTIVITA_CLGE", "TI_APPARTENENZA_CLGE", "TI_GESTIONE_CLGE", "CD_ELEMENTO_VOCE_CLGE", "PG_ENTRATA_CLGE", "IM_RH_CCS_COSTI", "IM_RI_CCS_SPESE_ODC", "IM_RJ_CCS_SPESE_ODC_ALTRA_UO", "IM_RK_CCS_SPESE_OGC", "IM_RL_CCS_SPESE_OGC_ALTRA_UO", "IM_RM_CSS_AMMORTAMENTI", "IM_RN_CSS_RIMANENZE", "IM_RO_CSS_ALTRI_COSTI", "IM_RP_CSS_VERSO_ALTRO_CDR", "IM_RQ_SSC_COSTI_ODC", "IM_RR_SSC_COSTI_ODC_ALTRA_UO", "IM_RS_SSC_COSTI_OGC", "IM_RT_SSC_COSTI_OGC_ALTRA_UO", "IM_RU_SPESE_COSTI_ALTRUI", "IM_RV_PAGAMENTI", "IM_RAA_A2_COSTI_FINALI", "IM_RAB_A2_COSTI_ALTRO_CDR", "IM_RAC_A2_SPESE_ODC", "IM_RAD_A2_SPESE_ODC_ALTRA_UO", "IM_RAE_A2_SPESE_OGC", "IM_RAF_A2_SPESE_OGC_ALTRA_UO", "IM_RAG_A2_SPESE_COSTI_ALTRUI", "IM_RAH_A3_COSTI_FINALI", "IM_RAI_A3_COSTI_ALTRO_CDR", "IM_RAL_A3_SPESE_ODC", "IM_RAM_A3_SPESE_ODC_ALTRA_UO", "IM_RAN_A3_SPESE_OGC", "IM_RAO_A3_SPESE_OGC_ALTRA_UO", "IM_RAP_A3_SPESE_COSTI_ALTRUI", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "CD_CENTRO_RESPONSABILITA_CLGS", "CD_LINEA_ATTIVITA_CLGS", "TI_APPARTENENZA_CLGS", "TI_GESTIONE_CLGS", "CD_ELEMENTO_VOCE_CLGS", "PG_SPESA_CLGS", "FL_LIMITE_ASS_OBBLIG", "FL_VOCE_PERSONALE", "CD_PARTE", "DS_ELEMENTO_VOCE", "CD_TITOLO", "DS_TITOLO", "CD_CAPOCONTO", "DS_CAPOCONTO", "CD_TIPO_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_GRUPPO_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "DS_LINEA_ATTIVITA", "CD_CDR_COLLEGATO", "CD_LA_COLLEGATO", "DS_FUNZIONE", "DS_NATURA", "CD_PROGETTO", "DS_PROGETTO") AS 
  select
--
-- Date: 16/11/2002
-- Version: 1.1
--
-- Vista di stampa dei dettagli pdg CDR: legge parte spesa ed entrata dalle viste da cui dipende
--
-- History:
--
-- Date: 10/11/2001
-- Version: 1.0
-- Creazione
--
-- Date: 16/11/2002
-- Version: 1.1
-- Eliminazione risultato da tale gerarchia di viste
--
-- Body:
--
ESERCIZIO,
CD_CDS,
DS_CDS,
CD_TIPO_UNITA,
CD_RESPONSABILE_CDS,
CD_RESPONSABILE_AMM_CDS,
CD_UNITA_ORGANIZZATIVA,
DS_UNITA_ORGANIZZATIVA,
FL_UO_CDS,
FL_RUBRICA,
FL_PRESIDENTE_AREA,
CD_AREA_RICERCA,
CD_CENTRO_RESPONSABILITA,
DS_CDR, CD_PROPRIO_CDR,
CD_RESPONSABILE_CDR,
INDIRIZZO,
CD_CDR_AFFERENZA,
STATO_PIANO, ANNOTAZIONI,
FL_RIBALTATO_SU_AREA,
CD_LINEA_ATTIVITA,
TI_APPARTENENZA,
TI_GESTIONE,
CD_ELEMENTO_VOCE,
PG_SPESA,
DT_REGISTRAZIONE,
DESCRIZIONE,
STATO,
ORIGINE,
CATEGORIA_DETTAGLIO,
FL_SOLA_LETTURA,
CD_CENTRO_RESPONSABILITA_CLGE,
CD_LINEA_ATTIVITA_CLGE,
TI_APPARTENENZA_CLGE,
TI_GESTIONE_CLGE,
CD_ELEMENTO_VOCE_CLGE,
PG_ENTRATA_CLGE,
IM_RH_CCS_COSTI, IM_RI_CCS_SPESE_ODC, IM_RJ_CCS_SPESE_ODC_ALTRA_UO,
IM_RK_CCS_SPESE_OGC, IM_RL_CCS_SPESE_OGC_ALTRA_UO, IM_RM_CSS_AMMORTAMENTI, IM_RN_CSS_RIMANENZE,
IM_RO_CSS_ALTRI_COSTI, IM_RP_CSS_VERSO_ALTRO_CDR, IM_RQ_SSC_COSTI_ODC, IM_RR_SSC_COSTI_ODC_ALTRA_UO,
IM_RS_SSC_COSTI_OGC, IM_RT_SSC_COSTI_OGC_ALTRA_UO, IM_RU_SPESE_COSTI_ALTRUI, IM_RV_PAGAMENTI,
IM_RAA_A2_COSTI_FINALI, IM_RAB_A2_COSTI_ALTRO_CDR, IM_RAC_A2_SPESE_ODC, IM_RAD_A2_SPESE_ODC_ALTRA_UO,
IM_RAE_A2_SPESE_OGC, IM_RAF_A2_SPESE_OGC_ALTRA_UO, IM_RAG_A2_SPESE_COSTI_ALTRUI, IM_RAH_A3_COSTI_FINALI,
IM_RAI_A3_COSTI_ALTRO_CDR, IM_RAL_A3_SPESE_ODC, IM_RAM_A3_SPESE_ODC_ALTRA_UO, IM_RAN_A3_SPESE_OGC,
IM_RAO_A3_SPESE_OGC_ALTRA_UO, IM_RAP_A3_SPESE_COSTI_ALTRUI,
0,
0,
0,
0,
0,
0,
0,
CD_CENTRO_RESPONSABILITA_CLGS,
CD_LINEA_ATTIVITA_CLGS,
TI_APPARTENENZA_CLGS,
TI_GESTIONE_CLGS,
CD_ELEMENTO_VOCE_CLGS,
PG_SPESA_CLGS,
FL_LIMITE_ASS_OBBLIG,
FL_VOCE_PERSONALE,
CD_PARTE,
DS_ELEMENTO_VOCE,
CD_TITOLO,
DS_TITOLO,
CD_CAPOCONTO,
DS_CAPOCONTO,
CD_TIPO_LINEA_ATTIVITA,
DENOMINAZIONE,
CD_GRUPPO_LINEA_ATTIVITA,
CD_FUNZIONE,
CD_NATURA,
DS_LINEA_ATTIVITA,
CD_CDR_COLLEGATO,
CD_LA_COLLEGATO,
DS_FUNZIONE,
DS_NATURA,
CD_PROGETTO,
DS_PROGETTO
from vp_dpdg_spe_cdr
union all
select
ESERCIZIO,
CD_CDS,
DS_CDS,
CD_TIPO_UNITA,
CD_RESPONSABILE_CDS,
CD_RESPONSABILE_AMM_CDS,
CD_UNITA_ORGANIZZATIVA,
DS_UNITA_ORGANIZZATIVA,
FL_UO_CDS,
FL_RUBRICA,
FL_PRESIDENTE_AREA,
CD_AREA_RICERCA,
CD_CENTRO_RESPONSABILITA,
DS_CDR, CD_PROPRIO_CDR,
CD_RESPONSABILE_CDR,
INDIRIZZO,
CD_CDR_AFFERENZA,
STATO_PIANO, ANNOTAZIONI,
FL_RIBALTATO_SU_AREA,
CD_LINEA_ATTIVITA,
TI_APPARTENENZA,
TI_GESTIONE,
CD_ELEMENTO_VOCE,
PG_ENTRATA,
DT_REGISTRAZIONE,
DESCRIZIONE,
STATO,
ORIGINE,
CATEGORIA_DETTAGLIO,
FL_SOLA_LETTURA,
null,
null,
null,
null,
null,
0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,
IM_RA_RCE, IM_RB_RSE, IM_RC_ESR,
IM_RD_A2_RICAVI, IM_RE_A2_ENTRATE, IM_RF_A3_RICAVI, IM_RG_A3_ENTRATE,
CD_CENTRO_RESPONSABILITA_CLGS,
CD_LINEA_ATTIVITA_CLGS,
TI_APPARTENENZA_CLGS,
TI_GESTIONE_CLGS,
CD_ELEMENTO_VOCE_CLGS,
PG_SPESA_CLGS,
FL_LIMITE_ASS_OBBLIG,
FL_VOCE_PERSONALE,
CD_PARTE,
DS_ELEMENTO_VOCE,
CD_TITOLO,
DS_TITOLO,
CD_CAPOCONTO,
DS_CAPOCONTO,
CD_TIPO_LINEA_ATTIVITA,
DENOMINAZIONE,
CD_GRUPPO_LINEA_ATTIVITA,
CD_FUNZIONE,
CD_NATURA,
DS_LINEA_ATTIVITA,
CD_CDR_COLLEGATO,
CD_LA_COLLEGATO,
DS_FUNZIONE,
DS_NATURA,
CD_PROGETTO,
DS_PROGETTO
from vp_dpdg_etr_cdr
;

   COMMENT ON TABLE "VP_DPDG_CDR"  IS 'Vista di stampa dei dettagli pdg CDR: legge parte spesa ed entrata dalle viste da cui dipende';
