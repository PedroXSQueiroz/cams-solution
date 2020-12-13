FROM node:12.16.2-alpine3.9

RUN mkdir /client && \ 
    npm install -g gulp && \
    npm install -g lite-server && \
    npm install -g create-react-app && \
    npm install -g react-scripts 

ADD ./startup-client.sh /client/startup-client.sh

WORKDIR /client/

ENTRYPOINT ["sh" , "startup-client.sh"]