package com.redhat.droolsfeature.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.redhat.droolsfeature.model.Booking;
import com.redhat.droolsfeature.model.BzrFlightDeclare;
import com.redhat.droolsfeature.runner.RulesRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class OptimRulesInsertTest {

	private RulesRunner runner = null;
	protected ArrayList<Object> objects;
	private Logger logger = Logger.getLogger(OptimRulesInsertTest.class);
	long initStartTime;
	long runStartTime;
	Booking booking = null;
	BzrFlightDeclare bzrfd = null;

	private void createObjects() {
		booking = new Booking();
		bzrfd = new BzrFlightDeclare();
		bzrfd.setAirline("1A");
		booking.setFlight(bzrfd);
		objects = new ArrayList<Object>();
		objects.add(booking);
		objects.add(booking.getFlight());
	}

	@Test
	public void testOptimUsingInsertAndExist() {
		logger.info("Test without from");
		for (int i = 0; i < 10; i++) {
			createObjects();
			initStartTime = System.currentTimeMillis();
			runner = new RulesRunner();
			runner.initialisation("rules.drl");
			runner.setSession();
			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			List<String>rules = runner.run(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
			assertThat(rules, hasItems("add match","evaluate booking customer"));

		}
	}

	

}
