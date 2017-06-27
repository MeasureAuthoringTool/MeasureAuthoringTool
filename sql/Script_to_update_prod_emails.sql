--THIS SCRIPT WILL UPDATE THE EMAILS OF ALL USERS (EXCEPT BARTLEMAN,WISHAM,HUMPHREY) TO 
--INVALID EMAILS ID's.
--THIS IS NECESSARY FOR ALL DB DMPS THAT WE BRING IN FROM TRAINING,STAGING OR PROD,  
--So that automated emails arent generated to all users unnecessarily.

update USER set SIGN_IN_DATE= SYSDATE();

--Wait for 30 seconds. This is done to make sure that SIGN_OUT_DATE > SIGN_IN_DATE. 
--If we dont do this then system will see SIGN_IN_DATE > SIGN_OUT_DATE and think that 
--the User is already logged in and wont allow you to login.

update USER set SIGN_OUT_DATE= SYSDATE();

update MAT_STAGE_COPY_06272017.USER set EMAIL_ADDRESS = REPLACE(EMAIL_ADDRESS,'@','{AT}') WHERE 
UPPER(LAST_NAME) NOT IN ('BARTLEMAN','WISHAM','REDDY','GREY','KADARU','SHOEMAKER','RANKINS','DUNN');