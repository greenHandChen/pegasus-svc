import React from "react";
import {Button, Input} from "antd";
import {_CREATE, _DELETE} from '../../../../config/config'
import Drawer from '../../../components/Drawer'
import Table from '../../../components/Table'
import {connect} from 'dva';

@connect(({loading, role}) => ({
  menuTree: role.menuTree,
  tiledMenuListLoading: loading.effects['role/findTiledMenuByRoleId']
}))
export default class AuthorizationDispatchDrawer extends React.Component {
  constructor(props) {
    super(props);
    this.props.onAuthorizationDispatchDrawer(this);
    this.state = {
      id: null,// roleId当前角色主键ID
      title: null,
      selectedRowKeys: [],// 被选中行的主键
      selectedRows: []// 用户操作的删除/新增数据
    };
  }

  componentDidMount() {
    this.init();
  }

  onRefDrawer = drawer => {
    this.onRefDrawer = drawer;
  }

  init = () => {
    this.props.dispatch({
      type: 'role/findMenuAllForRole'
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开Drawer
   * @Data 2019/9/28
   */
  handleOpenDrawer = options => {
    this.onRefDrawer.handleOpenDrawer();
    this.openInit(options);
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开初始化
   * @Data 2019/9/28
   */
  openInit = (options) => {
    this.props.dispatch({
      type: 'role/findTiledMenuByRoleId',
      payload: {
        roleId: options.id
      }
    }).then(res => {
      this.setState({
        ...this.state,
        ...options,
        selectedRows: [],
        selectedRowKeys: res && typeof res !== 'undefined' ? res.map(menu => menu.id) : []
      });
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 确定按钮
   * @Data 2019/9/28
   */
  handleConfirmBtn = () => {
    const {id, selectedRows} = this.state;
    this.props.dispatch({
      type: 'role/roleMenuBatchDispatch',
      payload: {
        roleId: id,
        menus: selectedRows
      }
    })
  }

  handleSearch = (val) => {
    this.props.dispatch({
      type: 'role/findMenuByMenuName',
      payload: {
        name: val
      }
    })
  }


  /**
   * @Author: enHui.Chen
   * @Description: 递归动态勾选
   * @Data 2019/9/27
   */
  handleToggleSelect = (row, selectedRowKeys, selectedRows, selected) => {
    if (!row || typeof row === 'undefined') return;
    const result = selectedRowKeys.findIndex(key => key === row.id);
    // 选中
    if (selected && result === -1) {
      selectedRowKeys.push(row.id);
      this.handleSelectedRows(selectedRows, row, _CREATE);
    }
    // 取消
    if (!selected) {
      selectedRowKeys.splice(result, 1);
      this.handleSelectedRows(selectedRows, row, _DELETE);
    }
    if (!row.routerData || typeof row.routerData === 'undefined') return;
    // 分治(往上找,往下找)
    row.routerData.forEach(data => this.handleToggleSelect(data, selectedRowKeys, selectedRows, selected));
  }

  handleSelectedRows = (selectedRows, row, _status) => {
    const resultRow = selectedRows.findIndex(selectedRow => selectedRow.id === row.id);
    if (resultRow === -1) {
      selectedRows.push({
        ...row,
        _status
      });
    } else {
      selectedRows.splice(resultRow, 1);
    }
  }


  /**
   * @Author: enHui.Chen
   * @Description: 处理复选框
   * @Data 2019/9/27
   */
  handleMenuSelect = (row, selected) => {
    const {
      selectedRowKeys,
      selectedRows
    } = this.state;
    this.handleToggleSelect(row, selectedRowKeys, selectedRows, selected);
    this.setState({
      ...this.state,
      selectedRowKeys,
      selectedRows
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 处理全选框
   * @Data 2019/9/28
   */
  handleMenuSelectAll = (selected, rows, changeRows) => {
    let {
      selectedRowKeys,
      selectedRows
    } = this.state;
    // 全选
    if (selected) {
      // 过滤出未勾选,或已勾选
      const filterSelectedRows = rows.filter(menu => selectedRowKeys.findIndex(selectedRowKey => selectedRowKey === menu.id) === -1);
      filterSelectedRows.forEach(filterSelectedRow => {
        this.handleSelectedRows(selectedRows, filterSelectedRow, _CREATE);
      })
    }
    // 全不选
    if (!selected) {
      selectedRows = changeRows;
      selectedRows.forEach(selectedRow => selectedRow._status = _DELETE);
    }
    this.setState({
      ...this.state,
      selectedRowKeys: selected ? rows.map(row => row.id) : [],
      selectedRows: selectedRows
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 展开行
   * @Data 2019/9/29
   */
  handleExpanded = () => {
    const collapsedDivs = document.getElementsByClassName('ant-table-row-collapsed');
    if (!collapsedDivs || typeof collapsedDivs === 'undefined') {
      return;
    }
    for (let i = 0; i < collapsedDivs.length; i++) {
      collapsedDivs[i].click();
    }
  }

  /**
   * @Author: enHui.Chen
   * @Description: 合并行
   * @Data 2019/9/29
   */
  handleCollapsed = () => {
    const expandedDivs = document.getElementsByClassName('ant-table-row-expanded');
    for (let i = 0; i < expandedDivs.length; i++) {
      expandedDivs[i].click();
    }
  }

  render() {
    const {
      menuTree,
      tiledMenuListLoading
    } = this.props;

    const {
      title,
      selectedRowKeys
    } = this.state;

    const rowSelection = {
      selectedRowKeys,
      onSelect: (row, selected) => this.handleMenuSelect(row, selected),
      onSelectAll: (selected, rows, changeRows) => this.handleMenuSelectAll(selected, rows, changeRows)
    }

    const content = (
      <div>
        <Input.Search
          placeholder="权限名称"
          onSearch={val => this.handleSearch(val)}
          style={{width: 180, marginBottom: 30}}
        />
        <Button style={{marginLeft: '10px'}} onClick={this.handleExpanded}>展开</Button>
        <Button style={{marginLeft: '10px'}} onClick={this.handleCollapsed}>合并</Button>
        <Table
          rowSelection={rowSelection}
          loading={tiledMenuListLoading}
          dataSource={menuTree}
          childrenColumnName={'routerData'}
          scroll={{x: '100%', y: 500}}
          pagination={false}
          columns={[
            {
              title: '权限名称',
              dataIndex: 'name',
              key: 'name',
              width: 200
            }
          ]}
        />
      </div>
    );

    return (
      <Drawer
        width={'400px'}
        title={title}
        onRefDrawer={this.onRefDrawer}
        onOk={this.handleConfirmBtn}
        content={content}
      />
    )
  }

}