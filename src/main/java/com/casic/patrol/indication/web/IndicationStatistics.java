package com.casic.patrol.indication.web;

import com.casic.patrol.indication.persistence.manager.IndicationInfoManager;
import com.casic.patrol.indication.support.Indication;
import com.casic.patrol.indication.support.IndicationResult;
import com.casic.patrol.indication.support.IndicationUserType;
import com.casic.patrol.overtime.persistence.manager.TaskOvertimeInfoManager;
import com.casic.patrol.party.persistence.domain.PartyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IndicationStatistics{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IndicationInfoManager indicationInfoManager;

    @Resource
    private TaskOvertimeInfoManager taskOvertimeInfoManager;

    public IndicationResult getBy(PartyEntity person, String dep,
                                  Date start, Date end) {
        IndicationUserType type = IndicationUserType.getByName(dep);
        switch (type) {
            case PATROL:
                return getPatrolBy(person,
                        taskOvertimeInfoManager.getUnfinishedOvertimeTaskBy(
                                start, end, patrolTaskCode, patrolArgs
                        ),
                        indicationInfoManager.getMissingReportNum(
                                start, end
                        ), start, end);
            case CENTER:
                return getCenterBy(person,
                        taskOvertimeInfoManager.getUnfinishedOvertimeTaskBy(
                                start, end, centerTaskCode, centerArgs
                        ), start, end);
            case COMPANY:
                return getCompanyBy(person,
                        taskOvertimeInfoManager.getUnfinishedOvertimeTaskBy(
                                start, end, companyTaskCode, companyArgs
                        ), start, end);
            default: logger.error("Chinese Log : 获取指标错误[种类提供错误]");
        }
        return new IndicationResult();
    }

    public List<IndicationResult> getBy(List<PartyEntity> persons,
                                        String dep,
                                        Date start, Date end) {
        List<IndicationResult> results = new ArrayList<IndicationResult>();
        IndicationUserType type = IndicationUserType.getByName(dep);
        switch (type) {
            case PATROL:
                Map<String, Integer> patrolTaskMap =
                        taskOvertimeInfoManager.getUnfinishedOvertimeTaskBy(
                                start, end, patrolTaskCode, patrolArgs
                        );
                Map<String, Long> missingReportMap =
                        indicationInfoManager.getMissingReportNum(
                                start, end
                        );
                for (PartyEntity person : persons) {
                    IndicationResult result = getPatrolBy(
                            person, patrolTaskMap,
                            missingReportMap, start, end
                    );
                    result.setPerson(person);
                    result.setDepartment(dep);
                    results.add(result);
                }
                break;
            case CENTER:
                Map<String, Integer> centerTaskMap =
                        taskOvertimeInfoManager.getUnfinishedOvertimeTaskBy(
                                start, end, centerTaskCode, centerArgs
                        );
                for (PartyEntity person : persons) {
                    IndicationResult result = getCenterBy(
                            person, centerTaskMap, start, end
                    );
                    result.setPerson(person);
                    result.setDepartment(dep);
                    results.add(result);
                }
                break;
            case COMPANY:
                Map<String, Integer> companyTaskMap =
                        taskOvertimeInfoManager.getUnfinishedOvertimeTaskBy(
                                start, end, companyTaskCode, companyArgs
                        );
                for (PartyEntity person : persons) {
                    IndicationResult result = getCompanyBy(
                            person, companyTaskMap, start, end
                    );
                    result.setPerson(person);
                    result.setDepartment(dep);
                    results.add(result);
                }
                break;
            default: logger.error("Chinese Log : 获取指标错误[种类提供错误]");
        }
        return results;
    }

    private String centerArgs = "commandCenter";

    private String centerTaskCode = "taskuser-4";

    /**
     * 查询指挥中心相关指标
     * @param person
     * @param start
     * @param end
     * @return
     */
    private IndicationResult getCenterBy(
            PartyEntity person,
            Map<String, Integer> unfinishedOvertimeTaskMap,
            Date start, Date end) {
        IndicationResult result = new IndicationResult();
        result.setReportNum(indicationInfoManager.getNumberBy(
                Indication.REPORT.name(), person.getRef(), start, end
        ).doubleValue());
        result.setAssignNum(indicationInfoManager.getNumberBy(
                Indication.ASSIGN.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDispatchShouldNum(indicationInfoManager.getNumberBy(
                Indication.DISPATCH_SHOULD.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDispatchNum(indicationInfoManager.getNumberBy(
                Indication.DISPATCH.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDispatchOntimeNum(indicationInfoManager.getNumberBy(
                Indication.DISPATCH_ONTIME.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDispatchOverdueNum(indicationInfoManager.getNumberBy(
                Indication.DISPATCH_OVERDUE.name(), person.getRef(), start, end
        ).doubleValue());
        Integer count = unfinishedOvertimeTaskMap.get(person.getRef());
        if (count == null) {
            count = 0;
        }
        Double total = result.getDispatchOverdueNum() + count.doubleValue();
        result.setUndispatchOverdueNum(total);
        return result;
    }

    private String patrolArgs = "verifyUser";

    private String patrolTaskCode = "taskuser-3";

    private IndicationResult getPatrolBy(
            PartyEntity person,
            Map<String, Integer> unfinishedOvertimeTaskMap,
            Map<String, Long> missingReportTaskMap,
            Date start, Date end) {
        IndicationResult result = new IndicationResult();
        result.setVerifyNum(indicationInfoManager.getNumberBy(
                Indication.VERIFY.name(), person.getRef(), start, end
        ).doubleValue());
        result.setVerifyShouldNum(indicationInfoManager.getNumberBy(
                Indication.VERIFY_SHOULD.name(), person.getRef(), start, end
        ).doubleValue());
        result.setVerifyOntimeNum(indicationInfoManager.getNumberBy(
                Indication.VERIFY_ONTIME.name(), person.getRef(), start, end
        ).doubleValue());
        result.setVerifyOverdueNum(indicationInfoManager.getNumberBy(
                Indication.VERIFY_OVERDUE.name(), person.getRef(), start, end
        ).doubleValue());
        Integer count = unfinishedOvertimeTaskMap.get(person.getRef());
        if (count == null) {
            count = 0;
        }
        Double total = result.getVerifyOverdueNum() + count.doubleValue();
        result.setUnverifyOverdueNum(total);
        Long number = missingReportTaskMap.get(person.getRegion());
        if (number == null) {
            number = 0l;
        }
        result.setMissingReportNum(number.doubleValue());
        return result;
    }

    private String companyArgs = "company";

    private String companyTaskCode = "taskuser-5";

    private IndicationResult getCompanyBy(
            PartyEntity person,
            Map<String, Integer> unfinishedOvertimeTaskMap,
            Date start, Date end) {
        IndicationResult result = new IndicationResult();
        result.setDisposeNum(indicationInfoManager.getNumberBy(
                Indication.DISPOSE.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDisposeShouldNum(indicationInfoManager.getNumberBy(
                Indication.DISPOSE_SHOULD.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDisposeOntimeNum(indicationInfoManager.getNumberBy(
                Indication.DISPOSE_ONTIME.name(), person.getRef(), start, end
        ).doubleValue());
        result.setDisposeOverdueNum(indicationInfoManager.getNumberBy(
                Indication.DISPOSE_OVERDUE.name(), person.getRef(), start, end
        ).doubleValue());
        Integer count = unfinishedOvertimeTaskMap.get(person.getRef());
        if (count == null) {
            count = 0;
        }
        Double total = result.getDisposeOverdueNum() + count.doubleValue();
        result.setUndisposeOverdueNum(total);
        result.setReworkNum(indicationInfoManager.getReworkNum(
                person.getRef(), start, end, "2"//返工第二次不算做返工数
        ).doubleValue());
        return result;
    }
}
