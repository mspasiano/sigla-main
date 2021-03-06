--------------------------------------------------------
--  DDL for View V_CONS_PDG_ETR_X_AREA_DET
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_PDG_ETR_X_AREA_DET" ("ESERCIZIO", "CD_CDS_AREA", "DS_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_CLASSIFICAZIONE", "DS_CLASSIFICAZIONE", "NR_LIVELLO", "CD_LIVELLO1", "CD_LIVELLO2", "CD_LIVELLO3", "CD_LIVELLO4", "CD_LIVELLO5", "CD_LIVELLO6", "CD_LIVELLO7", "PG_MODULO", "CD_MODULO", "DS_MODULO", "PG_COMMESSA", "CD_COMMESSA", "DS_COMMESSA", "PG_PROGETTO", "CD_PROGETTO", "DS_PROGETTO", "CD_TERZO_FINANZIATORE", "DS_DETTAGLIO", "TOT_FINANZIAMENTO", "IM_ENTRATA", "IM_ENTRATA_A2", "IM_ENTRATA_A3") AS 
  SELECT
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista CONSULTAZIONE Piano di Gestione Entrate per Area
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
PDG.ESERCIZIO, PDG.CD_CDS_AREA, AREA.DS_UNITA_ORGANIZZATIVA,
       PDG.CD_CENTRO_RESPONSABILITA, CDR.DS_CDR,
       CD_CLASSIFICAZIONE, DS_CLASSIFICAZIONE, NR_LIVELLO,
       CD_LIVELLO1, CD_LIVELLO2, CD_LIVELLO3, CD_LIVELLO4, CD_LIVELLO5,
       CD_LIVELLO6, CD_LIVELLO7,
       MODU.PG_PROGETTO PG_MODULO, MODU.CD_PROGETTO CD_MODULO, MODU.DS_PROGETTO DS_MODULO,
       COMM.PG_PROGETTO PG_COMMESSA, COMM.CD_PROGETTO CD_COMMESSA, COMM.DS_PROGETTO DS_COMMESSA,
       PROG.PG_PROGETTO, PROG.CD_PROGETTO, PROG.DS_PROGETTO,
       CD_TERZO CD_TERZO_FINANZIATORE,
       DS_DETTAGLIO,
       IM_ENTRATA_TOT TOT_FINANZIAMENTO,
       IM_ENTRATA,
       IM_ENTRATA_A2,
       IM_ENTRATA_A3
FROM   PDG_MODULO_ENTRATE PDG, V_CLASSIFICAZIONE_VOCI CLA, UNITA_ORGANIZZATIVA AREA,
       CDR, PROGETTO_PREV MODU, PROGETTO_PREV COMM, PROGETTO_PREV PROG
WHERE  PDG.ID_CLASSIFICAZIONE = CLA.ID_CLASSIFICAZIONE AND
       PDG.CD_CDS_AREA = AREA.CD_UNITA_ORGANIZZATIVA AND
       PDG.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA AND
       PDG.PG_PROGETTO = MODU.PG_PROGETTO And
       MODU.ESERCIZIO = PDG.ESERCIZIO And
       MODU.ESERCIZIO_PROGETTO_PADRE = COMM.ESERCIZIO And
       MODU.PG_PROGETTO_PADRE = COMM.PG_PROGETTO And
       COMM.ESERCIZIO_PROGETTO_PADRE = PROG.ESERCIZIO And
       COMM.PG_PROGETTO_PADRE = PROG.PG_PROGETTO And
       AREA.CD_TIPO_UNITA = 'AREA';
