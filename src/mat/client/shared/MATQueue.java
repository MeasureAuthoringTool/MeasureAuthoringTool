package mat.client.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Queue implementation with add and poll (remove) operations NOTE: this
 * queue is agnostic to the Object type placed in it.
 * 
 * @author aschmidt
 */
public class MATQueue {

	/** The queue. */
	List<Object> queue = new ArrayList<Object>();
	
	/**
	 * Instantiates a new mAT queue.
	 */
	public MATQueue(){
	}
	
	/**
	 * Adds the.
	 * 
	 * @param o
	 *            the o
	 */
	public void add(Object o){
		queue.add(o);
	}
	
	/**
	 * Poll.
	 * 
	 * @return the object
	 */
	public Object poll(){
		if(!isEmpty()){
			Object ret = queue.get(0);
			queue.remove(0);
			return ret;
		}
		return null;
	}
	
	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty(){
		return queue.size() <= 0;
	}
	
	/**
	 * Size.
	 * 
	 * @return the int
	 */
	public int size(){
		return queue.size();
	}
	
	/**
	 * Clear.
	 */
	public void clear(){
		queue.clear();
	}
}
