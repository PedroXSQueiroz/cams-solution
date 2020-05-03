export default class ClientService
{
    async list()
    {
        let clientsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/clients/`);

        let response = await fetch(clientsRequest);

        return response.json();
    }

    async getCamerasFromClient(camId)
    {
        let camsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/clients/${camId}/cams`);

        let response = await fetch(camsRequest);

        return response.json();
    }

    async startStream(clientId, camId)
    {
        let camsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/clients/${clientId}/stream/${camId}`, {
            method: 'POST'
        });

        let response = await fetch(camsRequest);

        return response.json();
    }
}