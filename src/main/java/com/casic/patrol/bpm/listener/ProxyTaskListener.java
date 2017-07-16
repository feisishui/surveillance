package com.casic.patrol.bpm.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class ProxyTaskListener implements TaskListener {
    private List<TaskListener> taskListeners = Collections.EMPTY_LIST;

    private static Logger logger = LoggerFactory
            .getLogger(ProxyTaskListener.class);

    public void notify(DelegateTask delegateTask) {
        for (TaskListener taskListener : taskListeners) {
            logger.info("监听器名称:" + taskListener.getClass().getName() + " 方法:" + delegateTask.getEventName() + "任务名称:" +
                    delegateTask.getName());
            taskListener.notify(delegateTask);

        }
    }

    public void setTaskListeners(List<TaskListener> taskListeners) {
        this.taskListeners = taskListeners;
    }
}
