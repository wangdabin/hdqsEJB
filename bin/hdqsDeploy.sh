#!/usr/bin/ksh
backHdqsVersion=hdqs_version_1.5
logFileBakPath=/weblogic/wlslogs/backup/$backHdqsVersion                                               
if [ ! -d "$logFileBakPath" ];then
                                                   
mkdir -p $logFileBakPath 

fi

fileName=`date -d today +"%Y-%m-%d"`                                                  
echo $fileName                                                                          
adminServerFilename=AdminServer$fileName.tar.gz                                         
hdqsServerFileName=HdqsServer$fileName.tar.gz 
echo $adminServerFilename
echo $hdqsServerFileName

cd  /weblogic/wlslogs/hdqs_domain 
tar -zcvf   $hdqsServerFileName  HdqsServer               
tar -zcvf   $adminServerFilename  AdminServer             
#if [ ! -f "$hdqsServerFileName"&& -f "$adminServerFilename" ];then                                                  
 mv $hdqsServerFileName  $logFileBakPath 
 mv $adminServerFilename  $logFileBakPath                                                                                                                                    
#fi 

cd /weblogic/wlslogs/hdqs_domain/HdqsServer
rm -rf * 
cd /weblogic/wlslogs/hdqs_domain/AdminServer
rm -rf *  
 
#**************备份配置文件**************ear包#
cd /home/hdqs/hdqs_web
configBakFilePath="/weblogic/deploy/dep_bak/$backHdqsVersion/config"
if [ ! -d "$logFileBakPath" ];then
mkdir -p $logFileBakPath
fi

mkdir -p /weblogic/deploy/dep_bak/$backHdqsVersion/config>>deploy.log  2>&1
mkdir -p /weblogic/deploy/dep_bak/$backHdqsVersion/config/templates>>deploy.log  2>&1
mkdir -p /weblogic/deploy/dep_bak/$backHdqsVersion/hdqs_ear>>deploy.log  2>&1


mv /weblogic/domains/hdqs_domain/config/hdqs.properties /weblogic/deploy/dep_bak/$backHdqsVersion/config>>deploy.log  2>&1
mv /weblogic/deploy/hdqs.ear /weblogic/deploy/dep_bak/$backHdqsVersion/hdqs_ear/
mv /weblogic/domains/hdqs_domain/config/templates/TemplateOfPrivate.html /weblogic/deploy/dep_bak/$backHdqsVersion/config/templates/
mv /weblogic/domains/hdqs_domain/config/templates/CqtemplateOfCorporate.html /weblogic/deploy/dep_bak/$backHdqsVersion/config/templates/CqtemplateOfCorporate.html


cd /home/hdqs/hdqs_web/>>deploy.log  2>&1
cp hdqs.ear /weblogic/deploy/>>deploy.log  2>&1
cp deploy-conf/hdqs.properties /weblogic/domains/hdqs_domain/config/>>deploy.log  2>&1
cp -r resource /weblogic/deploy/resource

rm -rf /weblogic/domains/hdqs_domain/servers/AdminServer/cache/*>>deploy.log  2>&1
rm -rf /weblogic/domains/hdqs_domain/servers/AdminServer/logs/*>>deploy.log  2>&1
rm -rf /weblogic/domains/hdqs_domain/servers/AdminServer/tmp/*>>deploy.log  2>&1
rm -rf /weblogic/domains/hdqs_domain/servers/HdqsServer/cache/*>>deploy.log  2>&1
rm -rf /weblogic/domains/hdqs_domain/servers/HdqsServer/logs/*>>deploy.log  2>&1
rm -rf /weblogic/domains/hdqs_domain/servers/HdqsServer/stage/*>>deploy.log  2>&1
rm -rf /weblogic/domains/hdqs_domain/servers/HdqsServer/tmp/*>>deploy.log  2>&1
rm -rf /weblogic/wlslogs/hdqs_domain/AdminServer/*>>deploy.log  2>&1
rm -rf /weblogic/wlslogs/hdqs_domain/HdqsServer/*>>deploy.log  2>&1


echo "**************deploy  compeleted****************************"
