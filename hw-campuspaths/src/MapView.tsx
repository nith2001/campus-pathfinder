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
import "./MapView.css";

interface MapViewState {
    backgroundImage: HTMLImageElement | null;
}

interface MapViewProps {
    path: any;
}

class MapView extends Component<MapViewProps, MapViewState> {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    canvas: React.RefObject<HTMLCanvasElement>;

    constructor(props: MapViewProps) {
        super(props);
        this.state = {
            backgroundImage: null
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.fetchAndSaveImage()
        this.drawPathOnBackground();
    }

    // redraws the canvas with the background image
    // and the path in this.props whenever there's a change in the props
    componentDidUpdate() {
        this.drawPathOnBackground();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }

    drawBackgroundImage() {
        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);
        }
    }

    // Draws background image and then draws given path on top of it
    drawPathOnBackground = () => {
        this.drawBackgroundImage();
        if (this.props.path.length > 0) {
            let canvas = this.canvas.current;
            if (canvas === null) throw Error("Unable to draw, no canvas ref.");
            let ctx = canvas.getContext("2d");
            if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
            let path = this.props.path;
            let start = path[0];
            ctx.beginPath()
            ctx.strokeStyle = "Red";
            ctx.lineWidth = 15;
            // path provides x and y coordinates including decimals
            // since we only want pixels, round to nearest pixel
            ctx.moveTo(Math.round(start.start.x), Math.round(start.start.y));

            for (let i = 0; i < path.length; i++) {
                let point = path[i];
                ctx.lineTo(Math.round(point.end.x), Math.round(point.end.y));
            }
            ctx.stroke();
        }
    }

    render() {
        return (
            <canvas ref={this.canvas}/>
        )
    }
}

export default MapView;
