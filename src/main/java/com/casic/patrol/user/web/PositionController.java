package com.casic.patrol.user.web;

import com.casic.patrol.humantask.persistence.manager.TaskInfoManager;
import com.casic.patrol.user.domain.User;
import com.casic.patrol.user.dto.PositionDto;
import com.casic.patrol.user.dto.TaskTraceDto;
import com.casic.patrol.user.dto.UserDto;
import com.casic.patrol.user.manager.PositionManager;
import com.casic.patrol.user.manager.UserManager;
import com.casic.patrol.util.DataTable;
import com.casic.patrol.util.DataTableParameter;
import com.casic.patrol.util.DataTableUtils;
import com.casic.patrol.util.StringUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/1/18.
 */

@Controller
@RequestMapping("position")
public class PositionController {

    @Resource
    private PositionManager positionManager;
    @Resource
    private UserManager userManager;

    @Resource
    private TaskInfoManager taskInfoManager;

    @RequestMapping("trace")
    @ResponseBody
    public Map<String,Object> getUserPositions(@RequestParam(value = "userId", required = true) Long userId,
                                               @RequestParam(value = "locaTime", required = true) String locationTime
    ) throws ParseException{
        Map<String,Object> result = new HashMap<String,Object>();
        User user = userManager.getUserById(userId);
        List<PositionDto> positionDtoList = positionManager.findPositions(user, locationTime);
        result.put("positions", positionDtoList);
        return result;
    }

    @RequestMapping("taskTrace")
    @ResponseBody
    public Map<String,Object> getTaskPositions(@RequestParam(value = "userId", required = true) Long userId,
                                               @RequestParam(value = "startTime", required = true) String startTime,
                                               @RequestParam(value = "endTime", required = true) String endTime
    ) throws ParseException{
        Map<String,Object> result = new HashMap<String,Object>();
        User user = userManager.getUserById(userId);
        List<PositionDto> positionDtoList = positionManager.findPositionsByTimeRange(user, startTime, endTime);
        result.put("positions", positionDtoList);
        return result;
    }

    @RequestMapping("workspace-historyTrace")
    public String getUsersByRoleName(Model model){

        List<User> users = userManager.getUserByRoleName("巡查部门");
        model.addAttribute("success","false");
        model.addAttribute("message","获取角色下用户列表出现错误，请查看日志");
        if(users.size()> 0){
            model.addAttribute("success","ok");
            model.addAttribute("message","获取角色下用户列表成功");
            model.addAttribute("users", UserDto.Converts(users));
        }
        return "bpm/workspace-historyTrace";
    }
    @RequestMapping("workspace-taskTrace")
    public String taskTrace(Model model){


        return "bpm/workspace-taskTrace";
    }

    @RequestMapping("get-confirmed-task")
    public void getConfirmedTask(String jsonParam, HttpServletResponse response){
        try {
            DataTable<TaskTraceDto> taskTraceDtoDataTable = taskInfoManager.getTaskInfoForPosition(jsonParam);

//            //测试
//            DataTable<TaskTraceDto> taskTraceDtoDataTable = new DataTable<TaskTraceDto>();
//            DataTableParameter parameter = DataTableUtils.getDataTableParameterByJsonParam(jsonParam);
//            TaskTraceDto taskTraceDto = new TaskTraceDto();
//            taskTraceDto.setId((long) 1);
//            taskTraceDto.setTaskId("0000001");
//            taskTraceDto.setConfirmUser_id((long) 4);
//            taskTraceDto.setConfirmUser_name("陈鑫");
//            taskTraceDto.setEndTime("2016-06-23 12:12:12");
//            taskTraceDto.setStartTime("2016-06-21 11:12:12");
//            taskTraceDto.setTaskName("事故");
//            List<TaskTraceDto> taskTraceDtos = new ArrayList<TaskTraceDto>();
//            taskTraceDtos.add(taskTraceDto);
//            taskTraceDtoDataTable.setAaData(taskTraceDtos);
//            taskTraceDtoDataTable.setiTotalDisplayRecords(1);
//            taskTraceDtoDataTable.setiTotalRecords(1);
//            taskTraceDtoDataTable.setsEcho(parameter.getsEcho());

            Gson gson = new Gson();
            String json = gson.toJson(taskTraceDtoDataTable);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
