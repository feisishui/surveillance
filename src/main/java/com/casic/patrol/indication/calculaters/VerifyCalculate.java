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
public class VerifyCalculate extends BaseIndicationCalculateStrategy {

    public static final String UNKNOWN_LOCATION = "UNKNOWN";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private IndicationInfoManager indicationInfoManager;

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    private String missingReportRelatedTaskCode = "taskuser-1";

    private String shouldVerifyRelatedTaskCode = "taskuser-2";

    private String confirmCommentArgs = "confirmComment";

    private String locationArgs = "position";

    private String shouldDispatchArgs = "commandCenter";

    public VerifyCalculate() {
        super("surveillance:1:4", Arrays.asList("taskuser-3"));
    }

    @Override
    public void calculate(DelegateTask delegateTask) {
        IndicationInfo indicationInfo = calculateIndication(
                delegateTask, Indication.VERIFY
        );
        indicationInfoManager.save(indicationInfo);
        delShouldVerifyTask(delegateTask, indicationInfo);
        insertVerifyWithTime(delegateTask);
        insertMissReport(delegateTask);
        insertShouldDispatch(delegateTask);
    }

    /**
     * 删除多分发的“应核实数”指标
     * @param delegateTask
     * @param indicationInfo
     */
    private void delShouldVerifyTask(DelegateTask delegateTask, IndicationInfo indicationInfo) {
        List<HistoricTaskInstance> instances = processEngine
                .getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(delegateTask.getProcessInstanceId())
                .finished().orderByHistoricTaskInstanceEndTime().desc().list();
        for (HistoricTaskInstance instance : instances) {
            if (instance.getTaskDefinitionKey().equals(shouldVerifyRelatedTaskCode)) {
                String taskID = instance.getId();
                ExecInfo result = indicationInfoManager.delIndicationsBy(
                        delegateTask.getProcessInstanceId(),
                        taskID, Indication.VERIFY_SHOULD.name(),
                        indicationInfo.getRef()
                );
                if (!result.isSucc()) {
                    logger.error("删除指标[应核实数]失败，失败原因：{}.",
                            result.getMsg());
                }
                return;
            }
        }
    }

    /**
     * 插入“按期核实数”与“超期核实数”指标
     * @param delegateTask
     */
    private void insertVerifyWithTime(DelegateTask delegateTask) {
        IndicationInfo indicationInfo;
        TaskOvertimeInfo info = taskOvertimeInfoManager.getInfoBy(
                delegateTask.getProcessInstanceId(),
                delegateTask.getId(), delegateTask.getId()
        );
        if (info.getEndTime().getTime() > info.getOverTime().getTime()) {
            indicationInfo = calculateIndication(
                    delegateTask, Indication.VERIFY_OVERDUE
            );
        } else {
            indicationInfo = calculateIndication(
                    delegateTask, Indication.VERIFY_ONTIME
            );
        }
        indicationInfoManager.save(indicationInfo);
    }

    /**
     * 插入“漏报数”指标
     * @param delegateTask
     */
    private void insertMissReport(DelegateTask delegateTask) {
        if ("属实".equals(delegateTask.getExecution().getVariable(confirmCommentArgs))) {
            IndicationInfo indicationInfo = calculateIndication(
                    delegateTask, Indication.MISSING_REPORT
            );
            List<HistoricTaskInstance> instances = processEngine
                    .getHistoryService().createHistoricTaskInstanceQuery()
                    .processInstanceId(delegateTask.getProcessInstanceId())
                    .taskDefinitionKey(missingReportRelatedTaskCode).finished()
                    .orderByHistoricTaskInstanceEndTime().asc().list();
            if (instances == null || instances.size() < 1) {
                logger.error("插入指标[漏报数]查找不到初始上报任务[流程ID为{}]instance.",
                        delegateTask.getProcessInstanceId());
                indicationInfo.setRelatedTaskId(null);
                indicationInfo.setRelatedTaskStartTime(null);
                indicationInfo.setRelatedTaskEndTime(null);
            } else {
                HistoricTaskInstance instance = instances.get(0);
                String location = (String)delegateTask.getExecution()
                        .getVariable(locationArgs);
                indicationInfo.setAttr1(findLocation(location));
                indicationInfo.setRelatedTaskId(instance.getId());
                indicationInfo.setRelatedTaskStartTime(instance.getStartTime());
                indicationInfo.setRelatedTaskEndTime(instance.getEndTime());
            }
            indicationInfo.setRef(null);
            indicationInfoManager.save(indicationInfo);
        }
    }

    private String findLocation(String location) {
        String startMarker = ",", endMarker = "市", tempMarker = "自治州";
        int start = location.lastIndexOf(startMarker),
                end = location.indexOf(endMarker);
        if (end < 0 || (location.indexOf(tempMarker) > 0
                && location.indexOf(tempMarker) < end)) {
            endMarker = "自治州";
            end = location.indexOf(endMarker);
        }
        if (start > 0 && end > 0 && start < end) {
            return location.substring(
                    start + startMarker.length(),
                    end + endMarker.length()
            );
        } else {
            return UNKNOWN_LOCATION;
        }
    }

    /**
     * 插入“应派遣数”指标
     * @param delegateTask
     */
    private void insertShouldDispatch(DelegateTask delegateTask) {
        if ("属实".equals(delegateTask.getExecution().getVariable(confirmCommentArgs))) {
            List<String> users = (List<String>)
                    delegateTask.getExecution().getVariable(shouldDispatchArgs);
            for (String user : users) {
                IndicationInfo indicationInfo = calculateIndication(
                        delegateTask, Indication.DISPATCH_SHOULD
                );
                indicationInfo.setRef(user);
                indicationInfoManager.save(indicationInfo);
            }
        }
    }
}
