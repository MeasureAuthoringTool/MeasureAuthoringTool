package org.ifmc.mat.server.service.impl;

import java.util.Date;

import org.ifmc.mat.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class UnlockUserTask {
	@Autowired
	private UserDAO userDAO;
	
	private class UnlockUserTaskRunnable implements Runnable {

	    public UnlockUserTaskRunnable() {
	    }

	    public void run() {
	    	long currentDate = System.currentTimeMillis();
	    	long twentyFourHours = 24 * 60 * 60 * 1000;
	    	Date unlockDate = new Date(currentDate - twentyFourHours);
	    	userDAO.unlockUsers(unlockDate);
	    }

	  }

	  private TaskExecutor taskExecutor;

	  public UnlockUserTask(TaskExecutor taskExecutor) {
	    this.taskExecutor = taskExecutor;
	  }

	  public void unlockUsers() {
	    taskExecutor.execute(new UnlockUserTaskRunnable());
	  }

}
