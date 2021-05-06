import React from "react";
import {getStorage} from "../../../utils/util";

export default class OnlyOffice extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    const url = `http://192.168.91.1:8080/all/v1/test/preview-office?access_token=${getStorage("access_token")}`;
    console.log(url)
    // eslint-disable-next-line
    const docEditor = new DocsAPI.DocEditor("onlyOffice", {
      documentType: "spreadsheet",
      height: "800px",
      width: "100%",
      type: "desktop",
      document: {url:url,title:"测试"}

    });
  }

  render() {
    return (
      <React.Fragment>
        <div id={`onlyOffice-root`}>
          <div id={`onlyOffice`}/>
        </div>
      </React.Fragment>
    )
  }
}