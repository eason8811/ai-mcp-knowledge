# 基础镜像，可以先执行 docker pull openjdk:17-jdk-slim
FROM openjdk:17-jdk-slim

# 作者
LABEL maintainer="Eason"

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD jar/ai-mcp-client-eason.jar /ai-mcp-client-eason.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /ai-mcp-client-eason.jar $PARAMS"]