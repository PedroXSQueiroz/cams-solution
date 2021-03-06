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

        let streamStartedCallback = async stream => {
            
            console.log('stream loaded');

            const streamUrl = `ws://${process.env.REACT_APP_HUB_HOST}:${process.env.REACT_APP_HUB_OUT_PORT}/stream/${stream.id}`;
            
            this.setState({
                cam: stream.cam,
                streamUrl: streamUrl,
                streamUrlEcoded: window.btoa(streamUrl),
                isRecording: false
            });

        }

        clientId ? 
            this._clientService.startStream(clientId, camId).then(streamStartedCallback) : 
            this._camService.stream(camId).then(streamStartedCallback);

        this.setState({
            camId: camId,
            clientId: clientId,
            cam:{}
        });

    }

    async toogleRecord()
    {
        
        const toogled = !this.state.isRecording;

        if(this.clientId)
        {
            await this._clientService.setStreamRecord(toogled, this.state.camId, this.state.clientId );
        }
        else
        {
            await this._camService.setStreamRecord(toogled, this.state.camId);
        }

        this.setState({isRecording: toogled});

    }

    render()
    {
        return (
            <div>
                
                {
                    this.state.cam && this.state.streamUrl ? 
                        <div>
                            <div>
                                <h2>{this.state.cam.name}</h2>
                            </div>
                            
                            {
                                this.state.camId && this.state.streamUrl ?
                                
                                <object className="cam-view-container" data={"/embeddable-viewer/index.html?stream=" + this.state.streamUrlEcoded} >
                                </object> 
                                :
                                <div>
                                    Câmera ou Ponto de captura indefinido
                                </div>
                            }

                            {
                                this.state.camId && this.state.streamUrl ?
                                <div className="form-check"> 
                                    <input className="form-check-input" type="checkbox" onChange={() => this.toogleRecord()} />
                                    <label class="form-check-label"> Gravando </label>
                                </div> : null
                            }           
                        </div> :
                        <div>
                            <h2>Carregando</h2>
                        </div>
                }

               
            </div>
        )
    }

}