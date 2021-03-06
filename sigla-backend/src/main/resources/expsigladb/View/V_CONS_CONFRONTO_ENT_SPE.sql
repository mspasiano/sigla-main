--------------------------------------------------------
--  DDL for View V_CONS_CONFRONTO_ENT_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_CONFRONTO_ENT_SPE" ("ESERCIZIO", "CDS", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_NATURA", "CD_MODULO", "DS_MODULO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "IM_STANZ_INIZIALE_A1", "VARIAZIONI_PIU", "VARIAZIONI_MENO", "ASSESTATO_COMP", "IM_OBBL_ACC_COMP", "IM_ASS_DOC_AMM_SPE", "IM_ASS_DOC_AMM_ETR", "IM_MANDATI_REVERSALI_PRO", "TRATT_ECON_INT", "TRATT_ECON_EST") AS 
  SELECT  ESERCIZIO, CDS, CD_CENTRO_RESPONSABILITA, DS_CDR, CD_LINEA_ATTIVITA, DENOMINAZIONE, CD_NATURA, CD_MODULO, DS_MODULO,
        TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE,
        SUM(IM_STANZ_INIZIALE_A1), SUM(VARIAZIONI_PIU), SUM(VARIAZIONI_MENO),
        SUM(ASSESTATO_COMP), SUM(IM_OBBL_ACC_COMP), SUM(IM_ASS_DOC_AMM_SPE),
        SUM(IM_ASS_DOC_AMM_ETR), SUM(IM_MANDATI_REVERSALI_PRO), SUM(TRATT_ECON_EST), SUM(TRATT_ECON_INT)
FROM
(SELECT ESERCIZIO, CDS, CD_CENTRO_RESPONSABILITA, DS_CDR,
        CD_LINEA_ATTIVITA, DENOMINAZIONE, CD_NATURA, CD_MODULO, DS_MODULO,
        TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE,
        SUM(IM_STANZ_INIZIALE_A1) IM_STANZ_INIZIALE_A1, SUM(VARIAZIONI_PIU) VARIAZIONI_PIU, SUM(VARIAZIONI_MENO) VARIAZIONI_MENO, SUM(ASSESTATO_COMP) ASSESTATO_COMP,
        SUM(IM_OBBL_ACC_COMP) IM_OBBL_ACC_COMP,
        SUM(IM_ASS_DOC_AMM_SPE) IM_ASS_DOC_AMM_SPE, SUM(IM_ASS_DOC_AMM_ETR) IM_ASS_DOC_AMM_ETR, SUM(IM_MANDATI_REVERSALI_PRO) IM_MANDATI_REVERSALI_PRO,
        SUM(0) TRATT_ECON_EST, SUM(0) TRATT_ECON_INT
 FROM   V_SIT_ANALITICA_GAE_COMPETENZA
 GROUP BY ESERCIZIO, CDS, CD_CENTRO_RESPONSABILITA, DS_CDR,
          CD_LINEA_ATTIVITA, DENOMINAZIONE, CD_NATURA, CD_MODULO, DS_MODULO,
          TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE
 Having SUM(IM_STANZ_INIZIALE_A1) != 0 Or Sum(VARIAZIONI_PIU) != 0 Or Sum(VARIAZIONI_MENO) != 0 Or Sum(ASSESTATO_COMP) != 0 Or
        SUM(IM_OBBL_ACC_COMP) != 0 Or Sum(IM_ASS_DOC_AMM_SPE) != 0 Or Sum(IM_ASS_DOC_AMM_ETR) != 0 Or Sum(IM_MANDATI_REVERSALI_PRO) != 0
UNION ALL
 SELECT ESERCIZIO, CNRUTL001.GETCDSFROMCDR(CD_CDR_ASSEGNATARIO) CDS, CD_CDR_ASSEGNATARIO, DS_CDR,
        V_CONS_PDGG_SPE_BIL_IST_DIP_FO.CD_LINEA_ATTIVITA, DS_LINEA, CD_NATURA, CD_MODULO, DS_MODULO,
        V_CONS_PDGG_SPE_BIL_IST_DIP_FO.TI_APPARTENENZA, V_CONS_PDGG_SPE_BIL_IST_DIP_FO.TI_GESTIONE,
        CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE,
        SUM(0) IM_STANZ_INIZIALE_A1, SUM(0) VARIAZIONI_PIU, SUM(0) VARIAZIONI_MENO, SUM(0)ASSESTATO_COMP,
        SUM(0) IM_OBBL_ACC_COMP, SUM(0) IM_ASS_DOC_AMM_SPE, SUM(0) IM_ASS_DOC_AMM_ETR, SUM(0) IM_MANDATI_REVERSALI_PRO,
        SUM(TRATT_ECON_INT) TRATT_ECON_INT, SUM(TRATT_ECON_EST) TRATT_ECON_INT
 FROM   V_CONS_PDGG_SPE_BIL_IST_DIP_FO, LINEA_ATTIVITA LA
 WHERE  V_CONS_PDGG_SPE_BIL_IST_DIP_FO.CD_CDR_ASSEGNATARIO = LA.CD_CENTRO_RESPONSABILITA AND
        V_CONS_PDGG_SPE_BIL_IST_DIP_FO.CD_LINEA_ATTIVITA = LA.CD_LINEA_ATTIVITA
 GROUP BY ESERCIZIO, CNRUTL001.GETCDSFROMCDR(CD_CDR_ASSEGNATARIO), CD_CDR_ASSEGNATARIO, DS_CDR,
        V_CONS_PDGG_SPE_BIL_IST_DIP_FO.CD_LINEA_ATTIVITA, DS_LINEA, CD_NATURA, CD_MODULO, DS_MODULO,
        V_CONS_PDGG_SPE_BIL_IST_DIP_FO.TI_APPARTENENZA, V_CONS_PDGG_SPE_BIL_IST_DIP_FO.TI_GESTIONE, CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE
 Having  SUM(TRATT_ECON_INT) != 0 Or SUM(TRATT_ECON_EST) != 0)
GROUP BY ESERCIZIO, CDS, CD_CENTRO_RESPONSABILITA, DS_CDR, CD_LINEA_ATTIVITA, DENOMINAZIONE, CD_NATURA, CD_MODULO, DS_MODULO,
        TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, DS_ELEMENTO_VOCE
;
