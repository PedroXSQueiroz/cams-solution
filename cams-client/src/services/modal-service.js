import React from 'react';
import ReactDOM from 'react-dom'

const modalStack = [];

/**
 * @param title
 * @param body
 * @param finishCallback
 * @param okLabel
 */
class Modal extends React.Component
{
    
    dismiss()
    {
        
        this.props.container.remove(this);
        
    }
    
    finish()
    {
        return () => {
            if(this.props.finishCallback)
            {
                this.props.finishCallback();
            }
            
            this.dismiss();
        }
    }
    
    render()
    {
        return (
            
            <div className="modal" aria-hidden="false" style={{display:'block'}} >

                <div className="modal-dialog" role="document">

                    <div className="modal-content">

                        <div className="modal-header">

                            <h5>
                                {this.props.title}
                            </h5>

                            <button type="button" class="close" aria-label="Close" onClick={() => this.dismiss()}>
                                <span aria-hidden="true">&times;</span>
                            </button>

                        </div>

                        <div class="modal-body">
                            {this.props.body}
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" onClick={this.finish()} >{ this.props.okLabel || 'OK' }</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal" onClick={() => this.dismiss()}>Cancelar</button>
                        </div>

                    </div>

                </div>

            </div>
        );
    }

}

class ModalContainer extends React.Component
{
    
    constructor()
    {
        super();

        this.state = {stack:[]};
    }

    push(modal)
    {
        modalStack.push(modal);
        
        this.setState({
            stack: modalStack
        })
    }

    remove(modal)
    {
        console.log('removing modal');
        
        let modalIndex = modalStack.indexOf(modal);
        modalStack.splice(modalIndex, 1);
        
        console.log(`modal index:${modalIndex}`)

        this.setState({
            stack: [ ... modalStack]
        });
    }
    
    
    render()
    {
        return (
            <div className="modal-background" hidden={this.state.stack.length == 0} style={{'backgroundColor': '#000000cc'}} >

                {this.state.stack}

            </div>)
    }
}

export default class ModalService 
{
    
    constructor()
    {

    }
    
    /**
     * @param title
     * @param body
     * @param finishCallback
     * @param okLabel
     */
    push(title, body, finishCallback, okLabel)
    {   
        if(this._container)
        {
            this._container.push(
                <Modal
                    container={this._container}
                    title={title}
                    body={body}
                    finishCallback={finishCallback}
                    okLabel={okLabel}
                />
            );
        }
    }

    renderModals(parent)
    {
        console.log('container rendered');
        
        this._container = ReactDOM.render(<ModalContainer/>, parent);        
    }
}