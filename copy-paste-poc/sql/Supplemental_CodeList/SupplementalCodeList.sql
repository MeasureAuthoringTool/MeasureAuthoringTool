-- --------------------------------------------------------------------------------
-- Routine DDL
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`mat_app_usr`@`%` PROCEDURE `supplemental_proc`()
    SQL SECURITY INVOKER
BEGIN
					
					 DECLARE measure_id VARCHAR(64);

  					 DECLARE no_more_rows BOOLEAN;

  					 DECLARE measure_cur CURSOR FOR

    					SELECT M.ID FROM MEASURE M ;
                     

  						DECLARE CONTINUE HANDLER FOR NOT FOUND

    						SET no_more_rows = TRUE;


					  DELETE FROM `QUALITY_DATA_MODEL` where LIST_OBJECT_ID in ('bae50f18267111e1a17a78acc0b65c43','bae85d87267111e1a17a78acc0b65c43','bae86046267111e1a17a78acc0b65c43','bae86261267111e1a17a78acc0b65c43');         
					  
					  OPEN measure_cur;

					  the_loop: LOOP

						    FETCH  measure_cur  INTO   measure_id    ;
	
								    
	
	      							
	
	      							
						    IF no_more_rows THEN
						        CLOSE measure_cur;
			        			LEAVE the_loop;
   					 		END IF;

						
						
						    
						
						   INSERT INTO `QUALITY_DATA_MODEL_OID_GEN` values ();
						
						   
						   INSERT INTO `QUALITY_DATA_MODEL` (`QUALITY_DATA_MODEL_ID`,`DATA_TYPE_ID`,`LIST_OBJECT_ID`,`MEASURE_ID`,`VERSION`,`OID`,`OCCURRENCE`,`IS_SUPP_DATA_ELEMENT`)
								VALUES
						 	(UUID(),'26','bae50f18267111e1a17a78acc0b65c43',measure_id,'1',(SELECT LAST_INSERT_ID()),NULL,'1');
						
						 	INSERT INTO `QUALITY_DATA_MODEL_OID_GEN` values ();
						
						
						   INSERT INTO `QUALITY_DATA_MODEL` (`QUALITY_DATA_MODEL_ID`,`DATA_TYPE_ID`,`LIST_OBJECT_ID`,`MEASURE_ID`,`VERSION`,`OID`,`OCCURRENCE`,`IS_SUPP_DATA_ELEMENT`)
								VALUES
						 	(UUID(),'26','bae85d87267111e1a17a78acc0b65c43',measure_id,'1',(SELECT LAST_INSERT_ID()),NULL,'1');
						
						    
						    INSERT INTO `QUALITY_DATA_MODEL_OID_GEN` values ();
						
						 	
						    INSERT INTO `QUALITY_DATA_MODEL` (`QUALITY_DATA_MODEL_ID`,`DATA_TYPE_ID`,`LIST_OBJECT_ID`,`MEASURE_ID`,`VERSION`,`OID`,`OCCURRENCE`,`IS_SUPP_DATA_ELEMENT`)
								VALUES
						 				(UUID(),'26','bae86046267111e1a17a78acc0b65c43',measure_id,'1',(SELECT LAST_INSERT_ID()),NULL,'1');
						
						    
						    INSERT INTO `QUALITY_DATA_MODEL_OID_GEN` values ();
						
						 	
						     INSERT INTO `QUALITY_DATA_MODEL` (`QUALITY_DATA_MODEL_ID`,`DATA_TYPE_ID`,`LIST_OBJECT_ID`,`MEASURE_ID`,`VERSION`,`OID`,`OCCURRENCE`,`IS_SUPP_DATA_ELEMENT`)
								VALUES
						 				(UUID(),'26','bae86261267111e1a17a78acc0b65c43',measure_id,'1',(SELECT LAST_INSERT_ID()),NULL,'1');
						
						
					  END LOOP the_loop;

				END
