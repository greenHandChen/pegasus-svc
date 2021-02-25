import React, {Component} from 'react';
import QRCode from 'qrcode.react';

// 二维码
export default class QrCode extends Component {
  constructor(props) {
    super(props);
    this.state = {}
  };


  render() {
    const {value} = this.props;
    return (
      <QRCode value={value}/>);
  }
}