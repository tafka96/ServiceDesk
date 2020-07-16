import React from "react";
import {updateList} from "./tickets";

export default class TicketDetails extends React.Component{
    constructor(props) {
        super(props);
        this.state = {ticket:{title:'', email:'', problem:''}, errors: [], id:"", loading:[], priorities: [], successMessage:''};
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.loadPriorities()
        this.loadTicket()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if(this.props!==prevProps) {
            this.loadTicket()
        }
    }

    loadPriorities(){
        this.setState({loading:true})
        fetch("/api/priorities", { mode: "same-origin"})
            .then(res => res.json())
            .then(res =>{
                this.setState({priorities: res, loading:false})
            })
            .catch(() => {this.setState({errors : ["Could not connect to server"], loading:false})})
    }

    loadTicket(){
        let new_id = this.props.match.params.id
        this.setState({id: new_id, loading: true})
        fetch(`/api/tickets/${new_id}`, {mode:"same-origin"})
            .then(res=> res.json())
            .then((data) => {
                this.setState({ticket:data, loading: false, successMessage:this.props.location.message})
            })
            .catch(() =>{
                this.setState({loading: false, error:["Could not load ticket"]})
            })
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        this.setState(prevState=>({
            ...prevState,
            ticket:{
                ...prevState.ticket,
                [name]:value
            }
        }));
    }

    handleSubmit(event) {
        event.preventDefault();
        fetch("/api/tickets/update", {
            method: 'POST',
            mode: 'same-origin',
            body: JSON.stringify(this.state.ticket),
            headers:{'Content-Type':'application/json'}
        }).then(res => res.json()).then(res =>{
                if (res.errors){
                    this.setState({errors: res.errors, successMessage:''})
                }
                else {
                    updateList()
                    this.setState({errors: [], successMessage:"Details updated"})
                }
            })
            .catch(() => this.setState({errors : ["Failed to update ticket"]}))
    }

    closeTicket(){
        let id = this.state.ticket.id
        fetch(`/api/tickets/close/${id}`, {mode:"same-origin"})
            .then(res=> res.json())
            .then((data) => {
                this.setState({ticket:data, loading: false, successMessage: "Ticket closed"})
                updateList()
            })
            .catch(() =>{
                this.setState({loading: false, errors: ["Could not close ticket"]})
            })
        updateList()
    }

    render() {
        let ticket = this.state.ticket
        let priorities = this.state.priorities
        let isClosed = this.state.ticket.closed
        let isSuccess = this.state.successMessage && this.state.successMessage!=='';
        let successMessages ='';
        if (isSuccess){
            successMessages = <div className="success-message">{this.state.successMessage}</div>
        }
        let errorMessages = '';
        if (this.state.errors.length>0){
            errorMessages = this.state.errors.map(msg => <div className="error-message" key={msg}>{msg}</div>)
        }
        return (
            <div>
            <div className="ticket-form">
                <form onSubmit={this.handleSubmit}>
                    <div className="form ticket-id"><h2>Ticket: {ticket.id}</h2></div>
                    <div className="form input ticket-title">Title: <input name="title" type="text" disabled={isClosed} size="50" value={ticket.title} onChange={this.handleInputChange}/></div>
                    <div className="form input ticket-email">Email: <input name="email" type="email" size="50" disabled={isClosed} value={ticket.email} onChange={this.handleInputChange}/></div>
                    <div className="form ticket-problem"><label>Problem:<textarea name="problem" cols="50" rows="5" disabled={isClosed} value={ticket.problem} onChange={this.handleInputChange}/></label></div>
                    <div className="form ticket-priority">Priority:<select name="priority" disabled={isClosed} value={ticket.priority} onChange={this.handleInputChange}>
                        {priorities.map(p=>
                            <option key={p} value={p}>{p}</option>
                        )}
                    </select></div>
                    <div className="form">Created: {ticket.createdDate}</div>
                    <div className="form" hidden={!isClosed}>Closed: {ticket.closedDate}</div>
                    <div className="form-submit">
                        <input hidden={isClosed} type="submit" value="Update"/>
                        <button type="button" className="close-button" hidden={isClosed} onClick={this.closeTicket.bind(this)}>Close</button>
                    </div>


                </form>
            </div>

                {errorMessages}
                {successMessages}
            </div>

        )
    }
}
