# 依赖镜像
FROM java:8
# 作者
MAINTAINER enhui.chen
# 文件拷贝
COPY app.jar /pegasus.jar
# 数据挂载(可被run -v覆盖) :/app/logs(容器) -> /var/lib/docker/volumes/随机(docker inspect [容器名/容器ID] 可查看)/_data(宿主机)

VOLUME /app/logs
# 对外暴露的端口
EXPOSE 8079
# 执行命令
ENTRYPOINT exec java -Xms128m -Xmx512m -jar /pegasus.jar
#  docker run -d --name pegasus -v /app/logs:/app/logs --net=host -p 8079:8079  ceh/pegasus:0.0.1