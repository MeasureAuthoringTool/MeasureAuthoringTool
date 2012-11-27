#v1.0
delete from CLAUSE where MEASURE_ID ='8a4d92b235fb4aa701363bde1300284a' and NAME in('SUB02_Denominator2','SUB02_Denominator3', 'SUB02_Numerator2', 'SUB02_Denominator Exclusions2');
update CLAUSE set NAME = 'SUB02_Numerator2' where MEASURE_ID ='8a4d92b235fb4aa701363bde1300284a' and NAME = 'SUB02_Numerator3';
update CLAUSE set NAME = 'SUB02_Numerator3' where MEASURE_ID ='8a4d92b235fb4aa701363bde1300284a' and NAME = 'SUB02_Numerator4';
update CLAUSE set NAME =  'SUB02_Denominator Exclusions2' where MEASURE_ID ='8a4d92b235fb4aa701363bde1300284a' and NAME = 'SUB02_Denominator Exclusions3';

#v1.1
delete from CLAUSE where MEASURE_ID ='8a4d92b2373f82e20137a35e005d64cc' and NAME in('SUB02_Numerator3','SUB02_Numerator4','SUB02_Denominator Exclusions3');
#v1.2
delete from CLAUSE where MEASURE_ID ='8a4d92b2373f82e20137a4cc894a2247' and NAME in('SUB02_Numerator3','SUB02_Numerator4','SUB02_Numerator5');
commit;