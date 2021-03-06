CREATE OR REPLACE procedure         pop_storico_indicatore (DA_DATA        IN date,
 			A_DATA         IN date)
 is
begin
  Declare
  Cursor c Is
  -- fatture pagate nel periodo non legate a compenso
  SELECT   f.esercizio,f.CD_UO_ORIGINE uo,f.PG_FATTURA_PASSIVA pg_doc,'FATTURA_P' tipo_doc,sum(decode(f.ti_fattura,'C',(fr.im_imponibile+fr.im_iva)*-1,(fr.im_imponibile+fr.im_iva))) tot,
   nvl(f.dt_scadenza,f.dt_fattura_fornitore+30) dt_scadenza,trunc(dt_trasmissione) dt_trasmissione,
                       (mr.im_mandato_riga) tot_pagato,((TRUNC (m.dt_trasmissione) - nvl(f.dt_scadenza,f.dt_fattura_fornitore+30))* mr.im_mandato_riga) tot_pesato,
                       a.cd_anag,f.cd_terzo,os.esercizio esercizio_obb,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                          ,c.esercizio esercizio_contratto,c.pg_contratto,null esercizio_inc,null pg_incarico
                 FROM contratto c,
                      obbligazione o,
                      obbligazione_scadenzario os,
                      fattura_passiva f,fattura_passiva_riga fr,
                      mandato_riga mr,
                      mandato m,
                      anagrafico a,terzo t
                WHERE a.cd_anag=t.cd_anag and t.cd_terzo=f.cd_terzo and
                      c.esercizio(+) = o.esercizio_contratto
                  AND c.stato(+) = o.stato_contratto
                  AND c.pg_contratto(+) = o.pg_contratto
                  AND (   c.cd_tipo_contratto NOT IN
                             ('AL', 'CONL', 'CSPP', 'QUOTE', 'COND', 'COMPR',
                              'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE', 'LOC',
                              'TRASF', 'INTP')
                      OR (o.esercizio_contratto IS NULL )
                      )
                  AND o.esercizio = os.esercizio
                  AND o.esercizio_originale = os.esercizio_originale
                  AND o.cd_cds = os.cd_cds
                  AND o.pg_obbligazione = os.pg_obbligazione
                  AND fr.esercizio = f.esercizio
                  AND fr.cd_cds = f.cd_cds
                  AND fr.cd_unita_organizzativa = f.cd_unita_organizzativa
                  AND fr.pg_fattura_passiva = f.pg_fattura_passiva
                  and f.fl_fattura_compenso = 'N'
                  AND os.esercizio = fr.esercizio_obbligazione
                  AND os.esercizio_originale = fr.esercizio_ori_obbligazione
                  AND os.cd_cds = fr.cd_cds_obbligazione
                  AND os.pg_obbligazione = fr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = fr.pg_obbligazione_scadenzario
                  AND os.esercizio = mr.esercizio_obbligazione
                  AND os.cd_cds = mr.cd_cds
                  AND os.esercizio_originale = mr.esercizio_ori_obbligazione
                  AND os.pg_obbligazione = mr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
                  AND mr.cd_cds = m.cd_cds
                  AND mr.esercizio = m.esercizio
                  AND mr.pg_mandato = m.pg_mandato
                  AND m.stato != 'A'
                  AND f.stato_cofi != 'A'
                  AND m.ti_mandato in( 'P')
                  and mr.cd_cds_doc_amm=f.cd_cds_origine
                  and mr.CD_UO_DOC_AMM=f.cd_uo_origine
                  and mr.esercizio_doc_amm=f.esercizio
                  and mr.pg_doc_amm = f.pg_fattura_passiva
                  AND mr.cd_tipo_documento_amm = 'FATTURA_P'
                  AND m.dt_trasmissione IS NOT NULL
                  and trunc(dt_trasmissione) between da_data and a_data
                  and STATO_PAGAMENTO_FONDO_ECO ='N'
                  --AND decode(f.ti_fattura,'F',NVL (f.stato_liquidazione, 'LIQ'),'LIQ') = 'LIQ'
                  AND NVL (f.stato_liquidazione, 'LIQ') = 'LIQ'
                  group by
                                  f.esercizio,f.CD_UO_ORIGINE,f.PG_FATTURA_PASSIVA,'FATTURA_P',nvl(f.dt_scadenza,f.dt_fattura_fornitore+30),
                                  trunc(m.dt_trasmissione),mr.im_mandato_riga,a.cd_anag,f.cd_terzo,
                                  os.esercizio,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                                  ,c.esercizio ,c.pg_contratto,null ,null
          -- fatture scadute liquidabili non pagate non collegate a compenso
       UNION ALL
                   select f.esercizio,f.CD_UO_ORIGINE uo,f.PG_FATTURA_PASSIVA pg_doc,'FATTURA_P'  tipo_doc,
                   sum(decode(f.ti_fattura,'C',(fr.im_imponibile+fr.im_iva)*-1,(fr.im_imponibile+fr.im_iva))) tot,
                   nvl(f.dt_scadenza,f.dt_fattura_fornitore+30) dt_scadenza,null dt_trasmissione,
                      0 tot_pagato,0 tot_pesato,a.cd_anag,f.cd_terzo,os.esercizio esercizio_obb,
                      os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                      ,c.esercizio esercizio_contratto,c.pg_contratto,null esercizio_inc,null pg_incarico
                 FROM contratto c,
                      obbligazione o,
                      obbligazione_scadenzario os,
                      fattura_passiva f,fattura_passiva_riga fr,
                      anagrafico a,terzo t
                WHERE a.cd_anag=t.cd_anag and t.cd_terzo=f.cd_terzo and
                      c.esercizio(+) = o.esercizio_contratto
                  AND c.stato(+) = o.stato_contratto
                  AND c.pg_contratto(+) = o.pg_contratto
                  AND (   c.cd_tipo_contratto NOT IN
                             ('AL', 'CONL', 'CSPP', 'QUOTE', 'COND', 'COMPR',
                              'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE', 'LOC',
                              'TRASF', 'INTP')
                    OR (o.esercizio_contratto IS NULL )
                      )
                  AND o.esercizio = os.esercizio
                  AND o.esercizio_originale = os.esercizio_originale
                  AND o.cd_cds = os.cd_cds
                  AND o.pg_obbligazione = os.pg_obbligazione
                   AND fr.esercizio = f.esercizio
                  AND fr.cd_cds = f.cd_cds
                  AND fr.cd_unita_organizzativa = f.cd_unita_organizzativa
                  AND fr.pg_fattura_passiva = f.pg_fattura_passiva
                  and f.fl_fattura_compenso = 'N'
                  AND os.esercizio = fr.esercizio_obbligazione
                  AND os.esercizio_originale = fr.esercizio_ori_obbligazione
                  AND os.cd_cds = fr.cd_cds_obbligazione
                  AND os.pg_obbligazione = fr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = fr.pg_obbligazione_scadenzario
                  and not exists(
                      select 1 from mandato m , mandato_riga mr
                      where
                        os.esercizio = mr.esercizio_obbligazione
                  AND os.cd_cds = mr.cd_cds
                  AND os.esercizio_originale = mr.esercizio_ori_obbligazione
                  AND os.pg_obbligazione = mr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
                  AND mr.cd_cds = m.cd_cds
                  AND mr.esercizio = m.esercizio
                  AND mr.pg_mandato = m.pg_mandato
                  AND m.stato != 'A'
                  --AND m.ti_mandato in( 'P','S')
                  and mr.cd_cds_doc_amm=f.cd_cds_origine
                  and mr.CD_UO_DOC_AMM=f.cd_uo_origine
                  and mr.esercizio_doc_amm=f.esercizio
                  and mr.pg_doc_amm = f.pg_fattura_passiva
                  AND mr.cd_tipo_documento_amm = 'FATTURA_P'
                  AND ((m.ti_mandato !='R' and m.dt_trasmissione  IS NOT NULL  )
                  	OR m.ti_mandato ='R'))
                  AND f.stato_cofi != 'A'
                  and nvl(f.dt_scadenza,f.dt_fattura_fornitore+30) < a_data
                  and STATO_PAGAMENTO_FONDO_ECO ='N'
                  AND decode(f.ti_fattura,'F',NVL (f.stato_liquidazione, 'LIQ'),'LIQ') = 'LIQ'
                  --AND NVL (f.stato_liquidazione, 'LIQ') = 'LIQ'
                  --and ((f.ti_fattura='F' and nvl(fr.im_diponibile_nc,0)!=0 or f.ti_fattura!='F')
                  and nvl(fr.im_diponibile_nc,0)!=0
                  group by
                                 f.esercizio,f.CD_UO_ORIGINE,f.PG_FATTURA_PASSIVA,'FATTURA_P',
                                 nvl(f.dt_scadenza,f.dt_fattura_fornitore+30),null,0,a.cd_anag,f.cd_terzo,os.esercizio,
                                 os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                                 ,c.esercizio,c.pg_contratto,null,null
         union all
         -- fatture legati a compensi pagate nel periodo sia con contratto che con incarico
          select comp.esercizio,comp.CD_unita_organizzativa uo,comp.pg_compenso pg_doc,'COMPENSO'  tipo_doc,sum(comp.im_totale_compenso) tot,
          comp.dt_scadenza,trunc(m.dt_trasmissione) dt_trasmissione,
           (mr.im_mandato_riga) tot_pagato, ((TRUNC (m.dt_trasmissione) - comp.dt_scadenza)* mr.im_mandato_riga) tot_pesato,
           	a.cd_anag,f.cd_terzo,os.esercizio esercizio_obb,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                          ,c.esercizio esercizio_contratto,c.pg_contratto,i.esercizio esercizio_inc,i.pg_repertorio pg_incarico
                 FROM contratto c,
                      obbligazione o,
                      obbligazione_scadenzario os,
                      fattura_passiva f,fattura_passiva_riga fr,
                      mandato_riga mr,
                      mandato m,anagrafico a, terzo t,
                      INCARICHI_REPERTORIO I,
                      compenso comp
                WHERE a.cd_anag = t.cd_anag and t.cd_terzo= f.cd_terzo and
                      c.esercizio(+) = o.esercizio_contratto
                  AND c.stato(+) = o.stato_contratto
                  AND c.pg_contratto(+) = o.pg_contratto
                  AND (   c.cd_tipo_contratto NOT IN
                             ('AL', 'CONL', 'CSPP', 'QUOTE', 'COND', 'COMPR',
                              'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE', 'LOC',
                              'TRASF', 'INTP')
                      OR (o.esercizio_contratto IS NULL )
                      )
                  AND (O.ESERCIZIO_REP = I.ESERCIZIO(+)
                  AND O.PG_REPERTORIO  =I.PG_REPERTORIO(+)
                  AND comp.CD_TIPO_RAPPORTO IN('OCCA','PROF'))
                  AND o.esercizio = os.esercizio
                  AND o.esercizio_originale = os.esercizio_originale
                  AND o.cd_cds = os.cd_cds
                  AND o.pg_obbligazione = os.pg_obbligazione
                  and comp.fl_generata_fattura = 'Y'
                  AND os.esercizio = comp.esercizio_obbligazione
                  AND os.esercizio_originale = comp.esercizio_ori_obbligazione
                  AND os.cd_cds = comp.cd_cds_obbligazione
                  AND os.pg_obbligazione = comp.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = comp.pg_obbligazione_scadenzario
                  AND comp.esercizio = f.esercizio_compenso
                  AND comp.cd_cds = f.cds_compenso
                  AND comp.cd_unita_organizzativa = f.uo_compenso
                  AND comp.pg_compenso = f.pg_compenso
                  AND fr.esercizio = f.esercizio
                  AND fr.cd_cds = f.cd_cds
                  AND fr.cd_unita_organizzativa = f.cd_unita_organizzativa
                  AND fr.pg_fattura_passiva = f.pg_fattura_passiva
                  and f.fl_fattura_compenso = 'Y'
                  AND os.esercizio = mr.esercizio_obbligazione
                  AND os.cd_cds = mr.cd_cds
                  AND os.esercizio_originale = mr.esercizio_ori_obbligazione
                  AND os.pg_obbligazione = mr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
                  AND mr.cd_cds = m.cd_cds
                  AND mr.esercizio = m.esercizio
                  AND mr.pg_mandato = m.pg_mandato
                  AND m.stato != 'A'
                  AND f.stato_cofi != 'A'
                  AND m.ti_mandato in( 'P')
                  and mr.cd_cds_doc_amm=comp.cd_cds
                  and mr.CD_UO_DOC_AMM=comp.cd_unita_organizzativa
                  and mr.esercizio_doc_amm=comp.esercizio
                  and mr.pg_doc_amm = comp.pg_compenso
                  AND mr.cd_tipo_documento_amm = 'COMPENSO'
                  AND comp.dt_scadenza IS NOT NULL
                  AND m.dt_trasmissione IS NOT NULL
                  and trunc(dt_trasmissione) between da_data and a_data
                  and comp.STATO_PAGAMENTO_FONDO_ECO ='N'
                  AND NVL (comp.stato_liquidazione, 'LIQ') = 'LIQ'
                 group by   comp.esercizio,comp.CD_unita_organizzativa,comp.pg_compenso,'COMPENSO',comp.dt_scadenza,trunc(dt_trasmissione),mr.im_mandato_riga,a.cd_anag,f.cd_terzo,os.esercizio,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                           ,c.esercizio ,c.pg_contratto,i.esercizio ,i.pg_repertorio
                  union all
           -- compensi pagati nel periodo sia con contratto che con incarico non da fattura
                   select comp.esercizio,comp.CD_unita_organizzativa uo,comp.pg_compenso pg_doc,'COMPENSO'  tipo_doc,
                   sum(comp.im_totale_compenso) tot,nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30),trunc(dt_trasmissione) dt_trasmissione,
                       (mr.im_mandato_riga) tot_pagato,
                        ((TRUNC (m.dt_trasmissione) -nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30))* mr.im_mandato_riga) tot_pesato,
                       a.cd_anag,comp.cd_terzo,os.esercizio esercizio_obb,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                    ,c.esercizio esercizio_contratto,c.pg_contratto,i.esercizio esercizio_inc,i.pg_repertorio pg_incarico
                 FROM contratto c,
                      obbligazione o,
                      obbligazione_scadenzario os,
                      mandato_riga mr,
                      mandato m,
                      INCARICHI_REPERTORIO I,
                      compenso comp,anagrafico a, terzo t
                WHERE a.cd_anag = t.cd_anag and t.cd_terzo= comp.cd_terzo and
                        c.esercizio(+) = o.esercizio_contratto
                  AND c.stato(+) = o.stato_contratto
                  AND c.pg_contratto(+) = o.pg_contratto
                  AND (   c.cd_tipo_contratto NOT IN
                             ('AL', 'CONL', 'CSPP', 'QUOTE', 'COND', 'COMPR',
                              'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE', 'LOC',
                              'TRASF', 'INTP')
                       OR (o.esercizio_contratto IS NULL )
                      )
                  AND (O.ESERCIZIO_REP = I.ESERCIZIO(+)
                  AND O.PG_REPERTORIO  =I.PG_REPERTORIO(+)
                  AND comp.CD_TIPO_RAPPORTO IN('OCCA','PROF'))
                  and comp.fl_generata_fattura = 'N'
                  AND o.esercizio = os.esercizio
                  AND o.esercizio_originale = os.esercizio_originale
                  AND o.cd_cds = os.cd_cds
                  AND o.pg_obbligazione = os.pg_obbligazione
                  AND os.esercizio = comp.esercizio_obbligazione
                  AND os.esercizio_originale = comp.esercizio_ori_obbligazione
                  AND os.cd_cds = comp.cd_cds_obbligazione
                  AND os.pg_obbligazione = comp.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario= comp.pg_obbligazione_scadenzario
                  AND os.esercizio = mr.esercizio_obbligazione
                  AND os.cd_cds = mr.cd_cds
                  AND os.esercizio_originale = mr.esercizio_ori_obbligazione
                  AND os.pg_obbligazione = mr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
                  AND mr.cd_cds = m.cd_cds
                  AND mr.esercizio = m.esercizio
                  AND mr.pg_mandato = m.pg_mandato
                  AND m.stato != 'A'
                  AND comp.stato_cofi != 'A'
                  AND m.ti_mandato in( 'P')
                  and mr.cd_cds_doc_amm=comp.cd_cds
                  and mr.CD_UO_DOC_AMM=comp.cd_unita_organizzativa
                  and mr.esercizio_doc_amm=comp.esercizio
                  and mr.pg_doc_amm = comp.pg_compenso
                  AND mr.cd_tipo_documento_amm = 'COMPENSO'
                  --AND nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30) is not null
                  and trunc(dt_trasmissione) between da_data and a_data
                  AND m.dt_trasmissione IS NOT NULL
                  and comp.STATO_PAGAMENTO_FONDO_ECO ='N'
                  AND NVL (comp.stato_liquidazione, 'LIQ') = 'LIQ'
                    group by
                    comp.esercizio,comp.CD_unita_organizzativa,comp.pg_compenso,'COMPENSO',nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30),trunc(dt_trasmissione),
                  mr.im_mandato_riga,a.cd_anag,comp.cd_terzo,os.esercizio,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                  ,c.esercizio ,c.pg_contratto,i.esercizio ,i.pg_repertorio ,comp.dt_registrazione
           union all
          -- compensi non pagati sia con contratto che con incarico non da fattura
                   select comp.esercizio,comp.CD_unita_organizzativa uo,comp.pg_compenso pg_doc,'COMPENSO'  tipo_doc,
						sum(comp.im_totale_compenso) tot,nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30),null dt_trasmissione,
                      0 tot_pagato,0 tot_pesato,a.cd_anag,comp.cd_terzo,os.esercizio esercizio_obb,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                      ,c.esercizio esercizio_contratto,c.pg_contratto,i.esercizio esercizio_inc,i.pg_repertorio pg_incarico
                 FROM contratto c,
                      obbligazione o,
                      obbligazione_scadenzario os,
                      INCARICHI_REPERTORIO I,
                      compenso comp,anagrafico a,terzo t
                WHERE a.cd_anag = t.cd_anag and t.cd_terzo= comp.cd_terzo and
                        c.esercizio(+) = o.esercizio_contratto
                  AND c.stato(+) = o.stato_contratto
                  AND c.pg_contratto(+) = o.pg_contratto
                  AND (   c.cd_tipo_contratto NOT IN
                             ('AL', 'CONL', 'CSPP', 'QUOTE', 'COND', 'COMPR',
                              'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE', 'LOC',
                              'TRASF', 'INTP')
                      OR (o.esercizio_contratto IS NULL )
                      )
                   AND (O.ESERCIZIO_REP = I.ESERCIZIO(+)
                  AND O.PG_REPERTORIO  =I.PG_REPERTORIO(+)
                  AND comp.CD_TIPO_RAPPORTO IN('OCCA','PROF'))
                  and comp.fl_generata_fattura = 'N'
                  AND o.esercizio = os.esercizio
                  AND o.esercizio_originale = os.esercizio_originale
                  AND o.cd_cds = os.cd_cds
                  AND o.pg_obbligazione = os.pg_obbligazione
                  AND os.esercizio = comp.esercizio_obbligazione
                  AND os.esercizio_originale = comp.esercizio_ori_obbligazione
                  AND os.cd_cds = comp.cd_cds_obbligazione
                  AND os.pg_obbligazione = comp.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario= comp.pg_obbligazione_scadenzario
                  and not exists(
                  select 1 from mandato m , mandato_riga mr
                      where
                        os.esercizio = mr.esercizio_obbligazione
                  AND os.cd_cds = mr.cd_cds
                  AND os.esercizio_originale = mr.esercizio_ori_obbligazione
                  AND os.pg_obbligazione = mr.pg_obbligazione
                  AND os.pg_obbligazione_scadenzario = mr.pg_obbligazione_scadenzario
                  AND mr.cd_cds = m.cd_cds
                  AND mr.esercizio = m.esercizio
                  AND mr.pg_mandato = m.pg_mandato
                  AND m.stato != 'A'
                  --AND m.ti_mandato in( 'P','S')
                  and mr.cd_cds_doc_amm=comp.cd_cds
                  and mr.CD_UO_DOC_AMM=comp.cd_unita_organizzativa
                  and mr.esercizio_doc_amm=comp.esercizio
                  and mr.pg_doc_amm = comp.pg_compenso
                  AND mr.cd_tipo_documento_amm = 'COMPENSO'
                  AND m.dt_trasmissione IS NOT NULL     )
                  AND comp.stato_cofi != 'A'
                  AND nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30)  < a_data
                  and comp.STATO_PAGAMENTO_FONDO_ECO ='N'
                  AND NVL (comp.stato_liquidazione, 'LIQ') = 'LIQ'
                    group by
                    comp.esercizio,comp.CD_unita_organizzativa,comp.pg_compenso,'COMPENSO',nvl(comp.dt_scadenza,trunc(comp.dt_registrazione)+30),null,
                  0,a.cd_anag,comp.cd_terzo,os.esercizio,os.cd_cds,os.pg_obbligazione,os.pg_obbligazione_scadenzario,os.esercizio_originale
                  ,c.esercizio ,c.pg_contratto,i.esercizio ,i.pg_repertorio;
cur c%Rowtype;
prog NUMBER:=0;
Begin
select nvl(max(pg_estrazione),0)+1 into prog
  from storico_indicatore;
Open c;
Loop
  Fetch c Into cur;
  Exit When c%Notfound;
      Insert into storico_indicatore (esercizio,periodo_da,periodo_a,data_estr,pg_estrazione ,esercizio_doc ,cd_unita_organizzativa ,tipo_documento ,
					pg_documento ,importo ,importo_pesato ,importo_pagato ,
					dt_scadenza ,dt_trasmissione , cd_anag ,cd_terzo  ,cd_cds_obb	,ESERCIZIO_obb	,
					PG_OBBLIGAZIONE,PG_OBBLIGAZIONE_SCADENZARIO,ESERCIZIO_ORIGINALE ,
					esercizio_contratto , pg_contratto,esercizio_incarico,pg_incarico )
			values(to_char(da_data,'yyyy'),da_data,a_data,sysdate,prog,cur.esercizio,cur.uo,cur.tipo_doc,
			      cur.pg_doc,cur.tot,cur.tot_pesato,cur.tot_pagato,
			      cur.dt_scadenza, cur.dt_trasmissione,cur.cd_anag,cur.cd_terzo,cur.cd_cds,cur.esercizio_obb,
			      cur.pg_obbligazione,cur.PG_OBBLIGAZIONE_SCADENZARIO,cur.esercizio_originale,
						cur.esercizio_contratto,cur.pg_contratto,	cur.esercizio_inc,cur.pg_incarico);
end loop;
close c;
end;
end;
/


