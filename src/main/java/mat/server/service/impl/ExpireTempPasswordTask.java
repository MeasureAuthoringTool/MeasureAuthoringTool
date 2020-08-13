package mat.server.service.impl;

import mat.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import java.util.Date;

/**
 * The Class ExpireTempPasswordTask.
 */
public class ExpireTempPasswordTask {
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * The Class Task.
	 */
	private class Task implements Runnable {

	    /**
		 * Instantiates a new task.
		 */
    	public Task() {
	    }

	    /* (non-Javadoc)
    	 * @see java.lang.Runnable#run()
    	 */
    	public void run() {
	    	long currentDate = System.currentTimeMillis();
	    	long twentyFourHours = 24 * 60 * 60 * 1000;
	    	long fiveDays = 5 * twentyFourHours;
	    	Date targetDate = new Date(currentDate - fiveDays);
	    	userDAO.expireTemporaryPasswords(targetDate);
	    }

	  }

	  /** The task executor. */
  	private TaskExecutor taskExecutor;

	  /**
	 * Instantiates a new expire temp password task.
	 * 
	 * @param taskExecutor
	 *            the task executor
	 */
  	public ExpireTempPasswordTask(TaskExecutor taskExecutor) {
	    this.taskExecutor = taskExecutor;
	  }

	  /**
	 * Expire temp password.
	 */
  	public void expireTempPassword() {
	    taskExecutor.execute(new Task());
	  }

}
