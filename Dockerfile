FROM maven:3.9.8-eclipse-temurin-21-alpine

ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /work

COPY pom.xml .

RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.1.1:go-offline

COPY src/ ./src

COPY bin/ ./bin

RUN ["mvn", "clean", "install", "-DskipTests"]

RUN ["chmod", "+x", "bin/run.sh"]

EXPOSE 8080

ENTRYPOINT ["sh", "bin/run.sh"]