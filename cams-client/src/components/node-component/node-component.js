import React from 'react';
import './node-component.css'

export default class NodeComponent extends React.Component {

    constructor(props) {
        super();

        this.state = {
            node: props.node
        };
    }

    componentWillMount()
    {

    }

    inheritedNodes() {
        throw ' inheritedNodes Not implemented'
    }

    toggleAccorddion = () => {
        this.setState({ accordionOpen: !this.state.accordionOpen });
    }

    render() {

        return (
            <div>
                {this.state.node &&
                    <div className="card">

                        <div className="card-header" onClick={this.toggleAccorddion} >
                            
                            <h4>{this.state.node.name}</h4>

                            <i className={"fas " + ( this.state.accordionOpen ? "fa-angle-up" : "fa-angle-down" ) } onClick={this.toggleAccorddion} ></i>
                            
                        </div>

                        <div
                            className="card-body"
                            hidden={!this.state.accordionOpen} >

                            {this.inheritedNodes()}

                        </div>

                    </div>}
            </div>
        )
    }
}