#replace items:
#<<email_1>> -- from user
use MAT_APP;
#counts to report
SELECT  (
	select count(*) from MEASURE where MEASURE_OWNER_ID in (select USER_ID from USER where EMAIL_ADDRESS = '<<email_1>>')
    ) AS tot_measures,
    (
	select count(*) from LIST_OBJECT where OBJECT_OWNER in (select USER_ID from USER where EMAIL_ADDRESS = '<<email_1>>')
    ) AS tot_value_sets,
    (
	select count(*) from MEASURE_SHARE where MEASURE_OWNER_USER_ID in (select USER_ID from USER where EMAIL_ADDRESS = '<<email_1>>')
    ) AS tot_measures_shared;