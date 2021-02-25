package com.pegasus.activiti.utils;

import org.activiti.bpmn.model.FormProperty;
import org.activiti.engine.delegate.DelegateExecution;

import java.util.List;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
public class ActivitiUtil {

    public static FormProperty getFormPropertyById(List<FormProperty> formProperties, String id) {
        for (FormProperty formProperty : formProperties) {
            if (id.equals(formProperty.getId())) {
                return formProperty;
            }
        }
        return null;
    }


    public static DelegateExecution getMultiInstanceRootExecution(DelegateExecution executionEntity) {
        DelegateExecution multiInstanceRootExecution = null;
        DelegateExecution currentExecution = executionEntity;
        while (currentExecution != null && multiInstanceRootExecution == null && currentExecution.getParent() != null) {
            if (currentExecution.isMultiInstanceRoot()) {
                multiInstanceRootExecution = currentExecution;
            } else {
                currentExecution = currentExecution.getParent();
            }
        }
        return multiInstanceRootExecution;
    }


    public static void setLoopVariable(DelegateExecution execution, String variableName, Object value) {
        execution.setVariableLocal(variableName, value);
    }

    public static Integer getLoopVariable(DelegateExecution execution, String variableName) {
        Object value = execution.getVariableLocal(variableName);
        DelegateExecution parent = execution.getParent();
        while (value == null && parent != null) {
            value = parent.getVariableLocal(variableName);
            parent = parent.getParent();
        }
        return (Integer) (value != null ? value : 0);
    }
}
