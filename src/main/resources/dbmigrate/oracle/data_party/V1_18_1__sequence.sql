drop sequence CD_PARTY_ENTITYIS;
drop sequence CD_PARTY_STRUCTIS;
drop sequence CD_PARTY_STRUCT_RULEIS;
drop sequence CD_PARTY_STRUCT_TYPEIS;
drop sequence CD_PARTY_TYPEIS;
drop sequence CD_ROLEIS;
drop sequence CD_USERIS;

create sequence CD_PARTY_ENTITYIS
minvalue 1
maxvalue 999999999999999999999999999
start with 40
increment by 1
cache 20;

create sequence CD_PARTY_STRUCTIS
minvalue 1
maxvalue 999999999999999999999999999
start with 324
increment by 1
cache 20;

create sequence CD_PARTY_STRUCT_RULEIS
minvalue 1
maxvalue 999999999999999999999999999
start with 100
increment by 1
cache 20;

create sequence CD_PARTY_STRUCT_TYPEIS
minvalue 1
maxvalue 999999999999999999999999999
start with 2
increment by 1
cache 20;

create sequence CD_PARTY_TYPEIS
minvalue 1
maxvalue 999999999999999999999999999
start with 6
increment by 1
cache 20;

create sequence CD_ROLEIS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;

create sequence CD_USERIS
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;