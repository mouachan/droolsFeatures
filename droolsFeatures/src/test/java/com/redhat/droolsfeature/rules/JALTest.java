package com.redhat.droolsfeature.rules;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kie.api.definition.type.FactType;

import com.redhat.droolsfeature.runner.RulesRunner;

public class JALTest {
	private static Logger logger = Logger.getLogger(AgendaTest.class);
	long initStartTime;
	long runStartTime;

	@Test
	public void testCubaRule() {
		logger.info("Cuba rule Test");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("jal.drl");
		FactType tsType = runner.getKieBase().getFactType("com.redhat.jal", "Ts");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.jal", "BzrMarketDeclare");
		FactType segmentType = runner.getKieBase().getFactType("com.redhat.jal", "Segment");

		try {

			Object origin = marketType.newInstance();
			marketType.set(origin, "country", "CUBA");

			Object segment = segmentType.newInstance();
			segmentType.set(segment, "origin", origin);

			Object ts = tsType.newInstance();
			List<Object> segments = new ArrayList<Object>();
			segments.add(segment);
			tsType.set(ts, "segments", segments);
			objects.add(ts);

			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
			boolean valid = (Boolean) tsType.get(ts, "valid");
			assertFalse(valid);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testIsraelArabRule() {
		logger.info("Arab or Israel rule Test");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("jal.drl");
		FactType tsType = runner.getKieBase().getFactType("com.redhat.jal", "Ts");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.jal", "BzrMarketDeclare");
		FactType segmentType = runner.getKieBase().getFactType("com.redhat.jal", "Segment");

		try {

			Object origin = marketType.newInstance();
			marketType.set(origin, "country", "ISRAEL");

			Object destination = marketType.newInstance();
			marketType.set(destination, "country", "ARAB");

			Object segment = segmentType.newInstance();
			segmentType.set(segment, "origin", origin);
			segmentType.set(segment, "destination", destination);

			Object ts = tsType.newInstance();
			List<Object> segments = new ArrayList<Object>();
			segments.add(segment);
			tsType.set(ts, "segments", segments);
			objects.add(ts);

			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object createMarket(String city, FactType marketType)
			throws InstantiationException, IllegalAccessException {
		Object market = marketType.newInstance();
		marketType.set(market, "country", city);
		return market;
	}

	private Object createCarrier(FactType carrierType, String carrierCode, String opCarrier1, String opCarrier2)
			throws InstantiationException, IllegalAccessException {
		Object carrier = carrierType.newInstance();
		carrierType.set(carrier, "carrierCode", carrierCode);
		carrierType.set(carrier, "opCarrier1", opCarrier1);
		carrierType.set(carrier, "opCarrier2", opCarrier2);
		return carrier;

	}

	private Object createSegment(FactType segmentType, int id, Object origin, Object destination, Object carrier)
			throws InstantiationException, IllegalAccessException {
		Object segment = segmentType.newInstance();
		segmentType.set(segment, "id", id);
		segmentType.set(segment, "origin", origin);
		segmentType.set(segment, "destination", destination);
		segmentType.set(segment, "carrier", carrier);

		return segment;
	}

	@Test
	public void testFranceToPolyRule_Fired() {
		logger.info("France Poly rule Test");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("jal.drl");
		FactType tsType = runner.getKieBase().getFactType("com.redhat.jal", "Ts");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.jal", "BzrMarketDeclare");
		FactType segmentType = runner.getKieBase().getFactType("com.redhat.jal", "Segment");
		FactType carrierType = runner.getKieBase().getFactType("com.redhat.jal", "Carrier");

		try {

			List<Object> segments = new ArrayList<Object>();
			segments.add(createSegment(segmentType, 1, createMarket("FR", marketType), createMarket("NY", marketType),
					createCarrier(carrierType, "TEST", "Test", "Test")));
			segments.add(createSegment(segmentType, 2, createMarket("NY", marketType), createMarket("MEX", marketType),
					createCarrier(carrierType, "JAP", "Test", "Test")));
			segments.add(createSegment(segmentType, 3, createMarket("MEX", marketType), createMarket("AL", marketType),
					createCarrier(carrierType, "JAP", "Test", "Test")));
			segments.add(createSegment(segmentType, 4, createMarket("AL", marketType), createMarket("NICE", marketType),
					createCarrier(carrierType, "JAP", "Test", "Test")));
			segments.add(createSegment(segmentType, 5, createMarket("NICE", marketType),
					createMarket("POLY", marketType), createCarrier(carrierType, "Test", "Test", "Test")));

			Object ts = tsType.newInstance();
			tsType.set(ts, "segments", segments);
			tsType.set(ts, "valid", true);
			objects.add(ts);

			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");

			boolean valid = (Boolean) tsType.get(ts, "valid");

			assertFalse(valid);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFranceToPolyRule_Not_Fired() {
		logger.info("France Poly rule Test");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("jal.drl");
		FactType tsType = runner.getKieBase().getFactType("com.redhat.jal", "Ts");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.jal", "BzrMarketDeclare");
		FactType segmentType = runner.getKieBase().getFactType("com.redhat.jal", "Segment");
		FactType carrierType = runner.getKieBase().getFactType("com.redhat.jal", "Carrier");

		try {

			List<Object> segments = new ArrayList<Object>();
			segments.add(createSegment(segmentType, 1, createMarket("FR", marketType), createMarket("NY", marketType),
					createCarrier(carrierType, "TEST", "Test", "Test")));
			segments.add(createSegment(segmentType, 2, createMarket("NY", marketType), createMarket("MEX", marketType),
					createCarrier(carrierType, "JAP", "Test", "Test")));
			segments.add(createSegment(segmentType, 3, createMarket("MEX", marketType), createMarket("AL", marketType),
					createCarrier(carrierType, "test", "Test", "Test")));
			segments.add(createSegment(segmentType, 4, createMarket("AL", marketType), createMarket("NICE", marketType),
					createCarrier(carrierType, "JAP", "Test", "Test")));
			segments.add(createSegment(segmentType, 5, createMarket("NICE", marketType),
					createMarket("POLY", marketType), createCarrier(carrierType, "Test", "Test", "Test")));

			Object ts = tsType.newInstance();
			tsType.set(ts, "segments", segments);
			tsType.set(ts, "valid", true);
			objects.add(ts);

			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");

			boolean valid = (Boolean) tsType.get(ts, "valid");

			assertTrue(valid);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFranceToPolyRule_Multiple() {
		logger.info("France Poly rule Test");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("jal.drl");
		FactType tsType = runner.getKieBase().getFactType("com.redhat.jal", "Ts");
		FactType marketType = runner.getKieBase().getFactType("com.redhat.jal", "BzrMarketDeclare");
		FactType segmentType = runner.getKieBase().getFactType("com.redhat.jal", "Segment");
		FactType carrierType = runner.getKieBase().getFactType("com.redhat.jal", "Carrier");

		try {

			List<Object> segments = new ArrayList<Object>();
			segments.add(createSegment(segmentType, 1, createMarket("FR", marketType), createMarket("NY", marketType),
					createCarrier(carrierType, "TEST", "Test", "Test")));
			segments.add(createSegment(segmentType, 2, createMarket("NY", marketType), createMarket("MEX", marketType),
					createCarrier(carrierType, "Test", "Test", "Test")));
			segments.add(createSegment(segmentType, 3, createMarket("MEX", marketType), createMarket("AL", marketType),
					createCarrier(carrierType, "Test", "Test", "Test")));
			segments.add(createSegment(segmentType, 4, createMarket("AL", marketType), createMarket("NICE", marketType),
					createCarrier(carrierType, "Test", "Test", "Test")));
			segments.add(createSegment(segmentType, 5, createMarket("NICE", marketType),
					createMarket("POLY", marketType), createCarrier(carrierType, "Test", "Test", "Test")));

			Object ts = tsType.newInstance();
			tsType.set(ts, "segments", segments);
			objects.add(ts);

			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
			boolean valid = (Boolean) tsType.get(ts, "valid");

			assertFalse(valid);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
