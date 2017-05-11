#docker build -t nt-service .
#docker run -d -p9000:9000 -p9001:9001 --restart=always nt-service

FROM openjdk:8-jre-alpine

ADD target/nt-service-dropwizard-1.0-SNAPSHOT.jar /data/service.jar

CMD java -jar /data/service.jar

EXPOSE 9000 9001
