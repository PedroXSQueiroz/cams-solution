import React from 'react';
import ClientService from '../../services/client-service';

export default class CamViewComponent extends React.Component
{

    constructor(props)
    {
        super();

    }
    
    componentWillMount()
    {
        
        let queryParams = new URLSearchParams(this.props.location.search);

        console.log(this.props);
    
        let clientId = queryParams.has("clientId") ? queryParams.get('clientId') : null; 
        let camId = queryParams.has("camId") ? queryParams.get("camId") : null;

        this._clientService = new ClientService();

        this._clientService.startStream(clientId, camId).then(async stream => {
            
            const streamUrl = `ws://${process.env.REACT_APP_HUB_HOST}:${process.env.REACT_APP_HUB_OUT_PORT}/stream/${stream.id}`;
            
            this.setState({
                streamUrl: streamUrl,
                streamUrlEcoded: window.btoa(streamUrl)
            })

        });

        this.setState({
            camId: camId,
            clientId: clientId ,
            cam:{}
        });

    }

    render()
    {
        return (
            <div>
                
                <script src="/3rd-party/jsmpeg.min.js"></script>
                
                {
                    this.state.camId && this.state.clientId && this.state.streamUrl ?
                    
                    <object data={"/embeddable-viewer/index.html?stream=" + this.state.streamUrlEcoded} >
                    </object> 
                    :
                    <div>
                        Câmera ou Ponto de captura indefinido
                    </div>
                }
            </div>
        )
    }

}