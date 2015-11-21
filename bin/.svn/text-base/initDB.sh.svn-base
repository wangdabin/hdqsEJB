#!/usr/bin/ksh

. ~/.bash_profile
export NLS_LANG=AMERICAN_AMERICA.UTF8
flog=$BASE_HOME/initDB.log
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
@./sql/create_all_tables.sql
@./sql/create_constraint.sql
@./sql/create_function.sql
@./sql/create_seq.sql

!!!

echo "**************init DB compeleted****************************"
