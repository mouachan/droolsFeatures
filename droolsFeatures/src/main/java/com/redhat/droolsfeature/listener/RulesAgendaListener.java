package com.redhat.droolsfeature.listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.runtime.rule.FactHandle;

public class RulesAgendaListener implements AgendaEventListener {

	private static Logger logger = Logger.getLogger(RulesAgendaListener.class);
	private List<String> rulesFired = new ArrayList<String>();
	private List<Object> objects = new ArrayList<Object>();
	
	public List<String> getRulesFired() {
		return this.rulesFired;
	}

	public void matchCreated(MatchCreatedEvent event) {
		// TODO Auto-generated method stub

	}

	public void matchCancelled(MatchCancelledEvent event) {
		// TODO Auto-generated method stub

	}

	public void beforeMatchFired(BeforeMatchFiredEvent event) {
		// TODO Auto-generated method stub

	}

	public void afterMatchFired(AfterMatchFiredEvent event) {
		String ruleName = event.getMatch().getRule().getName();
		rulesFired.add(ruleName);
		logger.info("***** Rule fired: '"+ruleName+"'");
		
		//if (logger.isDebugEnabled()) {
			Iterator<? extends FactHandle> iter = event.getMatch().getFactHandles().iterator();
			while (iter.hasNext()) {
				logger.info("  -> rule "+ruleName+" fired with following data: "+iter.next().toString());
			}
		//}
	}

	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
		// TODO Auto-generated method stub

	}

	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
		// TODO Auto-generated method stub

	}

	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub

	}

	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub

	}

	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub

	}

	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub

	}

}
