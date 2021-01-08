# 微信小程序后台(miniprogram-code)
#### mvn 打包成jar包
```$xslt
cd miniprogram-code/start
mvn clean install -Dmaven.test.skip=true
# jar 文件生成在miniprogram-code/start/target/start.jar

# 运行 start.jar
java -Xmx1024m -Xms1024m -jar start.jar
# linux后台运行start.jar
nohup java -Xmx1024m -Xms1024m -jar start.jar > $LOG_1 2>&1 &
```


### docker 打包此服务镜像
##### 1. 使用docker-maven-plugin插件进行制作镜像
```
<properties>
    <docker.image.prefix>zfw</docker.image.prefix>
    <docker.dir>/home/v1/</docker.dir>
</properties>

...

<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <dockerHost>http://ip:2375</dockerHost>
        <!--镜像名字:版本-->
        <imageName>${docker.image.prefix}/${project.artifactId}:${project.version}</imageName>
        <!--依赖基础镜像 jdk镜像制作说明在miniprogram-code/start/resourse/docker/jdk8/README.md-->
        <baseImage>zfw/jdk8</baseImage>
        <!--作者姓名-->
        <maintainer>zfw</maintainer>
        <workdir>${docker.dir}</workdir>
        <cmd>["java","-Dfile.encoding=utf-8", "-jar", "${docker.dir}${project.build.finalName}.jar"]</cmd>
        <!-- 指定Dockerfile 文件的位置 上面的配置和下Dockerfile里面的一样 二者只能选一 -->
        <!--<dockerDirectory>${project.basedir}/src/main/resources/docker</dockerDirectory>-->
        <resources>
            <resource>
                <targetPath>${docker.dir}</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resource>
        </resources>
    </configuration>
</plugin>

```
##### 2.制作镜像并上传到远程docker：
```$xslt
mvn clean package -Dmaven.test.skip=true docker:build
或
mvn clean install -Dmaven.test.skip=true docker:build
```
##### 3.登录宿主机，查看，运行：
```
# 查看
docker images
REPOSITORY    TAG       IMAGE ID       CREATED          SIZE
zfw/start     1.0.0     961da4175094   20 minutes ago   693MB

# 运行，-d后台运行，-v 把容器日志目录/home/v1/logs映射到宿主机目录/home/v1/logs/10211，并把端口映射到宿主机的10211端口上
docker run -d -v /home/v1/logs/10211:/home/v1/logs -p10211:10210 961da4175094
```


