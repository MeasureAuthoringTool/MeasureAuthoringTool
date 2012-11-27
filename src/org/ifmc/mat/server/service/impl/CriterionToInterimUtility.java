package org.ifmc.mat.server.service.impl;

import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.shared.model.IDecision;
import org.ifmc.mat.shared.model.IQDSTerm;

/**
 * 
 * @author aschmidt
 *
 */
public class CriterionToInterimUtility {

	public IQDSTerm peel(IDecision decision) {
		if(decision instanceof Clause){
			Clause cl = (Clause)decision;
			if (! cl.getDecisions().isEmpty()) {
				if (cl.getDecisions().get(0) instanceof Clause) {
					return peel(cl.getDecisions().get(0));
				} else {		
					return (IQDSTerm)cl.getDecisions().get(0);
				}
			} else {
				return null;
			}
		} else if(decision instanceof IQDSTerm){
			return (IQDSTerm)decision;
		}
		return null;
	}
}
