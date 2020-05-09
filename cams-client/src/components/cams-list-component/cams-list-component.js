import React, { useState } from 'react';
import { Link } from 'react-router-dom';

import CamService from '../../services/cam-service';
import ClientService from '../../services/client-service';
import ServerService from '../../services/server-service';
import ModalService from '../../services/modal-service';

import './cams-list-component.css';

function CamForm(props){
                 
    let cam = props.cam;
    
    const [camState, setCamState] = useState(cam);
    const handleCam = e => {
        
        cam[e.target.name] = e.target.value;
        let newCamState = { [e.target.name] : e.target.value };
        
        setCamState(newCamState)
    }
    

    return (
                <div>
                    <div className="form-group">
                        <label for="camName" >Nome</label>
                        <input className="form-control" name="name" value={camState.name} onChange={handleCam}/>
                    </div>

                    <div className="form-group">
                        <label>Endereço</label>
                        <input className="form-control" name="address" value={camState.address} onChange={handleCam}/>
                    </div>
                </div>
            )
};

export default class CamsListComponent extends React.Component {

    constructor() {
        super();

        this._camService = new CamService();
        this._clientService = new ClientService();
        this._serverService = new ServerService();
        this._modalService = new ModalService();

        this.state = {};
    }

    async componentDidMount() {

        await this._listCams();

        this._modalService.renderModals(document.getElementById("cams-list-modal-container"));

    }

    async _listCams() {
        
        if (this.props.node) 
        {
            
            let cams = await this._clientService.getCamerasFromClient(this.props.node.key);
            
            this.setState({
                cams: cams
            });
        }
        else 
        {
            let self = await this._serverService.self();
            
            let cams = await this._camService.list();
            
            this.setState({
                cams: cams,
                self: self
            });
        }
    }

    edit(cam) {
            
        this._modalService.push(
            'Editar câmera'
            , <CamForm cam={cam}/>
            ,async () => {
                await this._camService.update(cam);
                await this._listCams();
            }
            , 'Confirmar');
        
    }

    create()
    {
        let newCam = {};
        
        this._modalService.push(
            'Criar câmera'
            , <CamForm cam={ newCam }/>
            ,async () => {
                await this._camService.create(newCam);
                await this._listCams();
            }
            , 'Confirmar')
        
    }

    async delete(cam)
    {
        await this._camService.delete(cam);
        this._listCams();
    }

    render() {
        return (
            <div>
                <div className="list-header">
                    <h2>Câmeras</h2>
                    <i class="fas fa-plus-square fa-2x btn text-primary" onClick={() => this.create()}></i>
                </div>

                {
                    this.state.cams ?
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
                                        this.state.cams && this.state.cams.map(cam =>
                                            <tr>
                                                <td>{cam.name}</td>
                                                <td>{cam.address}</td>
                                                {
                                                    this.props.node ?
                                                        <td>
                                                            <Link to={`/cam-stream?camId=${cam.id}&clientId=${this.props.node ? this.props.node.id : 'nada-ainda'}`}>
                                                                <i class="fas fa-video" ></i>
                                                            </Link>
                                                        </td> :
                                                        <td>
                                                            <i class="fas fa-edit" onClick={() => this.edit(cam)} ></i>
                                                            <i class="fas fa-trash-alt"onClick={() => this.delete(cam)} ></i>
                                                        </td>
                                                }
                                            </tr>
                                        )
                                    }
                                </tbody>

                            </table>

                        </div> :
                        <span>Não há câmeras registradas</span>
                }

                <div id="cams-list-modal-container" />

            </div>
        )
    }

}