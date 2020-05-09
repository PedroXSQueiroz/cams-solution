export default class CamService
{

    async list()
    {
        let camsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/cams/`);

        let response = await fetch(camsRequest);

        return response.json();
    }

    async update(cam)
    {
        let camsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/cams/${cam.id}`, {
            method: 'PUT',
            headers:{
                'Content-Type':'application/json'
            },
            body: JSON.stringify(cam),
        });

        let response = await fetch(camsRequest);

        return response.json();
    }

    async create(cam)
    {
        let camsRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/cams`, {
            method: 'POST',
            headers:{
                'Content-Type':'application/json'
            },
            body: JSON.stringify(cam),
        });

        let response = await fetch(camsRequest);

        return response.json();
    }

    async delete(cam)
    {
        let deeleteRequest = new Request(`${process.env.REACT_APP_RESOURCE_URL}/cams/${cam.id}`, {
            method: 'DELETE'
        });

        let response = await fetch(deeleteRequest);
    }

}