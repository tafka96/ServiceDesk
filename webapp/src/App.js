import React, {Component} from 'react';
import Sidebar from "./components/sidebar";
import { Route, HashRouter, Redirect} from 'react-router-dom'
import './App.css';
import TicketDetails from "./components/ticketDetails";
import AddTicket from "./components/addTicket";
import Home from "./components/home"

class App extends Component{
    render(){
        return(
            <HashRouter>
                <div className="main">
                    <Sidebar/>
                    <div className="content">
                        {<Route exact path="/tickets/:id" component={TicketDetails}/>}
                        {<Route exact path="/add/ticket" component={AddTicket}/>}
                        {<Route exact path="/" component={Home}/>}
                    </div>
                </div>
            </HashRouter>
    )}
}

export default App;
