# 第一阶段：构建应用
FROM maven:3.8-openjdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 文件
COPY pom.xml .
COPY hxp-common/pom.xml hxp-common/
COPY hxp-admin/pom.xml hxp-admin/
COPY hxp-api/pom.xml hxp-api/
COPY hxp-server/pom.xml hxp-server/
COPY hxp-file/pom.xml hxp-file/
COPY hxp-quartz/pom.xml hxp-quartz/
COPY hxp-auth/pom.xml hxp-auth/

# 下载依赖（利用Docker缓存层）
RUN mvn dependency:go-offline

# 复制源代码
COPY . .

# 构建应用
RUN mvn clean package -DskipTests

# 第二阶段：运行应用
FROM openjdk:8-jre-slim

WORKDIR /app

# 复制构建好的jar包
COPY --from=builder /app/hxp-server/target/hxp-blog.jar ./app.jar

# 复制ip2region.xdb文件（如果需要）
COPY ip2region.xdb ./

# 设置时区
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# 暴露端口（根据实际应用端口修改）
EXPOSE 8080

# 设置启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --spring.output.ansi.enabled=ALWAYS"]
