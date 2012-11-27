update LIST_OBJECT set OBJECT_OWNER = '8a4d928134a3d1fb0134a959b57b05cc' where LIST_OBJECT_ID = '8a4d92b2370b19fa013710a585cc4b88';

update CLAUSE set DECISION_ID = null where NAME like 'NQF0111a%';
delete from CLAUSE where NAME like 'NQF0111a%';