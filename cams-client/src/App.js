import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";

import logo from './logo.svg';
import './App.css';

import ServerService from './services/server-service';

import CamViewComponent from './components/cam-view-component/cam-view-component';
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


  render() {

    return (
      <div className="App">

        <Router>

          <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <a class="navbar-brand" href="#">Cams</a>
            
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarNav">
              
              <ul class="navbar-nav">
                
                
                <li class="nav-item active">
                  <Link class="nav-link" to="/">Pontos de Captura<span class="sr-only">(current)</span></Link>
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

            </Switch>

          </div>

        </Router>

        <div>

        </div>


      </div>
    );

  }
}

export default App;
