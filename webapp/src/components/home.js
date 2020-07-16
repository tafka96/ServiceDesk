import {Component} from "react";
import React from "react";

export default class Home extends Component{
    render() {
        return(
            <div>
                <h1>Welcome to service desk application</h1>
                <p>In the menu on the right you can choose to open a new ticket or look at the details of currently open tickets. The tickets are organised by the priority</p>
            </div>
        )
    }
}
