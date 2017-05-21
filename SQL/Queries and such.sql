select * from LIFETIME_TICKETS where MEMBER_NAME = 'Steve 1433';

delete from LIFETIME_TICKETS where ID = 213;
commit;

SELECT * FROM LIFETIME_TICKETS 
  WHERE 1=1
  AND DATE_FORMAT(M_DATE,'%c/%d/%Y') = '5/18/2017'
  ORDER BY TICKETS;


INSERT INTO swgohticketdb.LIFETIME_TICKETS(
   GUILD_ID
  ,MEMBER_NAME
  ,TICKETS
  ,M_DATE
  ,SOURCE_FILE
) VALUES (
  1 -- GUILD_ID - IN int(11)
  ,'RightOnBilly' -- MEMBER_NAME - IN varchar(50)
  ,600 -- TICKETS - IN bigint(20)
  ,'5/18/2017 5:00:00 AM'  -- M_DATE - IN datetime
  ,'N/A'  -- SOURCE_FILE - IN text
);
commit;
