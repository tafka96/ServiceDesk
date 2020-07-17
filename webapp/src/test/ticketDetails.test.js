import React from "react";
import { render, unmountComponentAtNode} from "react-dom";
import { act } from "react-dom/test-utils";

import TicketDetails from "../components/ticketDetails";

let container = null;
beforeEach(() => {
    container = document.createElement("div");
    document.body.appendChild(container);
});

afterEach(() => {
    unmountComponentAtNode(container);
    global.fetch.mockRestore();
    container.remove();
    container = null;
});

it("loads ticket with correct value", async () => {
    let priorities = ["LOW", "HIGH"]
    let ticket = {"title":"Title for low prior problem","id":1,"email":"email1@gmeil.com","problem":"Low Priority problem description","priority":"LOW","closed":false,"createdDate":"2020-07-17","closedDate":null}
    global.fetch = jest.fn().mockImplementationOnce(() =>
        Promise.resolve({
            json: () => Promise.resolve(priorities)
        })).mockImplementationOnce(() =>
        Promise.resolve({
            json: () => Promise.resolve(ticket)
        })
    );

    await act(async () => {
        render(<TicketDetails {...{match:{params:{id:1}}, location: {message:""}}}/>, container);
    });
    expect(fetch).toHaveBeenCalledTimes(2);
    expect(container.querySelectorAll("option").length).toBe(priorities.length);
    expect(container.querySelectorAll("option")[0].textContent).toBe(priorities[0]);
    expect(container.querySelectorAll("option")[1].textContent).toBe(priorities[1]);
    expect(container.querySelector("h2").textContent).toBe("Ticket: 1");
    expect(container.querySelector("input").disabled).toBe(false);
    fetch.mockClear();

});
