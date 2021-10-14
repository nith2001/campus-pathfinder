/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import MapView from "./MapView";
import UserInput from "./UserInput";

interface AppState {
    path: any;
    validBuildings: any;
    resultMessage: string;
}

class App extends Component<{}, AppState> {

    constructor(props: {}) {
        super(props);
        this.makeRequestForBuildings();
        this.state = {
            path: {},
            validBuildings: {},
            resultMessage: "",
        }
    }

    // get list of buildings and set it as value of validBuildings in the state
    // JSON object format: (shortName: longName) for all buildings
    makeRequestForBuildings = async () => {
        try {
            // fetch the buildings data and wait for it to resolve
            let response = await fetch("http://localhost:4567/buildings");

            // check response
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }

            // convert building data to JSON, wait for it to resolve
            let buildings = await response.json();

            this.setState({
                // sets validBuildings to the list of abbreviated buildings names like KNE and BAG
                validBuildings: buildings
            });
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);  // Log error for debugging.
        }
    }

    // return path between start and end as an array
    // sets path in this.state as the returned path
    // for reference, we are given short names of start and end buildings
    makeRequestForPath = async (start: string, end: string) => {
        try {
            if (start !== "" || end !== "") {
                // fetch path
                let response = await fetch("http://localhost:4567/findPath?start=" + start + "&end=" + end);

                if (!response.ok) {
                    alert("This is the path to the same building and entrance. Try again! " +
                        "\nError: " + response.status);
                    return;
                }
                // convert path object to JSON
                let object = await response.json();
                if (object === null) {
                    // error message for no path
                    this.setState({
                        resultMessage: "No path was found between these two buildings. Try again!",
                    });
                } else {
                    // save constructed path and message
                    let path = object.path;
                    this.setState({
                        path: path,
                        resultMessage: "See below for the printed out path from " + start + " to " + end + ".",
                    });
                }
            }
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    }

    // if input is updated and is submitted, this function
    // is called to take the names of start and end to
    // call makeRequestForPath to get path between the two buildings
    updateStartAndEnd = (start: String, end: String) => {
        if (start === "" || end === "") {
            this.setState({
                path: [],
            });
            return;
        }

        let obj = this.state.validBuildings
        const startKey = Object.keys(obj).find(key => obj[key] === start)
        const endKey = Object.keys(obj).find(key => obj[key] === end)

        if (startKey === undefined || endKey === undefined) {
            alert("Error in retrieving values.")
        } else {
            this.makeRequestForPath(startKey, endKey);
        }
    }

    render() {
        // include blank option in array of buildings' long names
        let vals: String[] = Object.values(this.state.validBuildings);
        vals.unshift(""); // adds empty string to the beginning of vals array
        return (
            <div>
                <UserInput onChange={this.updateStartAndEnd} value={vals}/>
                <p>{this.state.resultMessage}</p>
                <MapView path={this.state.path}/>
            </div>
        );
    }

}

export default App;
