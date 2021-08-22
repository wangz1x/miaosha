# kill -9 | xargs ps -ef | grep miaosha-9091 | awk '{print $2}'
nohup java -Xms512m -Xmx512m -XX:NewSize=256m -XX:MaxNewSize=256m -jar miaosha-8091.jar --spring.config.addition=location=/home/wangzx/IdeaProjects/miaosha/workdir/server1/application.yaml > log 2>&1 &
