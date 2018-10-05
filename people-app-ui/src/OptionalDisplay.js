import React, { Component } from 'react';
import './App.css';
import { Row, Col, Table, Button, Divider } from 'antd';


class OptionalDisplay extends Component {
    constructor() {
      super();
      this.state = {
        chars: [],
        dupes: [],
        showFreqResults: false,
        showDupeResults: false
      };
    }
  
    componentDidMount() {
    }
  
    loadFreqTableData() {
      fetch(`http://localhost:9000/people/email/charFrequency`)
      .then(results => {
        return results.json();
      }).then(data => {
        console.log(data)
        this.setState({showFreqResults: "true", chars: data});
      })
    }
  
    loadDupeTableData() {
        fetch(`http://localhost:9000/people/email/duplicates`)
        .then(results => {
          return results.json();
        }).then(data => {
          console.log(data)
          this.setState({showDupeResults: "true", dupes: data.duplicates});
        })
    }

    render() {
      const freqTableColumns = [{
        title: 'Character',
        dataIndex: 'char',
        key: 'char',
      }, {
        title: 'Count',
        dataIndex: 'count',
        key: 'count',
      }];
  
      const dupeTableColumns = [{
        title: 'Duplicates',
        dataIndex: 'emails',
        key: 'emails',
      }];

      return (
            <div>
              <Row>
                <Col span={11} offset={0}>
                    <div align="center" text-align="center">
                        <Divider>Email Char Frequency Lookup</Divider>
                        {this.state.showFreqResults ? null : <Button onClick={this.loadFreqTableData.bind(this)} >Display</Button> }
                        {this.state.showFreqResults ? <Table dataSource={this.state.chars} columns={freqTableColumns} pagination="false" /> : null }
                    </div>
                </Col>
                <Col span={11}offset={2}>
                    <div align="center" text-align="center">
                        <Divider>Possible Email Duplicates</Divider>
                        {this.state.showDupeResults ? null : <Button onClick={this.loadDupeTableData.bind(this)} >Display</Button> }
                        {this.state.showDupeResults ? <Table dataSource={this.state.dupes} columns={dupeTableColumns} pagination="false" /> : null }
                    </div>
                </Col>
              </Row>
            </div>
      )
    }
  }

  export default OptionalDisplay;