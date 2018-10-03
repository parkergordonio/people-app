import React, { Component } from 'react';
import './App.css';
import { Row, Col, Layout, Menu, Table, Divider } from 'antd';
import CharCountDisplay from './CharCountDisplay';
const { Header, Content, Footer } = Layout;

class PeopleTable extends Component {
  pageSize = 8

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
      //<div className="App">
        <Layout className="layout">
          <Header>
            <div>
              <Row>
                <Col span={4}>
                  <div className="App-header">Workforce Portal</div>
                </Col>
                <Col span={20}>
                  <Menu
                    theme="dark"
                    mode="horizontal"
                    defaultSelectedKeys={['1']}
                    style={{ lineHeight: '64px' }}
                  >
                    <Menu.Item key="1">People</Menu.Item>
                    <Menu.Item key="2">Contact</Menu.Item>
                  </Menu>
                </Col>
              </Row>
            </div>
          </Header>
          <Content style={{ padding: '0 50px' }}>
            <div>
              <Divider>Directory</Divider>
              <PeopleTable span={4}/>
              <CharCountDisplay />
            </div>
          </Content>
          <Footer style={{ textAlign: 'center' }}>
            Workforce Portal ©2018 Created by Parker Gordon. Data provided by SalesLoft.
          </Footer>
        </Layout>
      //</div>
    );
  }
}

export default App;
