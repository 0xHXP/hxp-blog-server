FROM openjdk:11-jre

ENTRYPOINT ["top", "-b"]

# author
MAINTAINER hxp

# 挂载目录
VOLUME /home/hxp
# 创建目录
RUN mkdir -p /home/hxp
# 指定路径
WORKDIR /home/hxp
# 复制jar文件到路径
COPY ./hxp-server/target/hxp-blog.jar /home/hxp/hxp-blog.jar
# 启动认证服务
ENTRYPOINT ["sh", "-c", "java -jar hxp-blog.jar"]
