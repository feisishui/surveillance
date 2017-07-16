package com.casic.patrol.humantask.listener;


import com.casic.patrol.humantask.persistence.domain.TaskInfo;

public interface HumanTaskListener {
    void onCreate(TaskInfo taskInfo) throws Exception;

    void onComplete(TaskInfo taskInfo) throws Exception;
}
