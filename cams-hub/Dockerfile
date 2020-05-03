FROM debian

RUN mkdir /hub && \
    apt update -y && \
    apt upgrade -y && \

    apt install netcat -y && \

    apt install curl -y && \

    apt install yasm libvpx. libx264. && \
    
    echo ------------------------------------------------------------------- && \
    echo REPOSITORY UPDATED && \
    echo ------------------------------------------------------------------- && \
    
    curl -sL https://deb.nodesource.com/setup_12.x | bash  && \
    apt install nodejs -y && \
    node -v && \
    npm -v && \
    
    npm install nodemon -g && \

    echo ------------------------------------------------------------------- && \
    echo NODE INSTALLED && \
    echo ------------------------------------------------------------------- && \

    apt install ffmpeg -y && \

    echo ------------------------------------------------------------------- && \
    echo FFMPEG INSTALLED && \
    echo -------------------------------------------------------------------


ADD ./startup-hub.sh /hub/startup-hub.sh

RUN chmod +x /hub/startup-hub.sh

ENTRYPOINT [ "sh", "/hub/startup-hub.sh" ]