import React, { Component } from 'react';
import logo from './people.svg';
import './App.css';
import { Grid } from 'react-bootstrap';
import { Table, Button } from 'antd';
// import 'antd/dist/antd.css';  // or 'antd/dist/antd.less'


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
        let people = data
        this.setState({people: people});
      })
  }

  pageChanged() {
    console.log("Page changed!")
  }

  render() {
    const columns = [{
      title: 'Name',
      dataIndex: 'fullName',
      key: 'fullName',
    }, {
      title: 'Email Address',
      dataIndex: 'emailAddress',
      key: 'emailAddress',
    }, {
      title: 'Job Title',
      dataIndex: 'jobTitle',
      key: 'jobTitle',
    }];

    return (
      <Table dataSource={this.state.people} columns={columns} onChange={this.pageChanged}/>
    )
  }
}

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">
            <img src={logo} className="App-logo" alt="logo" />
            <div>People Application</div>
          </h1>
        </header>
        <p/>
        <Grid>
          <p align="left" className="App-intro">
            Directory
          </p>
          <People />
          <Button
            loading='true'
          >
            Character Lookup
          </Button>
        </Grid>
      </div>
    );
  }
}

export default App;
