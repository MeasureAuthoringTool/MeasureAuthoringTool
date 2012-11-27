alter table CODE_LIST_AUDIT_LOG modify TIMESTAMP timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;
commit;

update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'mhumphrey@telligen.org') where LIST_OBJECT_ID = '8a4d92b2370b19fa013712f2683057f4';

update CODE_LIST_AUDIT_LOG set USER_ID = 'hfridere@telligen.org' where USER_ID = 'Email Not Found' and CODE_LIST_ID = '8a4d92b2370b19fa013710a585cc4b88';

update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'hfridere@telligen.org') 
    where NAME = 'do not use' and OID = '2.16.840.1.113883.3.117.1.7.1.354xxx';
update CODE_LIST_AUDIT_LOG set USER_ID = 'hfridere@telligen.org' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'do not use' and OID = '2.16.840.1.113883.3.117.1.7.1.354xxx');
	
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'pathak.jyotishman@mayo.edu') 
    where NAME = 'Table 4: Mayo Lab Mapping' and OID = '2.16.840.1.113883.3.2.1.69.1';
update CODE_LIST_AUDIT_LOG set USER_ID = 'pathak.jyotishman@mayo.edu' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Table 4: Mayo Lab Mapping' and OID = '2.16.840.1.113883.3.2.1.69.1');
    
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Meningococcal polysaccharide' and OID = '2.16.840.1.113883.3.464.0003.96.01.0006';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Meningococcal polysaccharide' and OID = '2.16.840.1.113883.3.464.0003.96.01.0006');
    
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Meningococcal polysaccharide' and OID = '2.16.840.1.113883.3.464.0003.96.02.0006';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Meningococcal polysaccharide' and OID = '2.16.840.1.113883.3.464.0003.96.02.0006');

update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Tdap Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.01.0008';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Tdap Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.01.0008');
    
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Tdap Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.02.0008';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Tdap Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.02.0008');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'TD Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.01.0010';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'TD Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.01.0010');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'TD Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.02.0010';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'TD Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.02.0010');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Tdap Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0040';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Tdap Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0040');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Tdap Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0021';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Tdap Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0021');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Td Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0041';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Td Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0041');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Td Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0022';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Td Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0022');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Meningococcal Conjugate Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0042';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Meningococcal Conjugate Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0042');

update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Meningococcal Conjugate Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0023';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Meningococcal Conjugate Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0023');

update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Meningococcal Polysaccharide Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0043';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Meningococcal Polysaccharide Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0043');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'Meningococcal Polysaccharide Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0024';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'Meningococcal Polysaccharide Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0024');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'HPV Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0044';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'HPV Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.01.0044');
		
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'HPV Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0025';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'HPV Vaccine Administered' and OID = '2.16.840.1.113883.3.464.0003.98.02.0025');
	
update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'HPV Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.01.0004';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'HPV Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.01.0004');

update LIST_OBJECT set OBJECT_OWNER = (select USER_ID from USER where EMAIL_ADDRESS = 'aslam_sadia@bah.com') 
    where NAME = 'HPV Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.02.0004';
update CODE_LIST_AUDIT_LOG set USER_ID = 'aslam_sadia@bah.com' 
	where USER_ID = 'Email Not Found' and CODE_LIST_ID in (select LIST_OBJECT_ID from LIST_OBJECT 
		where NAME = 'HPV Vaccine' and OID = '2.16.840.1.113883.3.464.0003.96.02.0004');

alter table CODE_LIST_AUDIT_LOG modify TIMESTAMP timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
commit;