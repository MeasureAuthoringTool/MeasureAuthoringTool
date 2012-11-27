-- resets the password for the admin account to Admin (note the capital A)

update `MAT_APP`.`USER_PASSWORD` set `SALT`='bbebea51-797b-4734-b71f-646e14843bda', 
    `PASSWORD`='e47b456be30459c185ddcebb92491767',
    `PWD_LOCK_COUNTER`=0,
    `FORGOT_PWD_LOCK_COUNTER`=0
where `USER_PASSWORD_ID`='1';
update `MAT_APP`.`USER` set `LOCKED_OUT_DATE`=null where `USER_ID`='Admin';

