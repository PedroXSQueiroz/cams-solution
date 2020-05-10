import React from 'react';
import ClientService from '../../services/client-service';
import CamService from '../../services/cam-service';

import './cam-view-component.css'

export default class CamViewComponent extends React.Component
{

    constructor(props)
    {
        super();

        this._clientService = new ClientService();
        this._camService = new CamService();

    }
    
    componentWillMount()
    {
        
        let queryParams = new URLSearchParams(this.props.location.search);

        console.log(this.props);
    
        let clientId = queryParams.has("clientId") ? queryParams.get('clientId') : null; 
        let camId = queryParams.has("camId") ? queryParams.get("camId") : null;


        this._clientService.startStream(clientId, camId).then(async stream => {
            
            const streamUrl = `ws://${process.env.REACT_APP_HUB_HOST}:${process.env.REACT_APP_HUB_OUT_PORT}/stream/${stream.id}`;
            
            this.setState({
                cam: stream.cam,
                streamUrl: streamUrl,
                streamUrlEcoded: window.btoa(streamUrl)
            })

        });

        this.setState({
            camId: camId,
            clientId: clientId,
            cam:{}
        });

    }

    render()
    {
        return (
            <div>
                
                <div>
                    <h2>{this.state.cam.name}</h2>
                </div>
                
                {
                    this.state.camId && this.state.clientId && this.state.streamUrl ?
                    
                    <object className="cam-view-container" data={"/embeddable-viewer/index.html?stream=" + this.state.streamUrlEcoded} >
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