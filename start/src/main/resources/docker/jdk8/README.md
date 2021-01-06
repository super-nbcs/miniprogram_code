# jdk8镜像制作

#### Dockerfile
```
# 基础镜像，依赖centos:7
FROM centos:7
# 制作作者
MAINTAINER jdk8
# 将jdk-8u221-linux-x64.tar.gz（jdk压缩文件自己找）压缩包解压到容器中的/usr/local目录下
ADD jdk-8u221-linux-x64.tar.gz /usr/local
# 配置环境变量
ENV JAVA_HOME /usr/local/jdk1.8.0_221
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin
# 查看版本
CMD java -version
```
#### jdk8.sh
```$xslt
#!/usr/bin/env bash
# 制作镜像命令，这条命令在Dockerfile同级目录下运行
docker build -t zfw/jdk8 .
```

