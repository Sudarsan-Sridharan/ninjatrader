# docker build -t nt-service .
# docker run -d -p9000:9000 -p9001:9001 --restart=always nt-service

FROM phusion/baseimage:latest

# Use baseimage-docker's init system.
CMD ["/sbin/my_init"]

# Copy jar file
ADD target/nt-service-dropwizard-1.0-SNAPSHOT.jar /data/service.jar

# Copy script to start nt-service
RUN mkdir /etc/service/nt-service
COPY docker/nt-service.sh /etc/service/nt-service/run
RUN chmod +x /etc/service/nt-service/run

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

# Expose main and admin ports
EXPOSE 9000 9001

# Clean up APT when done.
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
