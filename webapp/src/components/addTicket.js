import React from "react";
import {updateList} from "./tickets";
import {Redirect} from "react-router-dom"
import Loader from "react-loader-spinner";



export default class AddTicket extends React.Component{
    constructor(props) {
        super(props);
        this.state = {ticket: {title:'', email: '', problem: ''}, errors: [], priorities: [], loading:false, successful: false};
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

    }
    componentDidMount() {
        this.loadPriorities()
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
        fetch("/api/tickets/add", {
            method: 'POST',
            mode: "same-origin",
            body: JSON.stringify(this.state.ticket),
            headers:{'Content-Type':'application/json'}
        }).then(res => res.json())
            .then(res =>{
                if (res.errors){
                    this.setState({errors: res.errors})
                }
                else {
                    updateList()
                    this.setState({successful: true, redirectUrl: '/tickets/'+res.id})
                }
            })
            .catch(() => {this.setState({errors : ["Failed to add ticket"]})})
    }


    render() {
        let ticket = this.state.ticket
        let priorities = this.state.priorities
        let errorMessages = '';
        if (this.state.errors.length>0){
            errorMessages = this.state.errors.map(msg => <div className="error-message" key={msg}>{msg}</div>)
        }
        let loadingComponent = ''
        if (this.state.loading){loadingComponent = <Loader type="ThreeDots" color="#00BFFF" height={100} width={100}/>
        }
        const { from } = this.props.location.state || '/'
        this.state.priorities.map(p=>{
            return {value: p, label: p}});
        return (
            <div>
                <form hidden={this.state.loading} onSubmit={this.handleSubmit}>
                    <div className="form ticket-id"><h2>New Ticket</h2></div>
                    <div className="form input ticket-title">Title: <input name="title" type="text" placeholder="Title" size="50" value={ticket.title} onChange={this.handleInputChange}/></div>
                    <div className="form input ticket-email">Email: <input name="email" type="email" placeholder="Email" size="50" value={ticket.email} onChange={this.handleInputChange}/></div>
                    <div className="form ticket-problem"><label>Problem:<textarea name="problem" placeholder="Problem description" cols="50" rows="5" value={ticket.problem} onChange={this.handleInputChange}/></label></div>
                    <div className="form ticket-priority">Priority:<select name="priority" defaultValue="DEFAULT" value={ticket.priority} onChange={this.handleInputChange}>
                        <option value="DEFAULT">Priority</option>
                        {priorities.map(p=>
                            <option key={p} value={p}>{p}</option>
                        )}
                    </select></div>
                    <div className="form-submit"><input type="submit" value="Create" /></div>
                </form>
                {loadingComponent}
                {errorMessages}
                {this.state.successful && (
                    <Redirect to={{pathname : from || this.state.redirectUrl, message: "Ticket added"}}/>
                )}
            </div>
        )
    }
}

