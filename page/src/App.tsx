import React from 'react';
import logo from './logo.svg';
import './App.css';
import DenseTable from './component/DenseTable';
import ColorBadge from './component/badge'
import EnhancedTable from "./component/accTable";

function App() {
    return (
        <div className="App">
            {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
      </header> */}
            <DenseTable/>
            <ColorBadge/>
            <EnhancedTable/>
        </div>
    );
}

export default App;
