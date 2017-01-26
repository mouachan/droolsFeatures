package com.redhat.droolsfeature.rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.rule.GroupElement;
import org.drools.core.rule.IndexableConstraint;
import org.drools.core.spi.Constraint;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.parser.Column;
import org.drools.template.parser.DefaultGenerator;
import org.drools.template.parser.DefaultTemplateRuleBase;
import org.drools.template.parser.LongColumn;
import org.drools.template.parser.RuleTemplate;
import org.drools.template.parser.StringColumn;
import org.drools.template.parser.TemplateContainer;
import org.kie.api.definition.rule.Rule;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import com.redhat.droolsfeature.listener.RulesAgendaListener;
import com.redhat.droolsfeature.model.Cheese;
import com.redhat.droolsfeature.model.Person;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TemplateTest {

	private static Logger logger = Logger.getLogger(TemplateTest.class);

	//generate kjar and deployed on maven based on RuleTest project (with rule template and xls)
	@Test
	public void testMultipleTemplateRunRule() throws InstantiationException, IllegalAccessException {
		ReleaseId releaseId = new ReleaseIdImpl("com.sample", "RuleTest", "3.0.0");
		KieServices kieservices = KieServices.Factory.get();
		KieContainer container = kieservices.newKieContainer(releaseId);
		KieSession ksession = container.newKieSession("TemplatesKS");
		RulesAgendaListener agendaEventListener = new RulesAgendaListener();
		ksession.addEventListener(agendaEventListener);
		KieBase kbase = container.getKieBase("TemplatesKB");
		ksession.getAgenda().getAgendaGroup("MAIN").setFocus();
		for (KiePackage pack : kbase.getKiePackages()) {
			logger.info(pack.getName());
			for (Rule rule : pack.getRules()) {
				logger.info(rule.getName());
				logger.info(rule.toString());
			}
		}
		/*
		 * FactType CheeseType = kbase.getFactType("rules", "Cheese"); Object
		 * cheese = CheeseType.newInstance();
		 * CheeseType.set(cheese,"type","stillon"); FactType PersonType =
		 * kbase.getFactType("rules", "Person"); Object person =
		 * PersonType.newInstance(); CheeseType.set(person,"age",43);
		 */
		Cheese cheese = new Cheese();
		cheese.setType("stillon");
		Person person = new Person();
		person.setAge(42);
		ksession.insert(cheese);
		ksession.insert(person);
		ksession.fireAllRules();
		ksession.dispose();
		assertThat(agendaEventListener.getRulesFired(), hasItems("hello_1","hello_2"));


	}

	@Test
	public void testSimpleTemplate() throws Exception {
		TemplateContainer tc = new TemplateContainer() {
			private Column[] columns = new Column[] { new LongColumn("column1"), new LongColumn("column2"),
					new StringColumn("column3") };

			public Column[] getColumns() {
				return columns;
			}

			public String getHeader() {
				return null;
			}

			public Map<String, RuleTemplate> getTemplates() {
				Map<String, RuleTemplate> templates = new HashMap<String, RuleTemplate>();
				RuleTemplate ruleTemplate = new RuleTemplate("template1", this);
				ruleTemplate.addColumn("column1 == 10");
				ruleTemplate.addColumn("column2 < 5 || > 20");
				ruleTemplate.addColumn("column3 == \"xyz\"");
				templates.put("template1", ruleTemplate);
				return templates;
			}

			public Column getColumn(String name) {
				return columns[Integer.parseInt(name.substring(6)) - 1];
			}

		};

		DefaultTemplateRuleBase ruleBase = new DefaultTemplateRuleBase(tc);
		InternalKnowledgePackage[] packages = ((KnowledgeBaseImpl) ruleBase.newStatefulSession().getKieBase())
				.getPackages();
		assertEquals(1, packages.length);
		Map<String, String> globals = packages[0].getGlobals();
		assertEquals(DefaultGenerator.class.getName(), globals.get("generator"));
		Collection<org.kie.api.definition.rule.Rule> rules = packages[0].getRules();
		assertEquals(1, rules.size());
		assertEquals("template1", rules.iterator().next().getName());
		GroupElement lhs = ((RuleImpl) rules.iterator().next()).getLhs();

		assertEquals(7, lhs.getChildren().size());
		org.drools.core.rule.Pattern pattern = (org.drools.core.rule.Pattern) lhs.getChildren().get(1);
		assertEquals(1, pattern.getConstraints().size());
		Constraint constraint = pattern.getConstraints().get(0);
		GroupElement exists = (GroupElement) lhs.getChildren().get(2);
		pattern = (org.drools.core.rule.Pattern) exists.getChildren().get(0);
		assertEquals(3, pattern.getConstraints().size());
		IndexableConstraint vconstraint = (IndexableConstraint) pattern.getConstraints().get(1);
		assertEquals(Column.class, vconstraint.getFieldIndex().getExtractor().getExtractToClass());
		assertEquals("column1", vconstraint.getRequiredDeclarations()[0].getIdentifier());
		pattern = (org.drools.core.rule.Pattern) lhs.getChildren().get(3);
		assertEquals(1, pattern.getConstraints().size());
		constraint = pattern.getConstraints().get(0);
		exists = (GroupElement) lhs.getChildren().get(4);
		pattern = (org.drools.core.rule.Pattern) exists.getChildren().get(0);
		assertEquals(3, pattern.getConstraints().size());
		vconstraint = (IndexableConstraint) pattern.getConstraints().get(1);
		assertEquals(Column.class, vconstraint.getFieldIndex().getExtractor().getExtractToClass());
		assertEquals("column2", vconstraint.getRequiredDeclarations()[0].getIdentifier());
		pattern = (org.drools.core.rule.Pattern) lhs.getChildren().get(5);
		assertEquals(1, pattern.getConstraints().size());
		constraint = pattern.getConstraints().get(0);
		exists = (GroupElement) lhs.getChildren().get(6);
		pattern = (org.drools.core.rule.Pattern) exists.getChildren().get(0);
		assertEquals(3, pattern.getConstraints().size());
		vconstraint = (IndexableConstraint) pattern.getConstraints().get(1);
		assertEquals(Column.class, vconstraint.getFieldIndex().getExtractor().getExtractToClass());
		assertEquals("column3", vconstraint.getRequiredDeclarations()[0].getIdentifier());
	}

	private class TestDataProvider implements DataProvider {
		private Iterator<String[]> iterator;

		TestDataProvider(List<String[]> rows) {
			this.iterator = rows.iterator();
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public String[] next() {
			return iterator.next();
		}
	}

	private ArrayList<String[]> rows = new ArrayList<String[]>();

	public void setUp() {
		rows.add(new String[] { "1", "STANDARD", "FLAT", null, "SBLC", "ISS", "Commission", "Party 1", "USD", null,
				"750", "dummy" });
		rows.add(new String[] { "15", "STANDARD", "FLAT", "Entity Branch 1", "SBLC", "ISS", "Commission", null, "YEN",
				null, "1600", "dummy" });
		rows.add(new String[] { "12", "STANDARD", "FLAT", null, "SBLC", "ISS", "Postage", null, "YEN", null, "40",
				"dummy" });
		rows.add(new String[] { "62", "STANDARD", "FLAT", null, "SBLC", "ISS", "Telex", null, "YEN", "< 30000", "45",
				"dummy" });
	}

	@Test
	public void testGenerateDrlFromTemplateAndListData() throws Exception {
		String expected = "package org.kie.decisiontable;"+"\n"+
		"//generated from Decision Table"+"\n"+
		"global FeeResult result;"+"\n"+
		"\n"+
		"rule \"Fee Schedule_3\""+"\n"+
			    "    agenda-group \"STANDARD\""+"\n"+
			    "    when"+"\n"+
			        "        FeeEvent(productType == \"SBLC\","+"\n"+
			            "            activityType == \"ISS\","+"\n"+
			            "            feeType == \"Telex\","+"\n"+
			            "\n"+
			            "\n"+
			            "            amount < 30000,"+"\n"+
			            "            ccy == \"YEN\""+"\n"+
			        "        )"+"\n"+
			    "    then"+"\n"+
			        "        result.setSchedule(new FeeSchedule(\"62\", \"STANDARD\", 45));"+"\n"+
			"end"+"\n"+
			"\n"+
			"rule \"Fee Schedule_2\""+"\n"+
			    "    agenda-group \"STANDARD\""+"\n"+
			    "    when"+"\n"+
			        "        FeeEvent(productType == \"SBLC\","+"\n"+
			            "            activityType == \"ISS\","+"\n"+
			            "            feeType == \"Postage\","+"\n"+
			            "\n"+
			            "\n"+
			            "\n"+
			            "            ccy == \"YEN\""+"\n"+
			        "        )"+"\n"+
			    "    then"+"\n"+
			        "        result.setSchedule(new FeeSchedule(\"12\", \"STANDARD\", 40));"+"\n"+
			"end"+"\n"+
			"\n"+
			"rule \"Fee Schedule_1\""+"\n"+
			    "    agenda-group \"STANDARD\""+"\n"+
			    "    when"+"\n"+
			        "        FeeEvent(productType == \"SBLC\","+"\n"+
			            "            activityType == \"ISS\","+"\n"+
			            "            feeType == \"Commission\","+"\n"+
			            "\n"+
			            "            entityBranch == \"Entity Branch 1\","+"\n"+
			            "\n"+
			            "            ccy == \"YEN\""+"\n"+
			        "        )"+"\n"+
			    "    then"+"\n"+
			        "        result.setSchedule(new FeeSchedule(\"15\", \"STANDARD\", 1600));"+"\n"+
			"end"+"\n"+
			"\n"+
			"rule \"Fee Schedule_0\""+"\n"+
			    "    agenda-group \"STANDARD\""+"\n"+
			    "    when"+"\n"+
			        "        FeeEvent(productType == \"SBLC\","+"\n"+
			            "            activityType == \"ISS\","+"\n"+
			            "            feeType == \"Commission\","+"\n"+
			            "            txParty == \"Party 1\","+"\n"+
			            "\n"+
			            "\n"+
			            "            ccy == \"USD\""+"\n"+
			        "        )"+"\n"+
			    "    then"+"\n"+
			        "        result.setSchedule(new FeeSchedule(\"1\", \"STANDARD\", 750));"+"\n"+
			"end"+"\n"+"\n"+"\n";

		setUp();
		TestDataProvider tdp = new TestDataProvider(rows);
		final DataProviderCompiler converter = new DataProviderCompiler();
		final String drl = converter.compile(tdp, "/com/redhat/templates/rule_template_1.drt");
		assertEquals(expected, drl);
		logger.info("####"+drl+"###");
	}
}
