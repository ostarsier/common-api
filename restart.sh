#!/bin/bash
APP_NAME=common-api
DIR=$(cd $(dirname $0) && pwd )
kill -9 $(ps -ef | grep $APP_NAME-1.0-SNAPSHOT.jar | grep -v grep | awk '{print $2}')
nohup java -server -Xms1024m -Xmx1024m  -Duser.timezone=GMT+08 -Dfile.encoding=utf-8 -jar $DIR/$APP_NAME-1.0-SNAPSHOT.jar --spring.config.location=application.yml >./$APP_NAME.log 2>&1 &
echo "$APP_NAME started"