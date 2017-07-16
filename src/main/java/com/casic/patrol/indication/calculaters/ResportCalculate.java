package com.casic.patrol.indication.calculaters;

import com.casic.patrol.indication.listener.BaseIndicationCalculateStrategy;
import com.casic.patrol.indication.persistence.domain.IndicationInfo;
import com.casic.patrol.indication.persistence.manager.IndicationInfoManager;
import com.casic.patrol.indication.support.Indication;
import org.activiti.engine.delegate.DelegateTask;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 */
public class ResportCalculate extends BaseIndicationCalculateStrategy {

    @Resource
    private IndicationInfoManager indicationInfoManager;

    private String args = "commandCenter";

    private String reportTypeArgs = "reportType";

    public ResportCalculate() {
        super("surveillance:1:4", Arrays.asList("taskuser-1"));
    }

    @Override
    public void calculate(DelegateTask delegateTask) {
        List<String> centers = (List<String>)delegateTask.getExecution().getVariable(args);
        if ("公众上报".equals(delegateTask.getExecution().getVariable(reportTypeArgs))) {
            for (String center : centers) {
                IndicationInfo indicationInfo = calculateIndication(
                        delegateTask, Indication.REPORT
                );
                indicationInfo.setRef(center);
                indicationInfoManager.save(indicationInfo);
            }
        } else {
            for (String center : centers) {
                IndicationInfo indicationInfo = calculateIndication(
                        delegateTask, Indication.DISPATCH_SHOULD
                );
                indicationInfo.setRef(center);
                indicationInfoManager.save(indicationInfo);
            }
        }
    }

}
