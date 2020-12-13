FROM maven:3.6.3-ibmjava-8-alpine

RUN mkdir /server

ADD ./startup-server.sh /server/startup-server.sh

ENTRYPOINT ["bash", "/server/startup-server.sh"]