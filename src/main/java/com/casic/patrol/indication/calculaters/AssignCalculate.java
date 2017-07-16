package com.casic.patrol.indication.calculaters;

import com.casic.patrol.indication.listener.BaseIndicationCalculateStrategy;
import com.casic.patrol.indication.persistence.domain.IndicationInfo;
import com.casic.patrol.indication.persistence.manager.IndicationInfoManager;
import com.casic.patrol.indication.support.Indication;
import com.casic.patrol.util.ExecInfo;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2016/9/27.
 */
public class AssignCalculate extends BaseIndicationCalculateStrategy {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private IndicationInfoManager indicationInfoManager;

    private String relatedTaskCode = "taskuser-1";
    private String args = "verifyUser";

    public AssignCalculate() {
        super("surveillance:1:4", Arrays.asList("taskuser-2"));
    }

    @Override
    public void calculate(DelegateTask delegateTask) {
        IndicationInfo indicationInfo = calculateIndication(delegateTask, Indication.ASSIGN);
        indicationInfoManager.save(indicationInfo);
        delReportTask(delegateTask, indicationInfo);
        insertShouldVerify(delegateTask);
    }

    private void delReportTask(DelegateTask delegateTask, IndicationInfo indicationInfo) {
        List<HistoricTaskInstance> instances = processEngine
                .getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
                .finished().orderByHistoricTaskInstanceEndTime().desc().list();
        for (HistoricTaskInstance instance : instances) {
            if (instance.getTaskDefinitionKey().equals(relatedTaskCode)) {
                String taskID = instance.getId();
                ExecInfo result = indicationInfoManager.delIndicationsBy(
                        delegateTask.getProcessInstanceId(),
                        taskID, Indication.REPORT.name(),
                        indicationInfo.getRef()
                );
                if (!result.isSucc()) {
                    logger.error("删除指标[公众上报]失败，失败原因：{}.",
                            result.getMsg());
                }
                return;
            }
        }
    }

    private void insertShouldVerify(DelegateTask delegateTask) {
        List<String> users = (List<String>)
                delegateTask.getExecution().getVariable(args);
        for (String user : users) {
            IndicationInfo indicationInfo = calculateIndication(
                    delegateTask, Indication.VERIFY_SHOULD
            );
            indicationInfo.setRef(user);
            indicationInfoManager.save(indicationInfo);
        }
    }

}
