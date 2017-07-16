drop sequence CD_ROLEIS;
drop sequence CD_USERIS;

create sequence CD_ROLEIS
minvalue 1
maxvalue 999999999999999999999999999
start with 6
increment by 1
cache 20;

create sequence CD_USERIS
minvalue 1
maxvalue 999999999999999999999999999
start with 21
increment by 1
cache 20;