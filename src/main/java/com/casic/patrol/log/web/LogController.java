package com.casic.patrol.log.web;

import com.casic.patrol.core.mapper.JsonMapper;
import com.casic.patrol.log.dto.LogDTO;
import com.casic.patrol.log.manager.LogManager;
import com.casic.patrol.util.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping("log")
public class LogController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private LogManager logManager;

    @RequestMapping("log-info-list")
    public void listLog(String jsonParam , HttpServletResponse response,
                        @RequestParam(value = "day1",required = false) String day1,
                        @RequestParam(value = "day2",required = false) String day2) throws ParseException {
        try {
            DataTable<LogDTO> dt = logManager.queryLogByPage(jsonParam, day1, day2, 0l);
            String json = new JsonMapper().toJson(dt);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
