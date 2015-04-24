#QUERY 1
# get the ID of system clause decisions with a non-'AND' child 
# 63 Clauses in 34 Measures
select DESCRIPTION, ABBR_NAME, DRAFT, VERSION from MEASURE where ID in(
select c.MEASURE_ID from DECISION d inner join CLAUSE c
on d.PARENT_ID=c.DECISION_ID
where c.CONTEXT_ID not in('11','10')
and not(d.OPERATOR = 'AND'));

#QUERY 2
# get the ID of system clause decisions without a child
# 317 Clauses in 88 Measures
select DESCRIPTION, ABBR_NAME, DRAFT, VERSION from MEASURE where ID in(
select c.MEASURE_ID from DECISION d inner join CLAUSE c
on d.ID=c.DECISION_ID
where c.CONTEXT_ID not in('11','10')
and not(d.ID in(select PARENT_ID from DECISION where PARENT_ID is not null)));