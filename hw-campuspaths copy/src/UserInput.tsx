import React, {Component} from 'react';

// String is the JS string type
interface UserInputProps {
    onChange(start: String, end: String): void;
    value: String[];
}

// string is the TS string type for typing variables and params
interface UserInputState {
    start: string;
    end: string;
}

// handles all user input
class UserInput extends Component<UserInputProps, UserInputState> {
    constructor(props: UserInputProps) {
        super(props);
        this.state = {
            start: "",
            end: "",
        }
    }

    // calls given onChange function to pass updated input
    // values to App class whenever submit is clicked
    onClickSubmit = () => {
        if (this.state.start === "" || this.state.end === "") {
            alert("Must have two buildings to find the path!");
        } else {
            this.props.onChange(this.state.start, this.state.end)
        }
    }

    // resets program when clear button is clicked
    // accomplished by calling onChange with empty strings
    // so that the path is empty, thus, no path is drawn on the map
    onClickClear = () => {
        this.setState({
            start: "",
            end: ""
        })
        this.props.onChange("", "");
    }
    
    // updates state whenever input for start is changed
    onInputStartChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        let start: string = event.target.value;
        this.setState({
            start: start
        });
        console.log(this.state.start);
    }
    
    // update state whenever input for dest is changed
    onInputDestChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        let end: string = event.target.value;
        this.setState({
            end: end
        });
        console.log(this.state.end);
    }
    
    render() {
        return (
            <div id="input">
                <br/>
                Start:
                <select value={this.state.start} onChange={this.onInputStartChange}>
                    {this.props.value.map((x, y) => <option key={y}>{x}</option>)}
                </select>
                <br/>
                Destination:
                <select value={this.state.end} onChange={this.onInputDestChange}>
                    {this.props.value.map((x, y) => <option key={y}>{x}</option>)}
                </select>
                <br/>
                <br/>
                <button onClick={() => {
                    this.onClickSubmit()
                }}>
                    Submit
                </button>
                <button onClick={() => {
                    this.onClickClear()
                }}>
                    Clear
                </button>
            </div>
        )
    }
}

export default UserInput;