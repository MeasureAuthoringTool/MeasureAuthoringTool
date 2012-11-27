package org.ifmc.mat.server.service.impl;

import java.util.Date;

import org.ifmc.mat.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class ExpireTempPasswordTask {
	@Autowired
	private UserDAO userDAO;
	
	private class Task implements Runnable {

	    public Task() {
	    }

	    public void run() {
	    	long currentDate = System.currentTimeMillis();
	    	long twentyFourHours = 24 * 60 * 60 * 1000;
	    	long fiveDays = 5 * twentyFourHours;
	    	Date targetDate = new Date(currentDate - fiveDays);
	    	userDAO.expireTemporaryPasswords(targetDate);
	    }

	  }

	  private TaskExecutor taskExecutor;

	  public ExpireTempPasswordTask(TaskExecutor taskExecutor) {
	    this.taskExecutor = taskExecutor;
	  }

	  public void expireTempPassword() {
	    taskExecutor.execute(new Task());
	  }

}
