import React from "react";
import { render, unmountComponentAtNode} from "react-dom";
import { act } from "react-dom/test-utils";
import {fireEvent} from "@testing-library/react";

import AddTicket from "../components/addTicket";

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

it("loads priorities with default value", async () => {
    let priorities = ["LOW", "HIGH"]
    global.fetch = jest.fn().mockImplementationOnce(() =>
        Promise.resolve({
            json: () => Promise.resolve(priorities)
        })
    );

    await act(async () => {
        render(<AddTicket {...{location:"sadasd"}}/>, container);
    });
    expect(fetch).toHaveBeenCalledTimes(1);
    expect(container.querySelectorAll("option").length).toBe(priorities.length+1);
    expect(container.querySelectorAll("option")[1].textContent).toBe(priorities[0]);
    expect(container.querySelectorAll("option")[2].textContent).toBe(priorities[1]);
    fetch.mockClear();

});

it("shows error messages", async () => {
    let priorities = ["LOW", "HIGH"]

    global.fetch = jest.fn().mockImplementationOnce(() =>
        Promise.resolve({
            json: () => Promise.resolve(priorities)
        })).mockImplementationOnce(() =>
            Promise.resolve({
                json: () => Promise.resolve({errors:["ERROR1", "ERROR2"]})
            })
    );
    await act(async () => {
        render(<AddTicket {...{location:"location"}}/> , container);
        fireEvent.submit(container.querySelector("input[type=submit]"))

    });

    // await act(async () => {
    //
    //
    // });

    expect(fetch).toHaveBeenCalledTimes(2);
    expect(container.querySelectorAll(".error-message")[0].textContent).toBe("ERROR1");
    expect(container.querySelectorAll(".error-message")[1].textContent).toBe("ERROR2");
    fetch.mockClear();

});
