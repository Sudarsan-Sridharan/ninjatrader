# docker build -t nt-server .
# docker run -d -p8080:8080 -e "service.host=http://192.168.99.100:9000" nt-server

FROM tomcat:8-jre8
MAINTAINER bradwee2000@gmail.com

ADD docker/conf/tomcat-users.xml /usr/local/tomcat/conf/
#ADD docker/conf/server.xml /usr/local/tomcat/conf/
#ADD docker/conf/ROOT.xml /usr/local/tomcat/conf/Catalina/localhost/
ADD target/nt-server-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/server.war

EXPOSE 8080

CMD ["catalina.sh", "run"]