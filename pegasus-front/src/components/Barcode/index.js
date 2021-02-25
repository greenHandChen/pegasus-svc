import React, {Component} from 'react';
import JsBarcode from 'jsbarcode';

// 条形码
export default class Barcode extends Component {

  static defaultProps = {
    format: 'CODE128', //选择要使用的条形码类型
    renderer: 'svg',
    width: 1.6,//设置宽度
    height: 25,//高度
    displayValue: false,//是否在条形码下方显示文字
    textAlign: 'center',//设置文本的水平对齐方式
    textPosition: 'bottom',//设置文本的垂直位置
    textMargin: 6,//设置条形码和文本之间的间距
    fontSize: 14,//设置文本的大小
    background: '#ffffff',//设置条形码的背景
    lineColor: '#000000',//设置条和文本的颜色
    margin: 0,//设置条形码周围的空白边距
    marginBottom: 0,//设置条形码下边的空白边距
  };

  constructor(props) {
    super(props);
    this.update = this.update.bind(this);
  };

  componentDidMount() {
    this.update();
  };

  componentDidUpdate() {
    this.update();
  };

  handleBarcode = (r) => {
    this.barcode = r;
  }

  update() {
    const {
      value,
      format,
      width,
      height,
      displayValue,
      textAlign,
      textPosition,
      textMargin,
      fontSize,
      background,
      margin,
      lineColor,
      marginBottom,
    } = this.props;
    JsBarcode(this.barcode, value, {
      format,
      width,
      height,
      displayValue,
      textAlign,
      textPosition,
      textMargin,
      fontSize,
      background,
      margin,
      lineColor,
      marginBottom,
    })
  };

  render() {
    const {renderer} = this.props;
    if (renderer === 'svg') {
      return (
        <svg ref={this.handleBarcode}/>
      );
    } else if (renderer === 'canvas') {
      return (
        <canvas ref={this.handleBarcode}/>
      );
    } else if (renderer === 'img') {
      return (
        <img ref={this.handleBarcode} alt=""/>
      );
    }
  };
}