package com.casic.patrol.indication.listener;

import org.activiti.engine.delegate.DelegateTask;

/**
 * Created by lenovo on 2016/9/27.
 */
public interface IndicationCalculateStrategy {

    boolean needCalculate(DelegateTask delegateTask);

    void calculate(DelegateTask delegateTask);
}
