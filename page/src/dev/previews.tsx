import React from "react";
import {ComponentPreview, Previews} from "@react-buddy/ide-toolbox";
import {PaletteTree} from "./palette";
import DenseTable from "../component/DenseTable";

const ComponentPreviews = () => {
    return (
        <Previews palette={<PaletteTree/>}>
            <ComponentPreview path="/DenseTable">
                <DenseTable/>
            </ComponentPreview>
        </Previews>
    );
};

export default ComponentPreviews;