use MAT_APP;
#replace items:
#Aaron.Cutshall@LantanaGroup.com -- from user
#mhinterberg@ofmq.com -- to user
#<<audit_log_text1>> -- free text entered into the production job script to be determined by NQF with each request
#<<measure_log_text1>> -- free text entered into the production job script to be determined by NQF with each request

#update CODE_LIST_AUDIT_LOG
INSERT INTO CODE_LIST_AUDIT_LOG
   (ID
   ,CODE_LIST_ID
   ,ACTIVITY_TYPE
   ,USER_ID
   ,TIMESTAMP
   ,ADDL_INFO)
select (REPLACE(UUID(),'-',''))
      ,LIST_OBJECT_ID
      ,'Value Set Ownership Changed'
      ,''
      ,NOW()
      ,'Value Set Owner transferred from Aaron.Cutshall@LantanaGroup.com to mhinterberg@ofmq.com. Submitter: Chengjian Cheng, Approver: Michelle Hinterberg, Approval Provided 7/20/2012, Ticket # 35355'
FROM LIST_OBJECT
WHERE OBJECT_OWNER IN (SELECT USER_ID
                       FROM USER
                       WHERE EMAIL_ADDRESS = 'Aaron.Cutshall@LantanaGroup.com');


#update MEASURE_AUDIT_LOG
INSERT INTO MEASURE_AUDIT_LOG
   (ID
   ,MEASURE_ID
   ,ACTIVITY_TYPE
   ,USER_ID
   ,TIMESTAMP
   ,ADDL_INFO)
select (REPLACE(UUID(),'-',''))
      ,ID
      ,'Measure Ownership Changed'
      ,''
      ,NOW()
      ,'Measure Owner changed from Aaron.Cutshall@LantanaGroup.com to mhinterberg@ofmq.com. <<measure_log_text1>>'
FROM MEASURE
WHERE MEASURE_OWNER_ID IN (SELECT USER_ID
                       FROM USER
                       WHERE EMAIL_ADDRESS = 'Aaron.Cutshall@LantanaGroup.com');



#update MEASURE
	update MEASURE set MEASURE_OWNER_ID = (select USER_ID from USER where EMAIL_ADDRESS = 'mhinterberg@ofmq.com') where MEASURE_OWNER_ID in (select USER_ID from USER where EMAIL_ADDRESS = 'Aaron.Cutshall@LantanaGroup.com');

#update LIST_OBJECT
	update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'mhinterberg@ofmq.com') where OBJECT_OWNER in (select USER_ID from USER where EMAIL_ADDRESS = 'Aaron.Cutshall@LantanaGroup.com');

#update MEASURE_SHARE.MEASURE_OWNER_USER_ID
	update MEASURE_SHARE set MEASURE_OWNER_USER_ID = (select USER_ID from USER where EMAIL_ADDRESS = 'mhinterberg@ofmq.com') where MEASURE_OWNER_USER_ID in (select USER_ID from USER where EMAIL_ADDRESS = 'Aaron.Cutshall@LantanaGroup.com');