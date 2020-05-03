FROM node:12.16.2-alpine3.9

RUN mkdir /client && \ 
    npm install -g gulp && \
    npm install -g lite-server  

ADD ./startup-client.sh /client/startup-client.sh

WORKDIR /client/

ENTRYPOINT ["sh" , "startup-client.sh"]