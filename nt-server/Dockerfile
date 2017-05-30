# docker build -t nt-server .
# docker run -d -p8080:8080 -e "service.host=http://192.168.99.100:9000" --restart=always -e TZ=Asia/Manila nt-server

FROM jetty:alpine

MAINTAINER bradwee2000@gmail.com

ADD target/nt-server-1.0-SNAPSHOT.war /var/lib/jetty/webapps/ROOT.war

EXPOSE 8080

CMD java -jar "$JETTY_HOME/start.jar"