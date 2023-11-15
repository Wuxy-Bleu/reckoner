import React from 'react';
import logo from './logo.svg';
import './App.css';
import DenseTable from './component/DenseTable';

function App() {
  return (
    <div className="App">
      {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
      </header> */}
      <DenseTable/>
    </div>
  );
}

export default App;
