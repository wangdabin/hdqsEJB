umask 022

PATH=/bin:/usr/bin:/usr/local/bin:/usr/X11R6/bin

ORACLE_BASE=/oracle/app
ORACLE_HOME=$ORACLE_BASE/product/11.2.0
ORACLE_SID=hdqsdb
LD_LIBRARY_PATH=$ORACLE_HOME/jdk/jre/lib/i386:
PATH=$ORACLE_HOME/bin:$PATH

SOP_USER=hdqs
SOP_PASSWD=sqdh321

BASE_HOME=/opt/bigdata-cebank/logs

export PATH LD_LIBRARY_PATH
export ORACLE_BASE ORACLE_HOME ORACLE_SID
export BASE_HOME

export TFTCFG=/home/hadoop/tft/tft.conf  	
export RUNLOG=/home/hadoop

