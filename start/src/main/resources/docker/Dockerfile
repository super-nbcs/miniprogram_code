FROM huigou/jdk8:latest
MAINTAINER zfw
LABEL description="描述"
ADD /target/start.jar /home/v1/
RUN chmod +x /home/v1/start.jar
CMD ["java","-Dfile.encoding=utf-8","-jar","/home/v1/start.jar"]
