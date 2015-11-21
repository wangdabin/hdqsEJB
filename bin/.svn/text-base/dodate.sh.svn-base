#!/usr/bin/ksh
##0 0 * * * ksh /opt/bigdata-cebank/bin/dodate.sh > /opt/bigdata-cebank/bin/dodate.log 2>&1 &
ksh /opt/bigdata-cebank/bin/hdqsUpdateJiayrq.sh
if [ $? -eq 1 ]
then
	echo "更新交易日期出错了!\n"
	exit 1
fi
echo "*********************************************"
echo "*************PJYJL handle Partition**********"
echo "*********************************************"
ksh /opt/bigdata-cebank/bin/hdqsTabPart.sh PJYJL 30
if [ $? -eq 1 ]
then
	echo "PJYJL表分区表更新出错了!\n"
	exit 1
fi

echo "*********************************************"
echo "*************PYBJY handle Partition**********"
echo "*********************************************"
ksh /opt/bigdata-cebank/bin/hdqsTabPart.sh PYBJY 30
if [ $? -eq 1 ]
then
	echo "PYBJY表分区表更新出错了!\n"
	exit 1
else
    echo "0"
    exit 0
fi
