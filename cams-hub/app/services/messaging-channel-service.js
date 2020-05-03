var amqp = require('amqplib/callback_api');

const connections = new Map();

const channels = new Map();

module.exports = class MessagingChannelService
{
    constructor()
    {
    
    }
    
    async getChannel(node)
    {
        if( !channels.has( node.key ) )
        {
            let connection = await this._getConnection(node);
            
            let channelPromise = new Promise((resolve, reject) => {

                connection.createChannel(function(error, channel)
                {
                    channels.set(node.key, channel);

                    resolve(channel);
                });

            });
            
            await channelPromise;
        }

        return channels.get( node.key );

    }

    async _getConnection(node)
    {
        if( !connections.has( node.key ) ) 
        {   
            console.log(`connecting resource messagery: ${node.name} (${node.messagingBrokerAddress})`)

            let connectionPromise = new Promise((resolve, reject) => {
                
                let messageryUrl = node.messagingBrokerUsername && node.messagingBrokerPassword ? 
                `amqp://${node.messagingBrokerUsername}:${node.messagingBrokerPassword}@${node.messagingBrokerAddress}` : 
                `amqp://${node.messagingBrokerAddress}`;

                amqp.connect(messageryUrl, function(error0, connection) 
                {
                    if(error0)
                    {
                        console.error(error0);
                        return;
                    }
                    
                    connections.set(node.key, connection)

                    console.log(`connecting resource messagery: ${node.name} (${node.messagingBrokerAddress}) finished`)

                    resolve(connection);
                });

            });

            await connectionPromise; 
            
        }

        return connections.get( node.key );
    }
}