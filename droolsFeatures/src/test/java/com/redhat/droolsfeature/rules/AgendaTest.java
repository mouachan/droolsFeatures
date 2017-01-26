package com.redhat.droolsfeature.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kie.api.definition.type.FactType;

import com.redhat.droolsfeature.runner.RulesRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


public class AgendaTest {
	private static Logger logger = Logger.getLogger(AgendaTest.class);
	long initStartTime;
	long runStartTime;
	

	@Test
	public void testAgendaGroupProblem() {
		logger.info("Test agenda group ");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("hrule.drl");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.droolsfeature.rules", "BzrMarketDeclare");

		try {

			Object market = marketType.newInstance();
			marketType.set(market, "airport", "LIL");

			objects.add(market);
			marketType.set(market, "airport", "LIL");
			objects.add(market);

			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			// runner.getSession().getAgenda().getAgendaGroup("abr.keywords").setFocus();
			List<String> rules = runner.run(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
			assertThat(rules, hasItems("MKT1"));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testWatchRule() {
		logger.info("Test watch rule");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("hrules-watch.drl");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.droolsfeature.rules", "BzrFlightDeclare");

		try {

			Object market = marketType.newInstance();
			marketType.set(market, "airline", "LH");
			marketType.set(market, "number", 500);
			objects.add(market);
			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			// runner.getSession().getAgenda().getAgendaGroup("abr.keywords").setFocus();
			List<String> rules = runner.run(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
			assertThat(rules, hasItems("FLG1","FLG2"));
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testBugLoop() {
		logger.info("Test BUG Rule");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("rule_BUG.drl");
		FactType BookingType = runner.getKieBase().getFactType("com.redhat.droolsfeature.rules", "Booking");
		FactType BzrFlightDeclareType = runner.getKieBase().getFactType("com.redhat.droolsfeature.rules",
				"BzrFlightDeclare");

		try {
			for (int i = 0; i < 100; i++) {
				logger.info("############Iteration : "+i+" ##################");
				Object booking = BookingType.newInstance();
				BookingType.set(booking, "customer", "Bob");

				Object flight = BzrFlightDeclareType.newInstance();
				BzrFlightDeclareType.set(flight, "airline", "1A");
				BzrFlightDeclareType.set(flight, "number", 500);

				Object returnFlight = BzrFlightDeclareType.newInstance();
				BzrFlightDeclareType.set(returnFlight, "airline", "AF");
				BzrFlightDeclareType.set(returnFlight, "number", 666);

				BookingType.set(booking, "flight", flight);
				BookingType.set(booking, "returnFlight", returnFlight);

				objects.add(returnFlight);
				objects.add(flight);
				objects.add(booking);

				logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
				runStartTime = System.currentTimeMillis();
				runner.setSession();
				runner.runRule(objects);
			
				logger.info("running rules elapsed time "
						+ (System.currentTimeMillis() - runStartTime) + "ms");
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
