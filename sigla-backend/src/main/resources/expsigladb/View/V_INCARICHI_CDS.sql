--------------------------------------------------------
--  DDL for View V_INCARICHI_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INCARICHI_CDS" ("ESERCIZIO_LIMITE", "CD_CDS", "DS_CDS", "CD_TIPO_LIMITE", "IM_INCARICHI") AS 
  (SELECT
--
-- Date: 15/09/2008
-- Version: 1.1
--
-- Vista Incarichi per CDS utilizzata per conoscere, dato il codice limite
-- la ripartizione tra i vari CDS degli incarichi conferiti
--
-- History:
--
-- Date: 10/01/2008
-- Version: 1.0
-- Creazione
--
-- Date: 15/09/2008
-- Version: 1.1
-- Aggiornata tenedo conto della nuova struttura degli incarichi che prevedono la nuova tabella
-- INCARICHI_PROCEDURA
--
-- Body:
--
  INCARICHI_PROCEDURA_ANNO.ESERCIZIO_LIMITE,
  INCARICHI_PROCEDURA.CD_CDS,
  CDS.DS_UNITA_ORGANIZZATIVA,
  V_ASS_INCARICO_ATTIVITA_REPLIM.CD_TIPO_LIMITE,
  Sum(INCARICHI_PROCEDURA_ANNO.IMPORTO_COMPLESSIVO) IM_INCARICHI
From INCARICHI_PROCEDURA, INCARICHI_PROCEDURA_ANNO, V_ASS_INCARICO_ATTIVITA_REPLIM, UNITA_ORGANIZZATIVA CDS
Where INCARICHI_PROCEDURA.stato in ('PP','PU','PD', 'CC')
And   INCARICHI_PROCEDURA.ESERCIZIO = INCARICHI_PROCEDURA_ANNO.ESERCIZIO
And   INCARICHI_PROCEDURA.PG_PROCEDURA = INCARICHI_PROCEDURA_ANNO.PG_PROCEDURA
And   INCARICHI_PROCEDURA.CD_CDS = CDS.CD_UNITA_ORGANIZZATIVA
And   V_ASS_INCARICO_ATTIVITA_REPLIM.ESERCIZIO = INCARICHI_PROCEDURA.ESERCIZIO
And   V_ASS_INCARICO_ATTIVITA_REPLIM.ESERCIZIO_LIMITE = INCARICHI_PROCEDURA_ANNO.ESERCIZIO_LIMITE
And   V_ASS_INCARICO_ATTIVITA_REPLIM.CD_TIPO_INCARICO = INCARICHI_PROCEDURA.CD_TIPO_INCARICO
And   V_ASS_INCARICO_ATTIVITA_REPLIM.CD_TIPO_ATTIVITA = INCARICHI_PROCEDURA.CD_TIPO_ATTIVITA
And   V_ASS_INCARICO_ATTIVITA_REPLIM.TIPO_NATURA = INCARICHI_PROCEDURA.TIPO_NATURA
Group By INCARICHI_PROCEDURA_ANNO.ESERCIZIO_LIMITE,
         INCARICHI_PROCEDURA.CD_CDS,
         CDS.DS_UNITA_ORGANIZZATIVA,
         V_ASS_INCARICO_ATTIVITA_REPLIM.CD_TIPO_LIMITE);