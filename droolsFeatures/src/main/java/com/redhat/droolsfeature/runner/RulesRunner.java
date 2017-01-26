package com.redhat.droolsfeature.runner;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;

import com.redhat.droolsfeature.listener.RulesAgendaListener;

public class RulesRunner {
	private  String path = null;
	protected  RulesAgendaListener agendaEventListener = null;
	private  Logger logger = Logger.getLogger(RulesRunner.class);
	private KieSession kSession = null;
	private KieContainer kContainer = null;
	private KieBase kBase = null;

	public void initialisation(String fileName) {
		path = System.getProperty("user.dir") + "/src/main/resources/com/redhat/droolsfeature/rules/";
		KieServices kieServices = KieServices.Factory.get();
		// Create File System services
		KieFileSystem kFileSystem = kieServices.newKieFileSystem();
		Resource resource = null;
		File file = new File(path + fileName);
		//if (fileName.substring(fileName.indexOf('.') + 1, fileName.length()).equals("drl"))
			resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
		//else
			//resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DTABLE);
		resource.setSourcePath("com.amadeus.droolsfeature.rules."+fileName);
		kFileSystem.write(resource);
		KieBuilder kbuilder = kieServices.newKieBuilder(kFileSystem);
		kbuilder.buildAll();
		if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
			logger.error(kbuilder.getResults().getMessages());
			throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
		}
		kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		kBase = kContainer.getKieBase();
	}
	
	public void setSession(){
		kSession = kContainer.newKieSession();
	}
	public KieBase getKieBase(){
		return kBase;
	}
	public KieSession getSession(){
		return kSession; 
	}
	public void addAgendaListener(){
		agendaEventListener = new RulesAgendaListener();
		kSession.addEventListener(agendaEventListener);
	}
	public void runRule(List<Object> objects) {	
		logger.info(objects.size());
		for (Object object : objects){
			kSession.insert(object);
		}
		agendaEventListener = new RulesAgendaListener();
		kSession.addEventListener(agendaEventListener);
		kSession.fireAllRules();
		kSession.dispose();
	}
	
	public List<String> run(List<Object> objects) {	
		logger.info(objects.size());
		for (Object object : objects){
			kSession.insert(object);
		}
		agendaEventListener = new RulesAgendaListener();
		kSession.addEventListener(agendaEventListener);
		kSession.fireAllRules();
		kSession.dispose();
		return agendaEventListener.getRulesFired();
	}
	
	public List<String> getRulesFired(){
		return agendaEventListener.getRulesFired();
	}

	
}
