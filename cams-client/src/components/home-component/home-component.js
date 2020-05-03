import React from 'react'

import ServerService from '../../services/server-service';
import ClientService from '../../services/client-service';

import ClientComponent from '../../components/client-component/client-component';

export default class HomeComponent extends React.Component {

    constructor()
    {
        super();
    }
    
    async getOwnData() {

        let serverService = new ServerService();

        let selfServerResponse = await serverService.self();

        return selfServerResponse;
    }

    async getClients() {
        
        let clientService = new ClientService();

        let clients = await clientService.list();

        return clients;

    }

    componentWillMount() {
        
        this.setState({
            self:{},
            clients: []
        });
        
        Promise.all(
            [
                this.getOwnData(),
                this.getClients()
            ]
        ).then( (data) => {
            
            let selfServerResponse = data[0];
            let clients = data[1];

            this.setState({
                self: selfServerResponse,
                clients: clients
            });
        
        });
        
    }

    render() {
        return (

            
            <div>
                <h2> Pontos de Captura </h2>
                
                {this.state.clients && this.state
                    .clients.map(client => 
                        <ClientComponent node={client} >
                        </ClientComponent>)
                }
                
            </div>

        )
    }

}