import React from 'react'
import {NavLink} from "react-router-dom";
import Loader from 'react-loader-spinner'


export default class OpenTickets extends React.Component{
    constructor(props) {
        super(props);
        this.state = {tickets: [], loading: false, errors: []}
    }

    componentDidMount(){
        updateList=updateList.bind(this)
        this.getOpenTickets()
    }

    getOpenTickets(){
        this.setState({loading:true})
        fetch('/api/tickets', {mode:"same-origin"})
            .then(res=> res.json())
            .then((data) => {
                this.setState({tickets: data, loading:false})
            })
            .catch(err => {
                this.setState({errors: ["Failed to load"], loading:false})})
    }

    render() {
        let errorMessages = ''
        if (this.state.errors.length>0){
            errorMessages = this.state.errors.map(msg => <div className="error-message" key={msg}>{msg}</div>)
        }
        let loadingComponent = ''
        if (this.state.loading){
            loadingComponent = <Loader
                type="ThreeDots"
                color="#00BFFF"
                height={100}
                width={100}
            />
        }
        const tickets = this.state.tickets.map((ticket) =>(
            <NavLink key={ticket.id} to={`/tickets/${ticket.id}`}><li> <img src={`../${ticket.priority}.png`} alt="No logo found"/> <span>{ticket.title}</span></li></NavLink>

        ));
        return (
            <div className="open-tickets">
                <span>Open Tickets:</span>
                <ul className="tickets-list">{tickets}</ul>
                {loadingComponent}
                {errorMessages}
            </div>
        );
    }
}
export function updateList() {
    this.getOpenTickets();
}
