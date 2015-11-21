#!/bin/sh
#***********************USER_ARGS**********************************************
SRV_NAME=HdqsServerSD

psid=0
checkpid() {
    PIDSTR=`ps -ef|grep java|grep weblogic.Name=$SRV_NAME|grep -v grep `
    if [ -n "$PIDSTR" ]; then
          psid=`echo $PIDSTR | awk '{print $2}'`
    else
          psid=0
    fi
}

stop() {
    checkpid
    if [ $psid -ne 0 ]; then
        kill -9 $psid
    else
        echo "warn: $SRV_NAME is not running"
    fi
}

stop
exit 0
