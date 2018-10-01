import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { Button, Table , Grid } from 'react-bootstrap';


class People extends Component {
  constructor() {
    super();
    this.state = {
      people: [],
    };
  }

  componentDidMount() {
    fetch('http://localhost:9000/people')
      .then(results => {
        return results.json();
      }).then(data => {
        console.log(data)
      })
  }

  render() {
    return (
      <Table striped bordered >
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Job Title</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Mark</td>
            <td>Otto</td>
            <td>@mdo</td>
          </tr>
          <tr>
            <td>Jacob</td>
            <td>Thornton</td>
            <td>@fat</td>
          </tr>
        </tbody>
      </Table>
    )
  }
}

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">People Application</h1>
        </header>
        <p/>
        <Grid>
          <p align="left" className="App-intro">
            Directory
          </p>
          <People />
          <Button>
            Character Lookup
          </Button>
        </Grid>
      </div>
    );
  }
}

export default App;
