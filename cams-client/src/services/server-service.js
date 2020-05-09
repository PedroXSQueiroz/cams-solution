export default class ServerService 
{

    async self()
    {
        
        let selfResourceDataRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/servers/self`);

        let response = await fetch(selfResourceDataRequest);

        return response.json();

    }

    async list()
    {
        let selfResourceDataRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/servers/`);

        let response = await fetch(selfResourceDataRequest);

        return response.json();
    }

    async create(server)
    {
        let serverRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/servers/`, {
            method: 'POST',
            headers:{
                'Content-Type':'application/json'
            },
            body: JSON.stringify(server),
        });

        let response = await fetch(serverRequest);

        return response.json();
    }

}