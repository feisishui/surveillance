package com.casic.patrol.overtime.web;

import com.casic.patrol.core.mapper.BeanMapper;
import com.casic.patrol.humantask.persistence.domain.TaskDefBase;
import com.casic.patrol.humantask.persistence.manager.TaskDefBaseManager;
import com.casic.patrol.overtime.dto.OvertimeInfoDto;
import com.casic.patrol.overtime.persistence.domain.EmergencyLevel;
import com.casic.patrol.overtime.persistence.domain.EventType;
import com.casic.patrol.overtime.persistence.domain.OvertimeInfo;
import com.casic.patrol.overtime.persistence.manager.OvertimeInfoManager;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.ExecInfo;
import com.casic.patrol.util.Pair;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("overtime")
public class OvertimeController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${overtime.default.process.definition.key}")
    private String defaultProcessKey;

    public static final Float warnToAlarmRatio = 0.8f;

    @Resource
    private OvertimeInfoManager overtimeInfoManager;

    @Resource
    private TaskDefBaseManager taskDefBaseManager;


    @RequestMapping("overtime-info-list")
    public void list(String jsonParam,
                     HttpServletResponse response) {
        try {
            DataTable<OvertimeInfoDto> userList = overtimeInfoManager.pageInfo(jsonParam);
            Gson gson = new Gson();
            String json = gson.toJson(userList);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("overtime-info-delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "id", required = true) Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecInfo temp = overtimeInfoManager.deleteById(id);
        map.put("success", temp.isSucc());
        map.put("message", temp.getMsg());
        return map;
    }

    @RequestMapping("overtime-info-edit")
    public String edit(@RequestParam(value = "id", required = false) Long id,
                       @RequestParam(value = "processKey", required = false) String processKey,
                       Model model) {
        OvertimeInfoDto infoDto = new OvertimeInfoDto();
        if (id != null) {
            infoDto = overtimeInfoManager.getDTOByID(id);
        }
        model.addAttribute("model", infoDto);
        model.addAttribute("emergencyLevel", EmergencyLevel.emergencyLevels);
        model.addAttribute("eventType", EventType.eventTypes);
        model.addAttribute("taskName", getTaskCodeAndName(processKey));
        return "overtime/overtime-info-edit";
    }

    public static final Set<String> TASK_USEFUL = new HashSet<String>(Arrays.asList(
            "事故上报", "事故结案"
    ));

    private List<Pair> getTaskCodeAndName(String processKey) {
        if (processKey == null) {
            processKey = defaultProcessKey;
        }
        String hql = "from TaskDefBase where processDefinitionKey=? order by id asc";
        List<TaskDefBase> list = taskDefBaseManager.find(hql, processKey);
        List<Pair> result = new ArrayList<Pair>();
        for (TaskDefBase temp : list) {
            if (!TASK_USEFUL.contains(temp.getName()))
                result.add(new Pair<String, String>(temp.getCode(), temp.getName()));
        }
        return result;
    }

    @RequestMapping("overtime-info-save")
    @ResponseBody
    public Map<String, Object> save(@ModelAttribute OvertimeInfoDto infoDto) {
        Map<String, Object> map = new HashMap<String, Object>();
        ExecInfo regalationNameCheck = overtimeInfoManager.checkRegulationNameUnique(infoDto.getRegulationName(), infoDto.getId());
        if (!regalationNameCheck.isSucc()) {
            map.put("success", false);
            map.put("message", regalationNameCheck.getMsg());
            return map;
        }
        OvertimeInfo info = saveOvertimeInfoFromDto(infoDto);
        ExecInfo result = overtimeInfoManager.checkInfoUnique(info);
        if (result.isSucc()) {
            try {
                overtimeInfoManager.save(info);
            } catch (Exception e) {
                map.put("success", false);
                map.put("message", "保存失败[" + e.getMessage() + "]");
                return map;
            }
        }
        map.put("success", result.isSucc());
        map.put("message", result.isSucc() ?
                infoDto.getId() == null ? "新增用户成功" : "编辑用户成功"
                : result.getMsg());
        return map;
    }

    private OvertimeInfo saveOvertimeInfoFromDto(OvertimeInfoDto infoDto) {
        OvertimeInfo overtimeInfo = new OvertimeInfo();
        BeanMapper beanMapper = new BeanMapper();
        beanMapper.copy(infoDto, overtimeInfo, OvertimeInfoDto.class, OvertimeInfo.class);
        overtimeInfo.setStartTaskName(
            getTaskNameBy(overtimeInfo.getProcessDefinitionKey(), overtimeInfo.getStartTaskCode())
        );
        overtimeInfo.setEndTaskCode(overtimeInfo.getStartTaskCode());
        overtimeInfo.setEndTaskName(overtimeInfo.getStartTaskName());
        overtimeInfo.setWarnTime(overtimeInfo.getAlarmTime() * warnToAlarmRatio);
        if (null == overtimeInfo.getProcessDefinitionKey()) {
            overtimeInfo.setProcessDefinitionKey(defaultProcessKey);
        }
        return overtimeInfo;
    }

    private String getTaskNameBy(String processKey, String code) {
        if (processKey == null) {
            processKey = defaultProcessKey;
        }
        String hql = "from TaskDefBase where processDefinitionKey=? and code=?";
        TaskDefBase temp = taskDefBaseManager.findUnique(hql, processKey, code);
        return temp == null ? null : temp.getName();
    }
}
