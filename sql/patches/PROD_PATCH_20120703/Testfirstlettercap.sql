UPDATE `MAT_APP`.`CLAUSE` SET `NAME`='test_Population2' WHERE `ID`='8a4d928134a3d1fb0134c32596c240a5';

delete from DECISION where ID in ('345e404b-11b6-491d-986c-2171510c963a');

INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','1ea7b6aa-4d6b-4565-a94b-75506a3bdbf3','0',NULL,NULL);