import React, { useState } from 'react';

import ServerService from '../../services/server-service';
import ModalService from '../../services/modal-service';


function ServerForm(props){
    
    let server = props.server;

    const [urlServerState, setUrlServerState] = useState('http://exemplo:8080');
    const handleUrl = e => {
        const address = e.target.value;
        server.address = address;
        setUrlServerState(address);
    }
    
    return (
        <div>
            <div className="form-group">
                <label for="address" >Endereço</label>
                <input className="form-control" name="address" id="address" value={urlServerState} onChange={handleUrl}/>
            </div>
        </div>
    )

}

export default class ServerListComponent extends React.Component
{

    constructor()
    {
        super();
        
        this._serverService = new ServerService();
        this._modalService = new ModalService();

        this.state = {};
    }

    async componentDidMount()
    {
        await this._listServers();

        this._modalService.renderModals(document.getElementById("servers-list-modal-container"));
    }

    async _listServers() {
        let servers = await this._serverService.list();
        this.setState({
            servers: servers
        });
    }

    create()
    {
        let newServer = {};
        
        this._modalService.push(
            'Registrar Ponto de Copartilhamento'
            , <ServerForm server={newServer} />
            ,async () => {
                await this._serverService.create(newServer);
                await this._listServers();
            }
            , 'Confirmar')
    }

    async delete(server)
    {
        await this._serverService.delete(server);
        this._listServers();
    }
    
    render()
    {
        return (

            <div>
                <div className="list-header">
                    <h2>Pontos de Comaprtilhamento</h2>
                    <i class="fas fa-plus-square fa-2x btn text-primary" onClick={() => this.create()}></i>
                </div>

                {
                    this.state.servers ?
                        <div>

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
                                        this.state.servers && this.state.servers.map(server =>
                                            <tr>
                                                <td>{server.name}</td>
                                                <td>{server.address}</td>    
                                                <td>

                                                    <i class="fas fa-trash-alt"onClick={() => this.delete(server)} ></i>

                                                    {/* <i class="fas fa-edit" onClick={() => this.edit(server)} ></i>
                                                    <i class="fas fa-trash-alt"onClick={() => this.delete(server)} ></i> */}
                                                </td>
                                                
                                            </tr>
                                        )
                                    }
                                </tbody>

                            </table>

                        </div> :
                        <span>Não há pontos de compartilhamento registrados</span>
                }

                <div id="servers-list-modal-container" />

            </div>

        )
    }

}