package mat.server.service.impl;

import mat.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import java.util.Date;

/**
 * The Class UnlockUserTask.
 */
public class UnlockUserTask {
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * The Class UnlockUserTaskRunnable.
	 */
	private class UnlockUserTaskRunnable implements Runnable {

	    /**
		 * Instantiates a new unlock user task runnable.
		 */
    	public UnlockUserTaskRunnable() {
	    }

	    /* (non-Javadoc)
    	 * @see java.lang.Runnable#run()
    	 */
    	public void run() {
	    	long currentDate = System.currentTimeMillis();
	    	long twentyFourHours = 24 * 60 * 60 * 1000;
	    	Date unlockDate = new Date(currentDate - twentyFourHours);
	    	userDAO.unlockUsers(unlockDate);
	    }

	  }

	  /** The task executor. */
  	private TaskExecutor taskExecutor;

	  /**
	 * Instantiates a new unlock user task.
	 * 
	 * @param taskExecutor
	 *            the task executor
	 */
  	public UnlockUserTask(TaskExecutor taskExecutor) {
	    this.taskExecutor = taskExecutor;
	  }

	  /**
	 * Unlock users.
	 */
  	public void unlockUsers() {
	    taskExecutor.execute(new UnlockUserTaskRunnable());
	  }

}
