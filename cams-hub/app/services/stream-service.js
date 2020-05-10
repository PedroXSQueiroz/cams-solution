var http                = require('http'),
    WebSocket           = require('ws'),
    { spawn }           = require('child_process'),
    http                = require('http'),
    ServerService       = require('./server-service'),
    MessagingService    = require('./messaging-service'),
    ClientService       = require('./client-service'),
    url                 = require('url');

const streams = new Map();
const streamProcesses = new Map();

module.exports = class StreamService 
{
    constructor()
    {
        this._serverService = new ServerService();
        this._messagingService = new MessagingService();
        this._clientService = new ClientService();
    }
    
    async setup(inputPort, outputPort)
    {
        
        console.log(`Setuping sockets hub in: ${inputPort}, out:${outputPort}`)

        this.setupOut(outputPort);

        this.setupIn(inputPort);
        
        await this._messagingService.on('start-stream', async (message) => {

            return await this.start(message.cam, message.server);
        
        });

        await this._messagingService.on('stop-stream', async (data) => {
            
            console.log('received stop message');
            
            let sessionId = data.sessionId;

            const streamProcess = streamProcesses.get(sessionId);
            
            streamProcess.kill();

            return {
                success: streamProcess.killed
            };

        });

    }

    setupIn(inputPort) {
        
        http.createServer((request, response) => {
            
            console.log('frame received');
            
            response.connection.setTimeout(0);
            
            let sessionId = request.url.split('stream/')[1];
            
            request.on('data', (data) => {
                
                if (streams.has(sessionId)) 
                {
                    streams.get(sessionId).send(data);
                }
                else 
                {
                    console.log(`no client attached for stream ${sessionId}`);
                }
            });
        }).listen(inputPort);
    }

    setupOut(outputPort) {
        
        let socketServer = new WebSocket.Server({ port: outputPort, perMessageDeflate: false });
        
        socketServer.on('connection', (socket, upgradeReq) => {
            
            let sessionId = (upgradeReq || socket.upgradeReq).url.split('stream/')[1];
            
            streams.set(sessionId, socket);
            
            socket.on('close', () => {
                
                this.stop(sessionId);
            });
        });
    }

    async start(cam, server)
    {        
        let thisResourceServer = await this._serverService.self();
        const id = `?node=${thisResourceServer.key}&cam=${cam.id}`;

        const inputHubCompleteUrl = `http://${server.hubHost}:${server.hubInputPort}/stream/${id}`;
        
        let streamProcess = spawn('ffmpeg', [
            '-hide_banner',
            '-loglevel', 'panic',
            '-i', cam.address,
            '-f', 'mpegts',
            '-codec:v', 'mpeg1video',
            '-bf', '0',
            '-r', '30',
            inputHubCompleteUrl,
        ]);
        
        let outStream = streamProcess.stdout;
        
        outStream.on("exit", function(code){
            console.log("Failure", code);
        });
        
        streamProcess.stderr.on('data', data => {
            console.error(data.toString())
        });

        streamProcesses.set(id, streamProcess);
        
        return {
            cam: cam,
            id: id
        }
    }

    async stop(sessionId) 
    {
        
        let sessionData = url.parse(sessionId, true).query;
        
        let nodeKey = sessionData.node;
        
        let server = await this._clientService.get(nodeKey);
        
        console.log(`sending stop message to ${server.key}`);

        this._messagingService.send('stop-stream', {
            sessionId: sessionId
        }, server);
    }
}