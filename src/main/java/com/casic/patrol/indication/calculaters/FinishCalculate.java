package com.casic.patrol.indication.calculaters;

import com.casic.patrol.indication.listener.BaseIndicationCalculateStrategy;
import com.casic.patrol.indication.persistence.domain.IndicationInfo;
import com.casic.patrol.indication.persistence.manager.IndicationInfoManager;
import com.casic.patrol.indication.support.Indication;
import com.casic.patrol.overtime.persistence.domain.TaskOvertimeInfo;
import com.casic.patrol.overtime.persistence.manager.TaskOvertimeInfoManager;
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
public class FinishCalculate extends BaseIndicationCalculateStrategy {

    public static final String DISPOSE_PERSON = "THIS";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private IndicationInfoManager indicationInfoManager;

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    private String disposeRelatedTaskCode = "taskuser-5";

    private String args = "isClosed";

    private String companyArgs = "company";

    public FinishCalculate() {
        super("surveillance:1:4", Arrays.asList("taskuser-6"));
    }

    @Override
    public void calculate(DelegateTask delegateTask) {
        List<HistoricTaskInstance> instances = processEngine
                .getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
                .taskDefinitionKey(disposeRelatedTaskCode).finished()
                .orderByHistoricTaskInstanceEndTime().desc().list();
        if (instances == null || instances.size() < 1) {
            logger.error(
                    "插入指标[返工数]查找不到处置任务[流程ID为{}]instance.",
                    delegateTask.getProcessInstanceId()
            );
            return;
        }
        HistoricTaskInstance instance = instances.get(0);
        String ref = getAssigneeByTaskID(instance.getId());
        if ("不予结案".equals(delegateTask.getExecution().getVariable(args))) {
            IndicationInfo indicationInfo = calculateIndication(delegateTask, Indication.REWORK);
            Integer count = indicationInfoManager.getCountBy(
                    Indication.REWORK.name(), ref,
                    delegateTask.getProcessInstanceId()
            );
            indicationInfo.setRef(ref);
            indicationInfo.setAttr1(new Integer(count + 1).toString());
            indicationInfoManager.save(indicationInfo);
        } else {
            TaskOvertimeInfo info = taskOvertimeInfoManager.getInfoBy(
                    instance.getProcessInstanceId(),
                    instance.getId(), instance.getId()
            );
            boolean overtime = info.getEndTime().getTime() >
                    info.getOverTime().getTime();
            List<String> companys = (List<String>)
                    delegateTask.getExecution().getVariable(companyArgs);
            for (String company : companys) {
                IndicationInfo indicationInfo = calculateIndication(
                        delegateTask, Indication.DISPOSE
                );
                if (ref.equals(company)) {
                    indicationInfo.setAttr1(DISPOSE_PERSON);
                }
                saveUsefulIndicationInfo(indicationInfo, instance, company);
                if (overtime) {
                    indicationInfo = calculateIndication(
                            delegateTask, Indication.DISPOSE_OVERDUE
                    );
                } else {
                    indicationInfo = calculateIndication(
                            delegateTask, Indication.DISPOSE_ONTIME
                    );
                }
                if (ref.equals(company)) {
                    indicationInfo.setAttr1(DISPOSE_PERSON);
                }
                saveUsefulIndicationInfo(indicationInfo, instance, company);
            }
        }
    }

    private void saveUsefulIndicationInfo(
            IndicationInfo indicationInfo,
            HistoricTaskInstance instance,
            String ref
    ) {
        indicationInfo.setRef(ref);
        indicationInfo.setRelatedTaskId(instance.getId());
        indicationInfo.setRelatedTaskStartTime(instance.getStartTime());
        indicationInfo.setRelatedTaskEndTime(instance.getEndTime());
        indicationInfoManager.save(indicationInfo);
    }

}
