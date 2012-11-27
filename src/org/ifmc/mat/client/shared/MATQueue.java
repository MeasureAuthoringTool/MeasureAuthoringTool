package org.ifmc.mat.client.shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Queue implementation with add and poll (remove) operations
 * NOTE: this queue is agnostic to the Object type placed in it
 * @author aschmidt
 */
public class MATQueue {

	List<Object> queue = new ArrayList<Object>();
	
	public MATQueue(){
	}
	
	public void add(Object o){
		queue.add(o);
	}
	
	public Object poll(){
		if(!isEmpty()){
			Object ret = queue.get(0);
			queue.remove(0);
			return ret;
		}
		return null;
	}
	
	public boolean isEmpty(){
		return queue.size() <= 0;
	}
	
	public int size(){
		return queue.size();
	}
	
	public void clear(){
		queue.clear();
	}
}
