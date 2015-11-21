#!/bin/sh

if [ ! -d ${LOG_PATH} ]
    then
         mkdir -p ${LOG_PATH}
fi

DATA_FLAG=`date +%Y%m%d%H%M`

USER_ADMIN_URL="http://${ADMIN_IP}:${ADMIN_PORT}"

LOG_FILE=${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}.log

#*************************Function START*********************************************** 
#INTI_AGRS
INIT_ARGS () {

case `uname -s` in
HP-UX)

	MEM_AGRS="-Xverbosegc:file=${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}_GC.log" 
;;
AIX)

  MEM_AGRS="-Xverbosegclog:${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}_GC.log" 
  
;;
LINUX|Linux)

  MEM_AGRS="-verbose:gc -Xloggc:${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}_GC.log" 
  
;;
esac

JAVA_OPATION="-Dweblogic.threadpool.MinPoolSize=400 -Dweblogic.threadpool.MaxPoolSize=400 -Djava.security.egd=file:/dev/./urandom ${JAVA_OPATION}"

USER_MEM_ARGS="${HEAP_SIZE} ${MEM_AGRS} ${JAVA_OPATION} ${USER_JAVA_OPTIONS}"

export USER_MEM_ARGS

}

#INTI_AGRS_Diagnosid
INIT_ARGS_Diagnosid () {

case `uname -s` in
HP-UX)

	MEM_AGRS="-Xverbosegc:file=${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}_GC.log" 
;;
AIX)

  MEM_AGRS="-Xverbosegclog:${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}_GC.log" 
  
;;
LINUX|Linux)

  MEM_AGRS="-verbose:gc -Xloggc:${LOG_PATH}/${SRV_NAME}_${DATA_FLAG}_GC.log" 
  
;;
esac

JAVA_OPATION="-Dweblogic.threadpool.MinPoolSize=400 -Dweblogic.threadpool.MaxPoolSize=400 -Djava.security.egd=file:/dev/./urandom ${JAVA_OPATION}"

USER_MEM_ARGS="${HEAP_SIZE} ${MEM_AGRS} ${JAVA_OPATION} ${USER_JAVA_OPTIONS}"

export USER_MEM_ARGS
}

#************************Function END***************************************************
case $1 in 
INIT_ARGS)
   INIT_ARGS
;;
INIT_ARGS_Diagnosid)
   INIT_ARGS_Diagnosid
;;
esac