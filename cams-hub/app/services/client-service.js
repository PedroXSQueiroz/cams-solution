const request = require('request');
const RESOURCES_CONFIG = require('../config/resources-config');

module.exports = class ClientService
{

    async get(key)
    {
        return await new Promise((resolve, reject) => {

            request(`${RESOURCES_CONFIG.URL}/clients/${key}`, 
            {
                method: 'GET',
            },(err, res, body) => {

                console.log(body);
                
                resolve(JSON.parse( body ) );

            });

        })
    }

}