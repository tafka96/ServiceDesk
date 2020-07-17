import React from "react";
import { render, unmountComponentAtNode } from "react-dom";
import { act } from "react-dom/test-utils";
import { HashRouter as Router } from 'react-router-dom';
import OpenTickets from "../components/tickets";


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

it("shows correct titles on the tickets list", async () => {
    let fakeList = [{"title":"Title for higher priority problem","id":2,"email":"email1@gmeil.com","problem":"High Priority problem description","priority":"HIGH","closed":false,"createdDate":"2020-07-16","closedDate":null},{"title":"Title for low prior problem","id":1,"email":"email1@gmeil.com","problem":"Low Priority problem description","priority":"LOW","closed":false,"createdDate":"2020-07-16","closedDate":null}]
    global.fetch = jest.fn(() =>
        Promise.resolve({
            json: () => Promise.resolve(fakeList)
        })
    );
    await act(async () => {
        render(<Router><OpenTickets /></Router>, container);
    });
    expect(fetch).toHaveBeenCalledTimes(1);
    expect(container.querySelectorAll("li span")[0].textContent).toBe(fakeList[0].title);
    expect(container.querySelectorAll("li span")[1].textContent).toBe(fakeList[1].title);
    fetch.mockClear()

});

it("should show error message when failed to load tickets", async () => {
    global.fetch = jest.fn(() =>
        Promise.resolve({
            json: () => Promise.reject()
        })
    );
    await act(async () => {
        render(<Router><OpenTickets /></Router>, container);
    });
    expect(fetch).toHaveBeenCalledTimes(1);
    expect(container.querySelector(".error-message").textContent).toBe("Failed to load");
    fetch.mockClear()
});
