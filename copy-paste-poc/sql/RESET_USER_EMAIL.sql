use MAT_APP;
select QUESTION, ANSWER from USER_SECURITY_QUESTIONS where USER_ID in(select USER_ID from USER where EMAIL_ADDRESS = '<<target email>>');
update USER set EMAIL_ADDRESS = '<<your email>>' where EMAIL_ADDRESS = '<<target email>>';