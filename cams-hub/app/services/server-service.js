const request = require('request');
const RESOURCES_CONFIG = require('../config/resources-config');

module.exports = class ServerService
{
    
    async list()
    {
        return await new Promise((resolve, reject) => {

            request(`${RESOURCES_CONFIG.URL}/servers/`, 
            {
                method: 'GET',
            },(err, res, body) => {

                console.log(body);
                
                resolve(JSON.parse( body ) );

            });

        })
    }

    async get(key)
    {
        return await new Promise((resolve, reject) => {

            request(`${RESOURCES_CONFIG.URL}/servers/${key}`, 
            {
                method: 'GET',
            },(err, res, body) => {

                console.log(body);
                
                resolve(JSON.parse( body ) );

            });

        })
    }

    async self()
    {
        return await new Promise((resolve, reject) => {

            console.log(`trying connection to: ${RESOURCES_CONFIG.URL}`);

            request(`${RESOURCES_CONFIG.URL}/servers/self/`, 
            {
                method: 'GET',
            },(err, res, body) => {

                if(err)
                {
                    console.error(err);
                }else
                {
                    const jsonBody = JSON.parse(body);
                    
                    console.log( jsonBody );
                    
                    resolve( jsonBody );

                    console.log(`sucessfully connected to: ${RESOURCES_CONFIG.URL}`)

                }
            });

        })
    }

}