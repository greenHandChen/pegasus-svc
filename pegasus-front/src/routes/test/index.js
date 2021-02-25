import React from "react";
import Barcode from '../../components/Barcode';
import QrCode from '../../components/QrCode';

export default class Test extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
  }

  render() {
    return (
      <React.Fragment>
        <Barcode value={"123456"} height={50} width={2}/>
        <br/>
        <Barcode value={"1234"} height={49} width={3} displayValue={true}/>
        <br/>
        <QrCode value={'齐婉辰大傻帽'}/>
      </React.Fragment>
    )
  }
}