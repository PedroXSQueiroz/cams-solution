const MessagingService = require('./services/messaging-service');
const ServerService = require('./services/server-service');
const StreamService = require('./services/stream-service');
const RESOURCE_CONFIG = require('./config/resources-config');

(async () =>
    {
        console.log(`connected to resource servers: ${RESOURCE_CONFIG.URL}`)
        
        const streamService = new StreamService();
        const serverService = new ServerService();
        
        const selfConfig = await serverService.self();

        streamService.setup(selfConfig.hubInputPort, selfConfig.hubOutputPort);

    } 
)();