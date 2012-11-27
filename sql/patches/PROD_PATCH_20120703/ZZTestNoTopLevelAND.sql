use MAT_APP;

  -- New Changes after the Qa reload.
  -- Numerator1 Removing clauses
  delete from DECISION where ID in ('2251388f-9cd4-47a4-a1f9-cbcafb24f0f4');
  -- Numerator2 Removing clauses and adding Top-level ANd
  delete from DECISION where ID in ('33f408fa-2e05-42cf-8076-d702a12e2d6f');
  INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','38f78d1e-b984-41f6-b66a-7d281b528766','0',NULL,NULL);
  -- Numerator3 Removing clauses and adding Top-level AND
  delete from DECISION where ID in ('f757156c-5541-4051-a04f-f425f00c8e69');
  INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','460ada33-85d7-420e-9c3f-218bf6a12d32','0',NULL,NULL);
  
 -- Numerator 4 Removng Clauses and adding Top-level AND
  delete from DECISION where ID in ('15d181f4-6312-4e65-9a75-04e3336bdf7f');
  INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','a97bc592-3790-468a-bb3b-342628e2881b','0',NULL,NULL);
  
  -- Numerator 5 Removng Clauses and adding Top-level AND
  delete from DECISION where ID in ('8824d19a-7a8b-4226-aa58-74390cd0c6fa');
  INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','bc858095-755f-4cbd-8bda-ba6b37da672a','0',NULL,NULL);
  
  -- Numerator 6 Removing Clauses and adding Top-level AND
  delete from DECISION where ID in ('75cd5ba5-2fd8-4142-af57-1732a54248f6');
  INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','eb372e6c-aced-4ce0-a310-d4f876eb5af9','0',NULL,NULL);
  
  -- Numerator 7 Removing Clauses and adding Top-level AND
  delete from DECISION where ID in ('2ba3e4f4-7885-4607-a7d6-d6af377da491');
  INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','05aebb2f-acbc-4fae-9c6d-e048467f8661','0',NULL,NULL);
  
  
  -- Pop1 Removing Clauses 
  delete from DECISION where ID in ('171de4f5-5d11-4281-b665-4883b3eebd50');
  
  -- Den1 Removing clauses
  delete from DECISION where ID in ('202091f6-7f9c-4579-a7dc-d974b98020f9');