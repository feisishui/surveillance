package com.casic.patrol.indication.listener;

import com.casic.patrol.bpm.support.DefaultTaskListener;
import org.activiti.engine.delegate.DelegateTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 */
public class IndicationStatisticsListener extends DefaultTaskListener {

    private List<IndicationCalculateStrategy> calculates = new ArrayList<IndicationCalculateStrategy>();

    @Override
    public void onComplete(DelegateTask delegateTask) throws Exception {
        for (IndicationCalculateStrategy calculate : calculates) {
            if (calculate.needCalculate(delegateTask)) {
                calculate.calculate(delegateTask);
            }
        }
    }

    public void setCalculates(List<IndicationCalculateStrategy> calculates) {
        this.calculates = calculates;
    }
}
