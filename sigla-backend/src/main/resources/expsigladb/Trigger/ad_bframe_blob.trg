CREATE OR REPLACE TRIGGER AD_BFRAME_BLOB
AFTER DELETE
on BFRAME_BLOB
for each row
declare
 aBlob BFRAME_BLOB%rowtype;
begin
--
-- Trigger attivato su aggiornamento di BFRAME_BLOB
--
-- Date: 13/01/2003
-- Version: 1.1
--
-- Dependency: IBMERR001
--
-- History:
--
-- Date: 04/06/2003
-- Version: 1.0
--
-- Date: 13/01/2003
-- Version: 1.1
-- Aggiunti nuovi campi
--
-- Body:
     aBlob.CD_TIPO:=:old.CD_TIPO;
     aBlob.PATH:=:old.PATH;
     aBlob.FILENAME:=:old.FILENAME;
     aBlob.BDATA:=:old.BDATA;
     aBlob.CDATA:=:old.CDATA;
     aBlob.DACR:=:old.DACR;
     aBlob.DUVA:=:old.DUVA;
     aBlob.UTUV:=:old.UTUV;
     aBlob.UTCR:=:old.UTCR;
     aBlob.PG_VER_REC:=:old.PG_VER_REC;
     aBlob.STATO:=:old.STATO;
	 aBlob.DS_FILE:=:old.DS_FILE;
	 aBlob.DS_UTENTE:=:old.DS_UTENTE;
	 aBlob.TI_VISIBILITA:=:old.TI_VISIBILITA;
	 IBMUTL005.deleteCallback(aBlob);
end;
/


