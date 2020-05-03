import React from 'react';
import { Link } from 'react-router-dom';
import NodeComponent from "../node-component/node-component";
import ClientService from "../../services/client-service";
import './client-component.css'


export default class ClientComponent extends NodeComponent
{
    constructor(props)
    {
        super(props);

        this.setState(
            {
                cams:[]
            }
        )
        
        let clientService = new ClientService();
    
        clientService.getCamerasFromClient(this.state.node.id).then(cams => {

            this.setState({
                cams: cams
            });
        
        });    

    }

    inheritedNodes()
    {
        return (<div className="content"> 

                    <div className="header" >
                        <h5>Câmeras</h5>
                    </div>

                    <table className="table">

                        <thead>
                            <tr>
                                <th scope="tl"> Nome </th>
                                <th scope="tl"> Endereço </th>
                                <th scope="tl"> Ações </th>
                            </tr>
                        </thead>

                        <tbody>
                            {
                                this.state.cams && this.state.cams.map( cam => 
                                    <tr>
                                        <td>{cam.name}</td>
                                        <td>{cam.address}</td>
                                        <td> <Link to={`/cam-stream?camId=${cam.id}&clientId=${this.state.node.id}`}>Visualizar</Link> </td>
                                    </tr>
                                )
                            }
                        </tbody>

                    </table>
            
                </div>)
    }
}