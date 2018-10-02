import React, { Component } from 'react';
import logo from './people.svg';
import './App.css';
import { Grid, Row, Col } from 'react-bootstrap';
import { Table, Button } from 'antd';


class PeopleTable extends Component {
  pageSize = 10

  constructor() {
    super();
    this.state = {
      people: [],
      pageMeta: {
        totalCount: 0,
        totalPages: 0,
        currentPage: 1
      }
    };
  }

  componentDidMount() {
    let page = this.state.pageMeta.currentPage
    this.refreshPage(page)
  }

  refreshPage(page) {
    fetch(`http://localhost:9000/people?pageSize=${this.pageSize}&page=${page}`)
    .then(results => {
      return results.json();
    }).then(data => {
      console.log(data)
      let people = data.people
      let pageMeta = data.pageMeta
      this.setState({people: people, pageMeta: pageMeta});
    })
  }

  onChange(current) {
    console.log('Page: ', current);
    console.log('State: ', this.state);
    this.refreshPage(current);
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

    const paginationOptions = {
      simple: true,
      onChange: this.onChange.bind(this),
      total: this.state.pageMeta.totalCount
    }

    return (
      <Table dataSource={this.state.people} columns={columns} pagination={paginationOptions} />
    )
  }
}

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <Grid>
            <Row>
              <Col xs={6} md={1}>
                <img src={logo} className="App-logo" alt="logo" />
              </Col>
              <Col xs={12} md={6}>
                <h1 className="App-title">People Application</h1>
              </Col>
              <Col xs={12} md={5} />
            </Row>
          </Grid>
        </header>
        <p/>
        <Grid>
          <p align="left" className="App-intro">
            Directory
          </p>
          <PeopleTable />
          <div className="text-left">
            <Button align="left">
              Character Lookup
            </Button>
          </div>
          
        </Grid>
      </div>
    );
  }
}

export default App;
