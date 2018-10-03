import React, { Component } from 'react';
import './App.css';
import { Row, Col, Table, Button, Divider } from 'antd';


class CharCountDisplay extends Component {
    constructor() {
      super();
      this.state = {
        chars: [],
        showResults: false
      };
    }
  
    componentDidMount() {
    }
  
    loadTableData() {
      fetch(`http://localhost:9000/people/char/frequency`)
      .then(results => {
        return results.json();
      }).then(data => {
        console.log(data)
        this.setState({showResults: "true", chars: data});
      })
    }
  
    render() {
      const columns = [{
        title: 'Character',
        dataIndex: 'char',
        key: 'char',
      }, {
        title: 'Count',
        dataIndex: 'count',
        key: 'count',
      }];
  
      return (
            <div>
              <Row>
                <Col span={4} offset={10}>
                    <div align="center">
                        <Divider>Character Frequency Lookup</Divider>
                        {this.state.showResults ? null : <Button onClick={this.loadTableData.bind(this)} >Display</Button> }
                        {this.state.showResults ? <Table dataSource={this.state.chars} columns={columns} pagination="false" /> : null }
                    </div>
                </Col>
              </Row>
            </div>
      )
    }
  }

  export default CharCountDisplay;