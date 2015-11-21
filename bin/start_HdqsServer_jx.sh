#!/bin/sh
#***********************USER_ARGS**********************************************
SRV_NAME=HdqsServer
HDQS_HOME="/weblogic/domains/hdqs_domain/config"
HDQS_LOG="/weblogic/wlslogs/hdqs_domain/HdqsServer"
export HDQS_HOME
export HDQS_LOG

ADMIN_IP=10.1.20.63

ADMIN_PORT=17101

DOMAIN_NAME=hdqs_domain

DOMAIN_HOME="/weblogic/domains/hdqs_domain"


HEAP_SIZE="-Xms8192m -Xmx8192m -XX:PermSize=256m -XX:MaxPermSize=256m"

USER_JAVA_OPTIONS=""
#***********************INIT ARGS**********************************************

LOG_PATH=/weblogic/wlslogs/hdqs_domain/${SRV_NAME}
cd /weblogic/domains/hdqs_domain/start_script
. ./parameter.sh INIT_ARGS
#************************Start Server***********************************************  
nohup $DOMAIN_HOME/bin/startManagedWebLogic.sh ${SRV_NAME} ${USER_ADMIN_URL} >> ${LOG_FILE} 2>&1 &
