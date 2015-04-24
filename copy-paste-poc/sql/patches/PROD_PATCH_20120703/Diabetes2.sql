# deleting the QDS term first 
 delete from DECISION where ID in ('a13164be-e785-4b21-ac09-adc542a350d3'); 

# Correcting an invalid phrase attached without top-level And with population1
delete from DECISION WHERE ID in('b48b7745-fcaa-44ab-a149-684213952845','e2dbf38a-0abe-45d8-8e57-40eec8f3e9fe');
