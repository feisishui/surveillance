package com.casic.patrol.indication.calculaters;

import com.casic.patrol.indication.listener.BaseIndicationCalculateStrategy;
import com.casic.patrol.indication.persistence.domain.IndicationInfo;
import com.casic.patrol.indication.persistence.manager.IndicationInfoManager;
import com.casic.patrol.indication.support.Indication;
import com.casic.patrol.overtime.persistence.domain.TaskOvertimeInfo;
import com.casic.patrol.overtime.persistence.manager.TaskOvertimeInfoManager;
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
public class DispatchCalculate extends BaseIndicationCalculateStrategy {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IndicationInfoManager indicationInfoManager;

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    private String shouldDispatchRelatedTaskCode = "taskuser-3";

    private String companyArgs = "company";

    public DispatchCalculate() {
        super("surveillance:1:4", Arrays.asList("taskuser-4"));
    }

    @Override
    public void calculate(DelegateTask delegateTask) {
        IndicationInfo indicationInfo = calculateIndication(
                delegateTask, Indication.DISPATCH);
        indicationInfoManager.save(indicationInfo);
        delShouldDispatchTask(delegateTask, indicationInfo);
        insertDispatchWithTime(delegateTask);
        insertShouldDispose(delegateTask);
    }

    /**
     * 删除多分发的“应派遣数”指标
     * @param delegateTask
     * @param indicationInfo
     */
    private void delShouldDispatchTask(DelegateTask delegateTask,
                                       IndicationInfo indicationInfo) {
        List<HistoricTaskInstance> instances = processEngine
                .getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
                .finished().orderByHistoricTaskInstanceEndTime().desc().list();
        for (HistoricTaskInstance instance : instances) {
            if (instance.getTaskDefinitionKey().equals(shouldDispatchRelatedTaskCode)) {
                String taskID = instance.getId();
                ExecInfo result = indicationInfoManager.delIndicationsBy(
                        delegateTask.getProcessInstanceId(),
                        taskID, Indication.DISPATCH_SHOULD.name(),
                        indicationInfo.getRef()
                );
                if (!result.isSucc()) {
                    logger.error("删除指标[应派遣数]失败，失败原因：{}.",
                            result.getMsg());
                }
                return;
            }
        }
    }

    /**
     * 插入“按期派遣数”与“超期派遣数”指标
     * @param delegateTask
     */
    private void insertDispatchWithTime(DelegateTask delegateTask) {
        IndicationInfo indicationInfo;
        TaskOvertimeInfo info = taskOvertimeInfoManager.getInfoBy(
                delegateTask.getProcessInstanceId(),
                delegateTask.getId(), delegateTask.getId()
        );
        if (info.getEndTime().getTime() > info.getOverTime().getTime()) {
            indicationInfo = calculateIndication(
                    delegateTask, Indication.DISPATCH_OVERDUE
            );
        } else {
            indicationInfo = calculateIndication(
                    delegateTask, Indication.DISPATCH_ONTIME
            );
        }
        indicationInfoManager.save(indicationInfo);
    }

    /**
     * 插入“应处置数”指标
     * @param delegateTask
     */
    private void insertShouldDispose(DelegateTask delegateTask) {
        List<String> companys = (List<String>)
                delegateTask.getExecution().getVariable(companyArgs);
        for (String company : companys) {
            IndicationInfo indicationInfo = calculateIndication(
                    delegateTask, Indication.DISPOSE_SHOULD
            );
            indicationInfo.setRef(company);
            indicationInfoManager.save(indicationInfo);
        }
    }
}
