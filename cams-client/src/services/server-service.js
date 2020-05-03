export default class ServerService 
{

    async self()
    {
        
        let selfResourceDataRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/servers/self`);

        let response = await fetch(selfResourceDataRequest);

        return response.json();

    }

}