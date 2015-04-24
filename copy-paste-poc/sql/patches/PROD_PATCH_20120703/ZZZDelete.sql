UPDATE `MAT_APP`.`CLAUSE` SET `NAME`='Asthma_Population2' WHERE `ID`='8a4d92813448700d013461c1b7100341';

-- Adding a Top-level AND to Asthma_pop2 manually, for some reason, the top-level AND was not added to it automatically.
INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','cb007ab9-6973-4222-aa31-dc6bc7b3b05d','0',NULL,NULL);