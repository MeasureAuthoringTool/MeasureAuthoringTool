#steps for dba:
#report the results of 
#(1) delete from DECISION where ID is in the result set of the query below
#(2) delete form CLAUSE where DECISION_ID is in the result set of the query below
create table IDLOOKUP as (
select d.ID from DECISION d inner join CLAUSE c
on d.ID=c.DECISION_ID
where c.CONTEXT_ID not in('11','10')
and not(d.ID in(select PARENT_ID from DECISION where PARENT_ID is not null)));



delete from DECISION 
where ID in (
select ID from IDLOOKUP);


delete from CLAUSE
where DECISION_ID in (
select ID from IDLOOKUP);


drop table IDLOOKUP;