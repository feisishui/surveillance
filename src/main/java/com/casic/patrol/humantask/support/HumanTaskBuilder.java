package com.casic.patrol.humantask.support;


import com.casic.patrol.api.humantask.HumanTaskDTO;

import java.util.Date;

public class HumanTaskBuilder {
    public HumanTaskDTO create() {
        HumanTaskDTO humanTaskDto = new HumanTaskDTO();

        humanTaskDto.setDelegateStatus("none");
        humanTaskDto.setCreateTime(new Date());
        humanTaskDto.setSuspendStatus("none");
        humanTaskDto.setStatus("active");

        return humanTaskDto;
    }
}
