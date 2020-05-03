const MessagingChannelService = require('./messaging-channel-service')
const ServerService = require('./server-service');

const callbacks = new Map();

module.exports = class MessagingService{

    constructor()
    {
        this._messagingChannelService = new MessagingChannelService();
        this._serverService = new ServerService();
    }
    
   
    async on(action, callback)
    {
        await this._registerQueue(action, callback);
        callbacks.set(action, callback);
    }

    async send(action, content, server)
    {
        let channel = await this._messagingChannelService.getChannel(server);
        
        const queueName = `${server.key}.${action}`;
        const queueResponseName = `${queueName}.response`;

        channel.sendToQueue(queueName, Buffer.from( JSON.stringify(content) ));

        return await new Promise((resolve, reject) => {
            
            channel.consume(queueResponseName , (message) => {
                
                resolve( JSON.parse( message.content.toString() ) );

            }, {
                noAck: true
            })

        });

    }

    async _registerQueue(action, callback, servers)
    {
        
        servers = servers || await this._serverService.list();
        let thisResourceServer = await this._serverService.self();

        console.log(servers);
        
        for(let server of servers)
        {
            let channel = await this._messagingChannelService.getChannel(server);

            const queueName = `${thisResourceServer.key}.${action}`;
            
            channel.assertQueue( queueName, {
                durable: false
            });

            const queueNameResponse = `${queueName}.response`;
            channel.assertQueue( queueNameResponse, {
                durable: false
            });

            channel.consume(queueName, function(msg) 
            {
                
                if( callback.constructor.name === "AsyncFunction" )
                {
                    callback( JSON.parse( msg.content.toString() ) ).then( response => {
                        channel.sendToQueue(queueNameResponse, Buffer.from( JSON.stringify( response ) ));
                    }).catch(e => {
                        console.error(e);
                    });
                }
                else
                {
                    let response = callback( JSON.parse( msg.content.toString() ) );
                    channel.sendToQueue(queueNameResponse, Buffer.from( JSON.stringify( response ) ));
                }


            });
        }
    }

}