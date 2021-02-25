import {Route} from 'dva/router';
import React from "react";
import {ACCESS_TOKEN, AUTH_URL} from "../../../config/config";
import {ACCESS_TOKEN_REG} from "../../../config/regExp";
import {getStorage, setStorage} from "../../utils/util";

export default class AuthenticatedRoute extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    const accessTokenGroup = window.location.href.match(ACCESS_TOKEN_REG);
    const accessToken = !accessTokenGroup || typeof accessTokenGroup === 'undefined' ? null : accessTokenGroup[0].split('=')[1];

    if (accessToken) {
      setStorage(ACCESS_TOKEN, accessToken);
    }

    return (
      <Route path={this.props.path} render={(props) => {
        if (getStorage(ACCESS_TOKEN)) {
          return <this.props.component {...props}/>
        }
        window.open(AUTH_URL, '_self')
      }}/>
    )
  }
}