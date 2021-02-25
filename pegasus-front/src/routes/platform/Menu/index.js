import React from "react";
import Table from '../../../components/Table';
import {_CREATE, _UPDATE, APP_NAME} from '../../../../config/config';
import Drawer from '../../../components/Drawer';
import {connect} from "dva";
import {Form, Input, Select} from 'antd';

const ROOT_DIRECTORY = 'rootDirectory';
const DIRECTORY = 'directory';
const FUNC_MENU = 'funcMenu';
const MENU_SPLIT = '/';

@Form.create({name: 'menuForm'})
@connect(({loading, menu}) => ({
  menuTree: menu.menuTree,
  findMenuAllLoading: loading.effects['menu/findMenuAll']
}))
export default class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentRow: {},// 当前数据
      isDirectory: true
    }
  }

  componentDidMount() {
    this.init();
  }

  init = () => {
    this.props.dispatch({
      type: 'menu/findMenuAll'
    });
  }

  onRefDrawer = drawer => {
    this.onRefDrawer = drawer;
  }

  handleCreateMenu = row => {
    this.onRefDrawer.handleOpenDrawer();
    row._status = _CREATE;
    this.setState({
      currentRow: row,
      isDirectory: true
    })
  }

  handleUpdateMenu = row => {
    this.onRefDrawer.handleOpenDrawer();
    row._status = _UPDATE;
    this.setState({
      currentRow: row,
      isDirectory: row.type === DIRECTORY || row.type === ROOT_DIRECTORY
    })
  }

  handleActiveMenu = row => {
    this.props.dispatch({
      type: 'menu/createOrUpdateMenu',
      payload: {
        ...row,
        isActive: !row.isActive
      }
    });
  }

  handleConfirmBtn = () => {
    const {
      form,
      dispatch
    } = this.props;

    form.validateFieldsAndScroll(err => {
      if (!err) {
        const menuForm = form.getFieldsValue();
        const currentRow = this.state.currentRow;
        if (_CREATE === currentRow._status) {
          menuForm.id = null;
          menuForm.parentId = menuForm.type === ROOT_DIRECTORY ? 0 : currentRow.id;
          menuForm.levelPath = menuForm.type === ROOT_DIRECTORY ? 0 : `${currentRow.levelPath}${MENU_SPLIT}${currentRow.id}`
        } else {
          menuForm.id = currentRow.id;
          menuForm.parentId = currentRow.parentId;
          menuForm.levelPath = currentRow.levelPath;
        }
        menuForm.code = menuForm.parentCode + menuForm.code;
        dispatch({
          type: 'menu/createOrUpdateMenu',
          payload: menuForm
        });
      }
    });
  }

  handleSwitchMenuType = val => {
    this.setState({
      ...this.state,
      isDirectory: val === DIRECTORY || val === ROOT_DIRECTORY
    });
  }

  render() {
    const {
      menuTree,
      findMenuAllLoading,
      form: {getFieldDecorator, getFieldValue}
    } = this.props;

    const {
      currentRow
    } = this.state;

    const tableProps = {
      loading: findMenuAllLoading,
      scroll: {x: '105%', y: 1500},
      dataSource: menuTree,
      childrenColumnName:
        'routerData',
      columns:
        [
          {
            title: '菜单名称',
            key: 'name',
            width: 450,
            dataIndex: 'name'
          },
          {
            title: '菜单编码',
            key: 'code',
            width: 350,
            dataIndex: 'code'
          },
          {
            title: '菜单路由',
            key: 'path',
            width: 300,
            dataIndex: 'path'
          },
          {
            title: '菜单类型',
            key: 'type',
            width: 100,
            dataIndex: 'type',
            render: text => text === FUNC_MENU ? '功能菜单' : text === DIRECTORY ? '目录' : '根目录'
          },
          {
            title: '菜单排序',
            key: 'sort',
            width: 100,
            dataIndex: 'sort'
          },
          {
            title: '菜单icon图标',
            key: 'leftClass',
            dataIndex: 'leftClass',
            render: text => (<i className={text}></i>)
          },
          {
            title: '状态',
            key: 'isActive',
            dataIndex: 'isActive',
            fixed: 'right',
            width: 100,
            render: text => text ? '启用中' : '禁用中'
          },
          {
            title: '操作',
            key: 'action',
            width: 150,
            dataIndex: 'action',
            fixed: 'right',
            render: (text, row) => {
              return (<div>
                <a style={{marginLeft: '10px'}} onClick={() => this.handleCreateMenu(row)} target={'_blank'}>
                  新建
                </a>
                <a style={{marginLeft: '10px'}} onClick={() => this.handleUpdateMenu(row)} target={'_blank'}>
                  编辑
                </a>
                <a style={{marginLeft: '10px'}} onClick={() => this.handleActiveMenu(row)} target={'_blank'}>
                  {!row.isActive ? '启用' : '禁用'}
                </a>
              </div>);
            }
          }
        ]
    }

    let prefixCode = null;
    const formMenuType = getFieldValue('type');
    if (_CREATE === currentRow._status) {
      if (formMenuType) {
        prefixCode = formMenuType === ROOT_DIRECTORY ? `${APP_NAME}.` : `${currentRow.code}.`;
      }
      if (!prefixCode) {
        prefixCode = ROOT_DIRECTORY === currentRow.type ? `${APP_NAME}.` : `${currentRow.code}.`;
      }
    } else if (currentRow.code && currentRow.code.lastIndexOf('.') !== -1) {
      prefixCode = `${currentRow.code.substring(0, currentRow.code.lastIndexOf('.'))}.`;
    }
    const parentCode = getFieldDecorator('parentCode', {
      initialValue: prefixCode
    })(
      <Select style={{width: '100px'}}
              showArrow={false}
              disabled
      />);

    const formItemLayout = {
      labelCol: {
        xs: {span: 24},
        sm: {span: 6},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 14},
      },
    };

    const drawerProps = {
      title: '新建菜单',
      onRefDrawer: this.onRefDrawer,
      onOk: this.handleConfirmBtn,
      content:
        <Form {...formItemLayout}>
          <Form.Item label="菜单类型">
            {getFieldDecorator('type', {
              rules: [
                {
                  required: true,
                  message: '请选择菜单类型!',
                },
              ],
              initialValue: _UPDATE === currentRow._status ? currentRow.type : currentRow.type === DIRECTORY || currentRow.type === FUNC_MENU ? DIRECTORY : ROOT_DIRECTORY
            })(
              <Select onSelect={(val) => this.handleSwitchMenuType(val)}
                      disabled={_UPDATE === currentRow._status}
              >
                {currentRow.type === ROOT_DIRECTORY ? <Select.Option value={ROOT_DIRECTORY}>根目录</Select.Option> : null}
                <Select.Option value={DIRECTORY}>目录</Select.Option>
                <Select.Option value={FUNC_MENU}>功能菜单</Select.Option>
              </Select>
            )}
          </Form.Item>
          <Form.Item label="菜单编码">
            {getFieldDecorator('code', {
              rules: [
                {
                  required: true,
                  message: '请填写菜单编码!',
                }
              ],
              initialValue: _UPDATE === currentRow._status && currentRow.code ? currentRow.code.substring(currentRow.code.lastIndexOf('.') + 1, currentRow.code.length) : null
            })(
              <Input addonBefore={parentCode} style={{width: '100%'}} disabled={_UPDATE === currentRow._status}/>
            )}
          </Form.Item>
          <Form.Item label="菜单名称">
            {getFieldDecorator('name', {
              rules: [
                {
                  required: true,
                  message: '请填写菜单名称!',
                }
              ],
              initialValue: _UPDATE === currentRow._status ? currentRow.name : null
            })(<Input/>)}
          </Form.Item>
          {
            !this.state.isDirectory ? (<Form.Item label="菜单路由">
              {getFieldDecorator('path', {
                rules: [
                  {
                    required: true,
                    message: '请填写菜单路由!',
                  }
                ],
                initialValue: _UPDATE === currentRow._status ? currentRow.path : null
              })(<Input/>)}
            </Form.Item>) : null
          }
          <Form.Item label="菜单排序">
            {getFieldDecorator('sort', {
              initialValue: _UPDATE === currentRow._status ? currentRow.sort : 0
            })(<Input/>)}
          </Form.Item>
        </Form>
    }

    return (
      <React.Fragment>
        <Table
          {...tableProps}
        />
        <Drawer
          {...drawerProps}
        >
        </Drawer>
      </React.Fragment>
    )
  }
}