FROM tomcat:8-jre8
MAINTAINER bradwee2000@gmail.com

ADD conf/tomcat-users.xml /usr/local/tomcat/conf/
#ADD conf/server.xml /usr/local/tomcat/conf/
#ADD conf/ROOT.xml /usr/local/tomcat/conf/Catalina/localhost/
ADD nt-server-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/server.war

CMD ["catalina.sh", "run"]