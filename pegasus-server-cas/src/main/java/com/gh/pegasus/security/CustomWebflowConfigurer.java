package com.gh.pegasus.security;

import com.gh.pegasus.security.UsernamePasswordSubSystemCredential;
import org.apereo.cas.web.flow.AbstractCasWebflowConfigurer;
import org.apereo.cas.web.flow.CasWebflowConstants;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.engine.builder.BinderConfiguration;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;

/**
 * @author enhui.chen
 * @desc 拓展信息绑定登录FLOW
 * @date 2021-02-26 23:17:05
 */
public class CustomWebflowConfigurer extends AbstractCasWebflowConfigurer {

    /**
     * 构造函数
     *
     * @param flowBuilderServices
     * @param loginFlowDefinitionRegistry
     */
    public CustomWebflowConfigurer(FlowBuilderServices flowBuilderServices, FlowDefinitionRegistry loginFlowDefinitionRegistry) {
        super(flowBuilderServices, loginFlowDefinitionRegistry);
    }

    /**
     * @desc 登录流绑定输入信息
     *
     * @param
     * @date 2021-02-26 23:25:25
     * @return	void
     * @exception
     */
    @Override
    protected void doInitialize() throws Exception {
        Flow flow = getLoginFlow();
        bindCredential(flow);
    }

    /**
     * 绑定输入信息
     *
     * @param flow 流程
     */
    protected void bindCredential(Flow flow) {
        //重写绑定自定义credential
        createFlowVariable(flow, CasWebflowConstants.VAR_ID_CREDENTIAL, UsernamePasswordSubSystemCredential.class);
        //登录页绑定新参数
        final ViewState state = (ViewState) flow.getState(CasWebflowConstants.STATE_ID_VIEW_LOGIN_FORM);

        final BinderConfiguration cfg = getViewStateBinderConfiguration(state);
        //由于用户名以及密码已经绑定，所以只需对新加系统参数绑定即可
        //参数1 ：字段名
        //参数2 ：转换器
        //参数3 ：是否必须的字段
        cfg.addBinding(new BinderConfiguration.Binding("captcha", null, false));

        //进入viewstate前传入参数(在系统参数绑定之后设置)
/*        ActionList entryActionList = state.getEntryActionList();
        entryActionList.add(createEvaluateAction("viewScope.newReg=" + sysConfigQueryService.getNewRegister()));
        entryActionList.add(createEvaluateAction("viewScope.pwdReset=" + sysConfigQueryService.getResetPassword()));
        entryActionList.add(createEvaluateAction("viewScope.codeValidate=" + sysConfigQueryService.getImageValidate()));
        //系统名称
        entryActionList.add(createEvaluateAction("viewScope.sysTitle=" + sysConfigQueryService.getSysTitle()));*/
    }

}
