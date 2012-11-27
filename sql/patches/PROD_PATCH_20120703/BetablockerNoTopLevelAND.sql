use MAT_APP;
   delete from DECISION where ID in ('a5a99e6b-2764-4e93-bde1-6f3b78dfa11e');
   delete from DECISION where ID in ('56244cfb-057d-4a7f-a3ce-f49acdac8b3f');
   
-- Adding Top-level AND for Denominator 2    
INSERT INTO `DECISION`(`id`,`operator`,`parent_id`,`order_num`,`clause_id`,`attribute_id` )values(uuid(),'AND','4f7e0e89-d0d4-4413-b198-5c09dc0032d5','0',NULL,NULL);