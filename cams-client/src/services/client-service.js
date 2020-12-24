export default class ClientService
{
    async list()
    {
        let clientsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/clients/`);

        let response = await fetch(clientsRequest);

        return response.json();
    }

    async getCamerasFromClient(clientId)
    {
        let camsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/clients/${clientId}/cams`);

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

    async setStreamRecord(start, camId, clientId)
    {
        let setRecordStreamRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/clients/${clientId}/cam/${camId}/record/${start}?path=/videos/gravacao.mp4`, {
            method: 'POST'
        });

        let response = await fetch(setRecordStreamRequest);

        return response.json();
    }
}