import React from "react";
import Modal from '../../../components/Modal';
import {Col, Form, Input, Row} from 'antd';
import {connect} from "dva";

const EDIT = 'EDIT';
const EXTEND = 'EXTEND';
const CREATE = 'CREATE';
const COPY = 'COPY';
@Form.create({name: 'roleForm'})
@connect()
export default class RoleModal extends React.Component {
  constructor(props) {
    super(props);
    this.props.onRoleModal(this);
    this.state = {
      title: null
    };
  }

  onRefModal = drawer => {
    this.onRefModal = drawer;
  }

  handleOpenModal = options => {
    this.onRefModal.handleOpenModal();
    this.setState({
      ...this.state,
      ...options
    });
  }

  handleCancelModal = () => {
    this.setState({});
  }

  handleConfirmModal = () => {
    const {
      form,
      dispatch
    } = this.props;
    const {
      id,
      parentId,
      extendId,
      parentLevelPath,
      extendLevelPath,
      action
    } = this.state;
    form.validateFieldsAndScroll(rst => {
      if (!rst) {
        const roleForm = form.getFieldsValue();
        // 创建
        if (action === CREATE) {
          roleForm.parentId = id;
          roleForm.parentLevelPath = `${parentLevelPath}/${id}`;
        } else {
          roleForm.parentId = parentId;
          roleForm.parentLevelPath = parentLevelPath;
        }
        // 继承
        if (action === EXTEND) {
          roleForm.extendId = id;
          roleForm.extendLevelPath = `${extendLevelPath}/${id}`;
        } else {
          roleForm.extendId = extendId;
          roleForm.extendLevelPath = extendLevelPath;
        }
        // 编辑/复制/继承 id设置
        if(action === EDIT || action === COPY || action === EXTEND){
          roleForm.id = id;
        }

        roleForm.action = action;
        if(CREATE === action || EDIT === action) {
          dispatch({
            type: 'role/roleCreateOrEdit',
            payload: roleForm
          });
        }
        if(COPY === action || EXTEND){
          dispatch({
            type: 'role/roleCopyOrExtend',
            payload: roleForm
          });
        }
      }
    })
  }

  render() {
    const {
      form: {getFieldDecorator}
    } = this.props;

    const {
      parentRoleName,
      extendRoleName,
      roleName,
      roleCode,
      action
    } = this.state;

    const formItemLayout = {
      labelCol: {span: 8},
      wrapperCol: {span: 16}
    }

    const content = (
      <Form className='ant-advanced-search-form'>
        <Row>
          <Col span={12}>
            <Form.Item label="父级角色" {...formItemLayout}>
              <Input defaultValue={CREATE === action ? roleName : parentRoleName} disabled/>
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item label="继承角色"  {...formItemLayout}>
              <Input defaultValue={EXTEND === action ? roleName : extendRoleName} disabled/>
            </Form.Item>
          </Col>
        </Row>
        <Row>
          <Col span={12}>
            <Form.Item label="角色编码"  {...formItemLayout}>
              {getFieldDecorator('roleCode', {
                rules: [
                  {
                    required: true
                  }
                ],
                initialValue: action === EDIT ? roleCode : null
              })(<Input disabled={action === EDIT}/>)}
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item label="角色名称"  {...formItemLayout}>
              {getFieldDecorator('roleName', {
                rules: [
                  {
                    required: true,
                  }
                ],
                initialValue: action === EDIT ? roleName : null
              })(<Input/>)}
            </Form.Item>
          </Col>
        </Row>
      </Form>
    );

    return (
      <Modal
        width={'600px'}
        title={this.state.title}
        onRefModal={this.onRefModal}
        onCancel={this.handleCancelModal}
        onOk={this.handleConfirmModal}
        modalContent={content}
      >

      </Modal>
    )
  }

}

