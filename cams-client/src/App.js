import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

import './App.css';

import ServerService from './services/server-service';

import CamViewComponent from './components/cam-view-component/cam-view-component';
import CamsListComponent from './components/cams-list-component/cams-list-component';
import ServerListComponent from './components/server-list-component/server-list-component';
import HomeComponent from './components/home-component/home-component';

class App extends React.Component {

  constructor() {

    super();

    console.log(process.env.REACT_APP_RESOURCE_URL);
  }

  async getOwnData() {

    let serverService = new ServerService();

    let selfServerResponse = await serverService.self();

    return selfServerResponse;
  }

  componentWillMount() {
    
    this.setState({
      self:{}
    });
    
    this.getOwnData().then((selfServerResponse) => {
      
      this.setState({
        self: selfServerResponse
      });

    });

  }

  toggleMenu()
  {
    this.setState({
      menuVisible: !this.state.menuVisible
    })
  }

  render() {

    return (
      <div className="App">

        <Router>

          <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand" href="#">Cams</a>
            
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation" onClick={() => this.toggleMenu()}>
              <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarNav" style={{display: this.state.menuVisible ? 'block' : 'none'}}>
              
              <ul class="navbar-nav">
                
                
                <li class="nav-item active">
                  <Link class="nav-link" to="/">Pontos de Captura</Link>
                </li>
                <li class="nav-item">
                  <Link class="nav-link" to="/servers">Pontos de compartilhamento</Link>
                </li>
                <li class="nav-item">
                  <Link class="nav-link" to="/cams">Câmeras</Link>
                </li>
              </ul>
            </div>

          </nav>

          <div class="container container-fluid">

            <h1> {this.state.self.name} </h1>

            <Switch>

              <Route exact path="/" component={HomeComponent} />

              <Route path="/cam-stream" component={CamViewComponent} />

              <Route path="/cams" component={CamsListComponent} />

              <Route path="/servers" component={ServerListComponent} />

            </Switch>

          </div>

        </Router>

      </div>
    );

  }
}

export default App;
