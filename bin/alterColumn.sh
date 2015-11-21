#!/usr/bin/ksh

. ~/.bash_profile
export NLS_LANG=AMERICAN_AMERICA.UTF8
flog=$BASE_HOME/alertColumn.log
ouid="$SOP_USER/$SOP_PASSWD"

####################

CMD_SQLPLUS=$ORACLE_HOME/bin/sqlplus

####################

if [ ! -d $BASE_HOME ]; then
    mkdir -p $BASE_HOME
fi

echo "*********************************************"
echo "*************init DB start *************"
echo "*********************************************"

$CMD_SQLPLUS -s $ouid 1>$flog 2>&1  <<!!!
set autocommit on
alter table PSQBM modify BEIYZF VARCHAR2(22);
commit;

!!!

echo "**************modify table PSQBM  filed BEIYZF length compeleted****************************"
