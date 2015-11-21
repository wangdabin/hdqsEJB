#!/usr/bin/ksh

. ~/.bash_profile

flog=$BASE_HOME/data_del.log
ftmp=$BASE_HOME/data_del.tmp
ftmp2=$BASE_HOME/data_del2.tmp


ouid="$SOP_USER/$SOP_PASSWD"
START_PT_DAYS=15  #初始化预创建的分区表个数

####################

CMD_SQLPLUS=$ORACLE_HOME/bin/sqlplus

####################

if [ ! -d $BASE_HOME ]; then
    mkdir -p $BASE_HOME
fi
###########################
# parameter1 表名称
# parameter2 删除n天前分区
###########################

if [ $# -lt 2 ]; then
   echo "parameter1 :表名称 , parameter2: 删除n天前分区" | tee -a $flog
   exit 1
fi
HTABLE_NAME=`echo $1|tr a-z A-Z`
echo "表名称(参数1):$HTABLE_NAME" | tee -a $flog
echo "删除n天前分区(参数2):$2" | tee -a $flog

#删除历史分取表，建立下次分区表
$CMD_SQLPLUS -s $ouid <<!!! | grep "AAA" | read riqidrp riqicrt1 riqicrt2 tmpstr
select to_char( to_date( JIOYRQ, 'YYYYMMDD' ) - $2, 'YYYYMMDD' ), to_char( to_date( JIOYRQ, 'YYYYMMDD' ) + $START_PT_DAYS, 'YYYYMMDD' ), to_char( to_date( JIOYRQ, 'YYYYMMDD' ) + $START_PT_DAYS + 1, 'YYYYMMDD' ), 'AAA' from PJYRQ;
!!!

#echo "riqidrp:$riqidrp"
#echo "riqicrt1:$riqicrt1"
#echo "riqicrt2:$riqicrt2"

num=0
partition_name="P_"${HTABLE_NAME}"_"${riqidrp}
while [ 1 ]
do
	$CMD_SQLPLUS -s $ouid 1>$ftmp 2>&1 <<-!!!
		alter table $HTABLE_NAME drop partition ${partition_name};
	!!!
	echo "alter table $HTABLE_NAME drop partition ${partition_name}"
	cat $ftmp >>$flog
	grep ERROR $ftmp 2>1 1>/dev/null
	res1=$?
	grep "ORA-02149" $ftmp 2>1 1>/dev/null
	res2=$?
	grep "ORA-00054" $ftmp 2>1 1>/dev/null
	res3=$?
	if [ $res1 -eq 0 ]
	then
		if [ $res2 -ne 0 -a $res3 -ne 0 ]
		then
			cat $ftmp
			echo "删除历史分区${partition_name}出错!\n"
			exit 1
		elif [ $res3 -eq 0 ]
		then
		    #ORA-00054: resource busy and acquire with NOWAIT specified
			if [ $num -gt 10 ]
			then
				cat $ftmp
				echo "删除历史分区${partition_name}出错!\n"
				exit 1
			else
				num=`expr $num + 1`
				sleep 1
				continue
			fi
		else
		   #ORA-02149: Specified partition does not exist
		   break
		fi
	else
		cat $ftmp
		break
	fi
done


#创建新分区
partition_name="P_"${HTABLE_NAME}"_"${riqicrt1}
partition_max="P_${HTABLE_NAME}_MAXVAL"
partition_standard=${riqicrt2}

$CMD_SQLPLUS -s  $ouid <<-!!! |tee $ftmp
	alter table $HTABLE_NAME split partition ${partition_max} at('$partition_standard') into ( partition ${partition_name}, partition ${partition_max});
!!!
echo "alter table $HTABLE_NAME split partition ${partition_max} at('$partition_standard') into ( partition ${partition_name}, partition ${partition_max})"
cat $ftmp >>$flog
grep ERROR $ftmp 2>1 1>/dev/null
if [ $? -eq 0 ]
then
	echo "新建分区${partition_name}出错!\n"
	exit 1
fi

cat $ftmp

#生产环境有unload 命令 使用unload 命令导出索引名称 或者用sqlplus导出索引
#unload -f' ' $ftmp2 "select index_name from user_indexes where upper(table_name)='$HTABLE_NAME' and index_name not like 'SYS_%'"

#查询该表的索引，在新创建的分区上重新更新索引
query_tmp=`${CMD_SQLPLUS} -s $ouid <<!!! 
  set head off
  set headsep off
  set newp none
  set linesize 100
  set pagesize 0
  set sqlblanklines OFF
  set trimspool off
  set termout off
  set feedback off 
  spool $ftmp2;
       select index_name from user_indexes where upper(table_name)='$HTABLE_NAME' and index_name not like 'SYS_%' and PARTITIONED='YES';
   spool off;
!!!`

for i in `cat $ftmp2`
do

echo "alter index $i rebuild partition P_${HTABLE_NAME}_$riqicrt1;"
echo "alter index $i rebuild partition P_${HTABLE_NAME}_MAXVAL;"
$CMD_SQLPLUS -s  $ouid <<-!!! |tee $ftmp
	alter index $i rebuild partition P_${HTABLE_NAME}_$riqicrt1;
	alter index $i rebuild partition P_${HTABLE_NAME}_MAXVAL;
!!!

cat $ftmp >>$flog
grep ERROR $ftmp 2>1 1>/dev/null
if [ $? -eq 0 ]
then
	echo "重建索引 $i  P_${HTABLE_NAME}_${riqicrt1} 出错!\n"
	exit 1
fi

done

#表分区统计
$CMD_SQLPLUS -s  $ouid <<-!!! |tee $ftmp
	exec dbms_stats.gather_table_stats(ownname=>'$SOP_USER',tabname=>'$HTABLE_NAME',partname=>'P_${HTABLE_NAME}_$riqicrt1',method_opt=>'FOR COLUMNS SIZE REPEAT',cascade=>TRUE,granularity=>'PARTITION');
	exec dbms_stats.gather_table_stats(ownname=>'$SOP_USER',tabname=>'$HTABLE_NAME',partname=>'P_${HTABLE_NAME}_MAXVAL',method_opt=>'FOR COLUMNS SIZE REPEAT',cascade=>TRUE,granularity=>'PARTITION');
!!!

grep ERROR $ftmp 2>1 1>/dev/null
if [ $? -eq 0 ]
then
	cat $ftmp >>$flog
	echo "分区${partition_name} gather_table_stats 出错!\n"
	exit 1
fi
exit 0
