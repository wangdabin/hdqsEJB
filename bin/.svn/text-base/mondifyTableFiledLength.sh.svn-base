#!/usr/bin/ksh

. ~/.bash_profile
export NLS_LANG=AMERICAN_AMERICA.UTF8
flog=$BASE_HOME/mondifyTableFileLength.log
ouid="$SOP_USER/$SOP_PASSWD"

####################

CMD_SQLPLUS=$ORACLE_HOME/bin/sqlplus

####################

if [ ! -d $BASE_HOME ]; then
    mkdir -p $BASE_HOME
fi



$CMD_SQLPLUS -s $ouid 1>$flog 2>&1  <<!!!
set autocommit on
alter table PYBJY modify CHINANAME VARCHAR2(80);
commit;

!!!

echo "**************modify table PYBJY  filed CHINANAME length compeleted****************************"
