CREATE OR REPLACE package CNRSTO035 as
--
-- CNRSTO050 - Package per la gestione dello storico OBBLIGAZIONE
-- Date: 14/07/2006
-- Version: 1.5
--
--
-- Dependency:
--
-- History:
-- Date: 02/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 23/04/2002
-- Version: 1.1
-- L'obbligazione scarica su storico la versione precedente alla modifica
--
-- Date: 24/04/2002
-- Version: 1.2
-- L'obbligazione scarica su storico la versione precedente alla modifica
-- Fix errore di table mutating anche in Before Update trigger
--
-- Date: 10/06/2003
-- Version: 1.3
-- Modificato metodo di insert su obbligazione_scadenzario_s
--
-- Date: 12/01/2006
-- Version: 1.4
-- Gestione Residui - Modificato metodo di insert su obbligazione per l'aggiunta del campo
-- ESERCIZIO_ORIGINALE
--
-- Date: 14/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 21/04/2008
-- Version: 1.4
-- Aggiunto il campo FL_NETTO_SOSPESO nella procedura sto_OBBLIGAZIONE
--
-- Constants:


-- Functions Procedures:

 -- Procedura di scarico dell'obbligazione sullo storico
 -- La procedura non effettuan controlli
 --
 -- aObb deve contenere un rowtype completo di obbligazione (= record :new)
 -- aOldObb deve contenere un rowtype completo di obbligazione (= record :old)

 procedure scaricaSuStorico(aDesc varchar2, aOldObb obbligazione%rowtype, aObb obbligazione%rowtype);

 -- Procedure di inserimento nelle tabella di storico dell'obbligazione
 -- Viene chiamata da trigger BU_OBBLIGAZIONE

 procedure sto_OBBLIGAZIONE (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE%rowtype);
 procedure sto_OBBLIGAZIONE_SCADENZARIO (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE_SCADENZARIO%rowtype);
 procedure sto_OBBLIGAZIONE_SCAD_VOCE (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE_SCAD_VOCE%rowtype);
 procedure sto_OBBLIGAZIONE_PGIRO_MODIF (aPgStorico number, imp_iniziale NUMBER, imp_variazione NUMBER,aDest OBBLIGAZIONE%rowtype);

end;
/


CREATE OR REPLACE package body         CNRSTO035 is
 procedure scaricaSuStorico(aDesc varchar2, aOldObb obbligazione%rowtype, aObb obbligazione%rowtype) is
   imp_variazione		number(15,2);
   imp_iniziale     number(15,2);
 begin
  sto_OBBLIGAZIONE (aObb.pg_ver_rec, aDesc, aOldObb);

	 if ((aOldObb.CD_TIPO_DOCUMENTO_CONT = 'IMP_RES' or aOldObb.CD_TIPO_DOCUMENTO_CONT = 'OBB_PGIR_R') and aOldObb.FL_PGIRO ='Y'
	     and ((aOldObb.cd_elemento_voce !=aObb.cd_elemento_voce ) or ( aOldObb.im_obbligazione != aObb.im_obbligazione))) then
	     imp_variazione :=aObb.im_obbligazione-aOldObb.IM_OBBLIGAZIONE;
	     begin
	     -- il valore ? sempre quello originale dell'impegno
	     select im_ORIGINE into imp_iniziale
	     from OBBLIGAZIONE_PGIRO_MODIFICA
	 			where
	 		  cd_cds 	  	        = aOldObb.CD_CDS            	and
	   	  esercizio 	   		  = aOldObb.ESERCIZIO						and
     	  esercizio_originale = aOldObb.ESERCIZIO_ORIGINALE and
	   	  pg_obbligazione     = aOldObb.PG_OBBLIGAZIONE     and
	   	  rownum = 1;
	   	exception when no_data_found then
	   	   imp_iniziale :=aOldObb.im_obbligazione;
	   	end;
	     sto_OBBLIGAZIONE_PGIRO_MODIF(aObb.pg_ver_rec, imp_iniziale,imp_variazione, aOldObb);
	 end if;

  for aScad in (select * from obbligazione_scadenzario where
       cd_cds = aOldObb.cd_cds
   and esercizio = aOldObb.esercizio
   and esercizio_originale = aOldObb.esercizio_originale
   and pg_obbligazione = aOldObb.pg_obbligazione) loop
   sto_OBBLIGAZIONE_SCADENZARIO (aObb.pg_ver_rec, aDesc, aScad);
  end loop;
  for aDett in (select * from obbligazione_scad_voce where
       cd_cds = aOldObb.cd_cds
   and esercizio = aOldObb.esercizio
   and esercizio_originale = aOldObb.esercizio_originale
   and pg_obbligazione = aOldObb.pg_obbligazione
  ) loop
   sto_OBBLIGAZIONE_SCAD_VOCE (aObb.pg_ver_rec, aDesc, aDett);
  end loop;
 end;

 procedure sto_OBBLIGAZIONE (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE%rowtype) is
  begin
   insert into OBBLIGAZIONE_S (
     pg_storico_
    ,ds_storico_
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,CD_TIPO_OBBLIGAZIONE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,DT_REGISTRAZIONE
    ,DS_OBBLIGAZIONE
    ,NOTE_OBBLIGAZIONE
    ,CD_TERZO
    ,IM_OBBLIGAZIONE
    ,IM_COSTI_ANTICIPATI
    ,ESERCIZIO_COMPETENZA
    ,STATO_OBBLIGAZIONE
    ,DT_CANCELLAZIONE
    ,CD_RIFERIMENTO_CONTRATTO
    ,DT_SCADENZA_CONTRATTO
    ,FL_CALCOLO_AUTOMATICO
    ,CD_FONDO_RICERCA
    ,FL_SPESE_COSTI_ALTRUI
    ,FL_PGIRO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,RIPORTATO
    ,CD_CDS_ORI_RIPORTO
    ,ESERCIZIO_ORI_RIPORTO
    ,ESERCIZIO_ORI_ORI_RIPORTO
    ,PG_OBBLIGAZIONE_ORI_RIPORTO
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,CD_TIPO_DOCUMENTO_CONT
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO_CONTRATTO
    ,STATO_CONTRATTO
    ,PG_CONTRATTO
    ,ESERCIZIO_REP
    ,PG_REPERTORIO
    ,MOTIVAZIONE
    ,FL_NETTO_SOSPESO
    ,FL_GARA_IN_CORSO
    ,DS_GARA_IN_CORSO
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.CD_TIPO_OBBLIGAZIONE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DS_OBBLIGAZIONE
    ,aDest.NOTE_OBBLIGAZIONE
    ,aDest.CD_TERZO
    ,aDest.IM_OBBLIGAZIONE
    ,aDest.IM_COSTI_ANTICIPATI
    ,aDest.ESERCIZIO_COMPETENZA
    ,aDest.STATO_OBBLIGAZIONE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_RIFERIMENTO_CONTRATTO
    ,aDest.DT_SCADENZA_CONTRATTO
    ,aDest.FL_CALCOLO_AUTOMATICO
    ,aDest.CD_FONDO_RICERCA
    ,aDest.FL_SPESE_COSTI_ALTRUI
    ,aDest.FL_PGIRO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.RIPORTATO
    ,aDest.CD_CDS_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_RIPORTO
    ,aDest.ESERCIZIO_ORI_ORI_RIPORTO
    ,aDest.PG_OBBLIGAZIONE_ORI_RIPORTO
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO_CONTRATTO
    ,aDest.STATO_CONTRATTO
    ,aDest.PG_CONTRATTO
    ,aDest.ESERCIZIO_REP
    ,aDest.PG_REPERTORIO
    ,aDest.MOTIVAZIONE
    ,aDest.FL_NETTO_SOSPESO
    ,aDest.FL_GARA_IN_CORSO
    ,aDest.DS_GARA_IN_CORSO
    );
 end;
 procedure sto_OBBLIGAZIONE_SCADENZARIO (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE_SCADENZARIO%rowtype) is
  begin
   insert into OBBLIGAZIONE_SCADENZARIO_S (
     pg_storico_
    ,ds_storico_
    ,PG_OBBL_SCAD_ORI_RIPORTO
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,DT_SCADENZA
    ,DS_SCADENZA
    ,IM_SCADENZA
    ,IM_ASSOCIATO_DOC_AMM
    ,IM_ASSOCIATO_DOC_CONTABILE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.PG_OBBL_SCAD_ORI_RIPORTO
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.DT_SCADENZA
    ,aDest.DS_SCADENZA
    ,aDest.IM_SCADENZA
    ,aDest.IM_ASSOCIATO_DOC_AMM
    ,aDest.IM_ASSOCIATO_DOC_CONTABILE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure sto_OBBLIGAZIONE_SCAD_VOCE (aPgStorico number, aDsStorico varchar2, aDest OBBLIGAZIONE_SCAD_VOCE%rowtype) is
  begin
   insert into OBBLIGAZIONE_SCAD_VOCE_S (
     pg_storico_
    ,ds_storico_
    ,CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_ORIGINALE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_VOCE
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,IM_VOCE
    ,CD_FONDO_RICERCA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.ESERCIZIO_ORIGINALE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_VOCE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.IM_VOCE
    ,aDest.CD_FONDO_RICERCA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure sto_OBBLIGAZIONE_PGIRO_MODIF(aPgStorico number, imp_iniziale number,imp_variazione number, aDest OBBLIGAZIONE%rowtype) is
  begin
   insert into OBBLIGAZIONE_PGIRO_MODIFICA(
	  CD_CDS,
		ESERCIZIO,
		PG_OBBLIGAZIONE,
		ESERCIZIO_ORIGINALE,
		IM_ORIGINE,
		IM_VARIAZIONE,
		CD_ELEMENTO_VOCE,
		DACR,
		UTCR,
		DUVA,
		UTUV,
		PG_VER_REC,
		PG_STORICO_
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORIGINALE
    ,imp_iniziale
    ,imp_variazione
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aPgStorico
    );
 end;
end;
/


