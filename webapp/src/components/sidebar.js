import React from "react";
import OpenTickets from "./tickets";
import {NavLink} from "react-router-dom";

export default class Sidebar extends React.Component{
    render() {
        return(
            <div className="menu">
                <div className="menu-header">Service Desk</div>
                <div className="new-ticket-button">
            <NavLink  to="/add/ticket">New Ticket</NavLink>
                </div>
            <OpenTickets/>
            </div>
        )
    }
}
