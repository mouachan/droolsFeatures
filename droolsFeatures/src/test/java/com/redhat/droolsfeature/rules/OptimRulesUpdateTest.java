package com.redhat.droolsfeature.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.redhat.droolsfeature.model.Booking;
import com.redhat.droolsfeature.model.BzrFlightDeclare;
import com.redhat.droolsfeature.model.Fact;
import com.redhat.droolsfeature.runner.RulesRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class OptimRulesUpdateTest {

	private RulesRunner runner = null;
	private ArrayList<Object> objects;
	private Logger logger = Logger.getLogger(OptimRulesUpdateTest.class);
	long initStartTime;
	long runStartTime;
	Booking booking = null;
	BzrFlightDeclare bzrfd = null;
	List<String> rules = null;
	List<String> rules2 = null;

	private void createObjects() {
		booking = new Booking();
		bzrfd = new BzrFlightDeclare();
		bzrfd.setAirline("1A");
		booking.setFlight(bzrfd);
		objects = new ArrayList<Object>();
		objects.add(booking);
		objects.add(booking.getFlight());
	}

	private void createObj() {
		Fact fact = new Fact();
		fact.setValue(42);
		objects = new ArrayList<Object>();
		objects.add(fact);
	}

	private void runRule() {
		RulesRunner runner = new RulesRunner();
		runner.initialisation("init.drl");
		runner.setSession();
		objects = new ArrayList<Object>();
		runner.runRule(objects);
	}

	@Test
	public void testOptimUsingUpdateExistAndFrom() {
		logger.info("Test optim using update exist and from");
		runRule();
		runner = new RulesRunner();
		RulesRunner runner2 = new RulesRunner();
		initStartTime = System.currentTimeMillis();
		runner.initialisation("rules2.drl");
		logger.info("Init elapsed time " + (System.currentTimeMillis() - initStartTime) + "ms");
		long initStartTime2 = System.currentTimeMillis();
		runner2.initialisation("rules.drl");
		logger.info("Init 2 elapsed time " + (System.currentTimeMillis() - initStartTime2) + "ms");
		long runStartTime2;
		// for (int i = 0; i < 100000; i++) {
		createObjects();
		runStartTime = System.currentTimeMillis();
		runner.setSession();
		runner.addAgendaListener();
		rules = runner.run(objects);
		assertThat(rules, hasItems("add match with from", "evaluate booking customer with from"));
		logger.info("running rules 2 elapsed time using update exist and from "
				+ (System.currentTimeMillis() - runStartTime) + "ms");
		createObjects();
		runStartTime2 = System.currentTimeMillis();
		runner2.setSession();
		runner2.addAgendaListener();
		rules2 = runner2.run(objects);
		assertThat(rules2, hasItems("add match", "evaluate booking customer"));
		logger.info("running rules 1 elapsed time using insert " + (System.currentTimeMillis() - runStartTime2) + "ms");
		// }

	}

}
