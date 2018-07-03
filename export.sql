--------------------------------------------------------
--  File created - Wednesday-June-27-2018   

--------------------------------------------------------
--  DDL for Table APPOINTMENT
--------------------------------------------------------

  CREATE TABLE "VM"."APPOINTMENT" 
   (	"AID" VARCHAR2(20 BYTE), 
	"VISITOR_ID" NUMBER, 
	"EMPLOYEE_ID" NUMBER, 
	"REASON" VARCHAR2(20 BYTE), 
	"CHECKIN_DATE_TIME" TIMESTAMP (6), 
	"CHECK_OUT_DATE_TIME" TIMESTAMP (6), 
	"APPOINT_CATEGORY_ID" NUMBER, 
	"APPOINTMENT_DATE_TIME" TIMESTAMP (6)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table APPOINTMENT_CATEGORY
--------------------------------------------------------

  CREATE TABLE "VM"."APPOINTMENT_CATEGORY" 
   (	"ACID" NUMBER, 
	"CATEGORY_NAME" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table DEPT
--------------------------------------------------------

  CREATE TABLE "VM"."DEPT" 
   (	"DEPTNO" NUMBER, 
	"DNAME" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table EMPLOYEE
--------------------------------------------------------

  CREATE TABLE "VM"."EMPLOYEE" 
   (	"EMPLOYEE_ID" NUMBER, 
	"PASSWORD" VARCHAR2(20 BYTE), 
	"EMPLOYEE_FNAME" VARCHAR2(20 BYTE), 
	"EMPLOYEE_LNAME" VARCHAR2(20 BYTE), 
	"EMPLOYEE_EMAIL" VARCHAR2(40 BYTE), 
	"EMPLOYEE_CONTACT" VARCHAR2(20 BYTE), 
	"EMPLOYEE_STREET_ADDRESS" VARCHAR2(20 BYTE), 
	"EMPLOYEE_STATE" VARCHAR2(20 BYTE), 
	"EMPLOYEE_POSTAL_CODE" VARCHAR2(20 BYTE), 
	"EMPLOYEE_DOB" TIMESTAMP (6), 
	"DEPTNO" NUMBER, 
	"EMPLOYEE_CITY" VARCHAR2(20 BYTE), 
	"EMPLOYEE_CAT_ID" NUMBER, 
	"EMPLOYEE_WORKPHONE" VARCHAR2(20 BYTE), 
	"EMPLOYEE_COUNTRY" VARCHAR2(20 BYTE), 
	"EMPLOYEE_NRIC" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table EMPLOYEE_CATEGORY
--------------------------------------------------------

  CREATE TABLE "VM"."EMPLOYEE_CATEGORY" 
   (	"CATID" NUMBER, 
	"CATNAME" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Table VISITORS
--------------------------------------------------------

  CREATE TABLE "VM"."VISITORS" 
   (	"VISITOR_ID" NUMBER, 
	"VISITOR_FIRST_NAME" VARCHAR2(20 BYTE), 
	"VISITOR_LAST_NAME" VARCHAR2(20 BYTE), 
	"VISITOR_STREET_ADDRESS" VARCHAR2(20 BYTE), 
	"VISITOR_CITY" VARCHAR2(20 BYTE), 
	"VISITOR_STATE" VARCHAR2(20 BYTE), 
	"VISITOR_POSTAL_CODE" VARCHAR2(20 BYTE), 
	"VISITOR_PHONE_NUMBER" VARCHAR2(20 BYTE), 
	"VISITOR_EMAIL" VARCHAR2(20 BYTE), 
	"VISITOR_COMPANY" VARCHAR2(20 BYTE), 
	"VISITOR_NRIC" VARCHAR2(20 BYTE), 
	"COUNTRY" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into VM.APPOINTMENT
SET DEFINE OFF;
Insert into VM.APPOINTMENT (AID,VISITOR_ID,EMPLOYEE_ID,REASON,CHECKIN_DATE_TIME,CHECK_OUT_DATE_TIME,APPOINT_CATEGORY_ID,APPOINTMENT_DATE_TIME) values ('ScCQZE0a',1,17052689,'Visit',null,null,1,to_timestamp('30/06/2018 13:30:00.000000000','DD/MM/RRRR HH24:MI:SSXFF'));
Insert into VM.APPOINTMENT (AID,VISITOR_ID,EMPLOYEE_ID,REASON,CHECKIN_DATE_TIME,CHECK_OUT_DATE_TIME,APPOINT_CATEGORY_ID,APPOINTMENT_DATE_TIME) values ('3MZleg4d',1,17052689,'Metting',null,null,1,to_timestamp('27/06/2018 14:35:00.000000000','DD/MM/RRRR HH24:MI:SSXFF'));
REM INSERTING into VM.APPOINTMENT_CATEGORY
SET DEFINE OFF;
Insert into VM.APPOINTMENT_CATEGORY (ACID,CATEGORY_NAME) values (1,'Visitor');
Insert into VM.APPOINTMENT_CATEGORY (ACID,CATEGORY_NAME) values (2,'Interview');
Insert into VM.APPOINTMENT_CATEGORY (ACID,CATEGORY_NAME) values (3,'ABCD');
Insert into VM.APPOINTMENT_CATEGORY (ACID,CATEGORY_NAME) values (4,'A');
Insert into VM.APPOINTMENT_CATEGORY (ACID,CATEGORY_NAME) values (5,'B');
REM INSERTING into VM.DEPT
SET DEFINE OFF;
Insert into VM.DEPT (DEPTNO,DNAME) values (4,'Security');
Insert into VM.DEPT (DEPTNO,DNAME) values (1,'IT');
Insert into VM.DEPT (DEPTNO,DNAME) values (2,'Engineering');
Insert into VM.DEPT (DEPTNO,DNAME) values (3,'Finance');
REM INSERTING into VM.EMPLOYEE
SET DEFINE OFF;
Insert into VM.EMPLOYEE (EMPLOYEE_ID,PASSWORD,EMPLOYEE_FNAME,EMPLOYEE_LNAME,EMPLOYEE_EMAIL,EMPLOYEE_CONTACT,EMPLOYEE_STREET_ADDRESS,EMPLOYEE_STATE,EMPLOYEE_POSTAL_CODE,EMPLOYEE_DOB,DEPTNO,EMPLOYEE_CITY,EMPLOYEE_CAT_ID,EMPLOYEE_WORKPHONE,EMPLOYEE_COUNTRY,EMPLOYEE_NRIC) values (17052689,'admin','Kai Wei','Tan','admin','0164702346','Bandar Sunway','Selangor','47500',to_timestamp('07/02/1997 17:30:19.000000000','DD/MM/RRRR HH24:MI:SSXFF'),2,'Subang',1,'0325478225(258)','Malaysia','970207355655');
REM INSERTING into VM.EMPLOYEE_CATEGORY
SET DEFINE OFF;
Insert into VM.EMPLOYEE_CATEGORY (CATID,CATNAME) values (3,'Constractor');
Insert into VM.EMPLOYEE_CATEGORY (CATID,CATNAME) values (4,'Visitor');
Insert into VM.EMPLOYEE_CATEGORY (CATID,CATNAME) values (1,'Permenant');
Insert into VM.EMPLOYEE_CATEGORY (CATID,CATNAME) values (2,'Intern');
REM INSERTING into VM.VISITORS
SET DEFINE OFF;
Insert into VM.VISITORS (VISITOR_ID,VISITOR_FIRST_NAME,VISITOR_LAST_NAME,VISITOR_STREET_ADDRESS,VISITOR_CITY,VISITOR_STATE,VISITOR_POSTAL_CODE,VISITOR_PHONE_NUMBER,VISITOR_EMAIL,VISITOR_COMPANY,VISITOR_NRIC,COUNTRY) values (1,'Shaun ','Phun','ABC','Bayan Lepas','Penang','14250','0165667899','shaunPhun@test.com','EFG','850223356855',null);
--------------------------------------------------------
--  DDL for Index EMPLOYEE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VM"."EMPLOYEE_PK" ON "VM"."EMPLOYEE" ("EMPLOYEE_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index EMPLOYEE_CATEGORY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VM"."EMPLOYEE_CATEGORY_PK" ON "VM"."EMPLOYEE_CATEGORY" ("CATID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index APPOINTMENT_CATEGORY_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VM"."APPOINTMENT_CATEGORY_PK" ON "VM"."APPOINTMENT_CATEGORY" ("ACID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index VISITORS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VM"."VISITORS_PK" ON "VM"."VISITORS" ("VISITOR_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index APPOINTMENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VM"."APPOINTMENT_PK" ON "VM"."APPOINTMENT" ("AID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index DEPT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "VM"."DEPT_PK" ON "VM"."DEPT" ("DEPTNO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Function CUSTOM_AUTH
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "VM"."CUSTOM_AUTH" (p_username in VARCHAR2, p_password in VARCHAR2)
return BOOLEAN
is
  l_password varchar2(4000);
  l_stored_password varchar2(4000);
  l_expires_on date;
  l_count number;
begin
-- First, check to see if the user is in the user table
select count(*) into l_count from demo_users where user_name = p_username;
if l_count > 0 then
  -- First, we fetch the stored hashed password & expire date
  select password, expires_on into l_stored_password, l_expires_on
   from demo_users where user_name = p_username;

  -- Next, we check to see if the user's account is expired
  -- If it is, return FALSE
  if l_expires_on > sysdate or l_expires_on is null then

    -- If the account is not expired, we have to apply the custom hash
    -- function to the password
    l_password := custom_hash(p_username, p_password);

    -- Finally, we compare them to see if they are the same and return
    -- either TRUE or FALSE
    if l_password = l_stored_password then
      return true;
    else
      return false;
    end if;
  else
    return false;
  end if;
else
  -- The username provided is not in the DEMO_USERS table
  return false;
end if;
end;

/
--------------------------------------------------------
--  DDL for Function CUSTOM_HASH
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "VM"."CUSTOM_HASH" (p_username in varchar2, p_password in varchar2)
return varchar2
is
  l_password varchar2(4000);
  l_salt varchar2(4000) := 'RIXLRMJYHZDR6JCCRGFVMCO3FT7Y85';
begin

-- This function should be wrapped, as the hash algorhythm is exposed here.
-- You can change the value of l_salt or the method of which to call the
-- DBMS_OBFUSCATOIN toolkit, but you much reset all of your passwords
-- if you choose to do this.

l_password := utl_raw.cast_to_raw(dbms_obfuscation_toolkit.md5
  (input_string => p_password || substr(l_salt,10,13) || p_username ||
    substr(l_salt, 4,10)));
return l_password;
end;

/
--------------------------------------------------------
--  Constraints for Table APPOINTMENT
--------------------------------------------------------

  ALTER TABLE "VM"."APPOINTMENT" ADD CONSTRAINT "APPOINTMENT_PK" PRIMARY KEY ("AID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "VM"."APPOINTMENT" MODIFY ("AID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EMPLOYEE
--------------------------------------------------------

  ALTER TABLE "VM"."EMPLOYEE" ADD CONSTRAINT "EMPLOYEE_PK" PRIMARY KEY ("EMPLOYEE_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_NRIC" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_COUNTRY" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_WORKPHONE" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_CAT_ID" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_CITY" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("DEPTNO" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_DOB" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_POSTAL_CODE" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_STATE" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_STREET_ADDRESS" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_CONTACT" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_EMAIL" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_LNAME" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_FNAME" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("PASSWORD" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE" MODIFY ("EMPLOYEE_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table APPOINTMENT_CATEGORY
--------------------------------------------------------

  ALTER TABLE "VM"."APPOINTMENT_CATEGORY" ADD CONSTRAINT "APPOINTMENT_CATEGORY_PK" PRIMARY KEY ("ACID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "VM"."APPOINTMENT_CATEGORY" MODIFY ("CATEGORY_NAME" NOT NULL ENABLE);
  ALTER TABLE "VM"."APPOINTMENT_CATEGORY" MODIFY ("ACID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table VISITORS
--------------------------------------------------------

  ALTER TABLE "VM"."VISITORS" ADD CONSTRAINT "VISITORS_PK" PRIMARY KEY ("VISITOR_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "VM"."VISITORS" MODIFY ("VISITOR_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table EMPLOYEE_CATEGORY
--------------------------------------------------------

  ALTER TABLE "VM"."EMPLOYEE_CATEGORY" ADD CONSTRAINT "EMPLOYEE_CATEGORY_PK" PRIMARY KEY ("CATID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "VM"."EMPLOYEE_CATEGORY" MODIFY ("CATNAME" NOT NULL ENABLE);
  ALTER TABLE "VM"."EMPLOYEE_CATEGORY" MODIFY ("CATID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table DEPT
--------------------------------------------------------

  ALTER TABLE "VM"."DEPT" ADD CONSTRAINT "DEPT_PK" PRIMARY KEY ("DEPTNO")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "VM"."DEPT" MODIFY ("DEPTNO" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table APPOINTMENT
--------------------------------------------------------

  ALTER TABLE "VM"."APPOINTMENT" ADD CONSTRAINT "APPOINTMENT_FK1" FOREIGN KEY ("VISITOR_ID")
	  REFERENCES "VM"."VISITORS" ("VISITOR_ID") ENABLE;
  ALTER TABLE "VM"."APPOINTMENT" ADD CONSTRAINT "APPOINTMENT_FK2" FOREIGN KEY ("APPOINT_CATEGORY_ID")
	  REFERENCES "VM"."APPOINTMENT_CATEGORY" ("ACID") ENABLE;
  ALTER TABLE "VM"."APPOINTMENT" ADD CONSTRAINT "APPOINTMENT_FK3" FOREIGN KEY ("EMPLOYEE_ID")
	  REFERENCES "VM"."EMPLOYEE" ("EMPLOYEE_ID") ENABLE;
