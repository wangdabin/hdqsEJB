#!/usr/bin/ksh

. ~/.bash_profile

flog=$BASE_HOME/update_pjyrq.log
ouid="$SOP_USER/$SOP_PASSWD"

####################

CMD_SQLPLUS=$ORACLE_HOME/bin/sqlplus

####################

if [ ! -d $BASE_HOME ]; then
    mkdir -p $BASE_HOME
fi

echo "*********************************************"
echo "*************PJYRQ Update JIOYRQ*************"
echo "*********************************************"

$CMD_SQLPLUS -s $ouid 1>$flog 2>&1  <<!!!
set autocommit on
UPDATE PJYRQ p1 set p1.JIOYRQ = (select to_char( to_date(p2.JIOYRQ, 'YYYYMMDD') + 1, 'YYYYMMDD') from PJYRQ p2);
!!!
exit 0
