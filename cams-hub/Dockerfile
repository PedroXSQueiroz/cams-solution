FROM debian

RUN mkdir /hub && \
    DEBIAN_FRONTEND='noninteractive' apt update -y && \
    DEBIAN_FRONTEND='noninteractive' apt upgrade -y && \

    DEBIAN_FRONTEND='noninteractive' apt install -y netcat && \

    DEBIAN_FRONTEND='noninteractive' apt install -y curl && \

    DEBIAN_FRONTEND='noninteractive' apt install -y yasm libvpx. libx264. && \
    
    echo ------------------------------------------------------------------- && \
    echo REPOSITORY UPDATED && \
    echo ------------------------------------------------------------------- && \
    
    curl -sL https://deb.nodesource.com/setup_12.x | bash  && \
    DEBIAN_FRONTEND='noninteractive' apt install -y nodejs && \
    node -v && \
    npm -v && \
    
    npm install nodemon -g && \

    echo ------------------------------------------------------------------- && \
    echo NODE INSTALLED && \
    echo ------------------------------------------------------------------- && \

    DEBIAN_FRONTEND='noninteractive' apt install ffmpeg -y && \

    echo ------------------------------------------------------------------- && \
    echo FFMPEG INSTALLED && \
    echo ------------------------------------------------------------------- && \


ADD ./startup-hub.sh /hub/startup-hub.sh

RUN chmod +x /hub/startup-hub.sh

ENTRYPOINT [ "sh", "/hub/startup-hub.sh" ]