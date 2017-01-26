package com.redhat.droolsfeature.rules;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.xml.XmlDumper;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.internal.builder.conf.LanguageLevelOption;

public class DecisionTableTools {
	private static Logger logger = Logger.getLogger(DecisionTableTools.class);

	public  void generateXLSDTFromDRL() throws IOException, ClassNotFoundException, DroolsParserException {
		// create an input stream
		InputStream is = null;
		try {
			is = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/com/amadeus/dtables/test2.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// create compiler class instance
		SpreadsheetCompiler sc = new SpreadsheetCompiler();
		// compile the excel to generate the (.drl)
		String drl = sc.compile(is, InputType.XLS);		
		// check the generated (.drl) 
		logger.info("Generate DRL file is –: ");
		logger.info(drl);
		String r = drl.substring(drl.indexOf("rule \""), drl.indexOf("end")+3);
		logger.info(r);
		 final DrlParser parser = new DrlParser(LanguageLevelOption.DRL6);
		 PackageDescr pkg = (PackageDescr) parser.parse(new StringReader(r) );
		 RuleDescr rule = (RuleDescr) pkg.getRules().get( 0 );
		logger.info(rule.getConsequence());

	}
	
	public  void generateXMLFromDRL() throws IOException, ClassNotFoundException, DroolsParserException {
		// create an input stream
		InputStream is = null;
		try {
			is = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/com/amadeus/dtables/test2.xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// create compiler class instance
		SpreadsheetCompiler sc = new SpreadsheetCompiler();
		// compile the excel to generate the (.drl)
		String drl = sc.compile(is, InputType.XLS);	
		// check the generated (.drl) 
		logger.info("Generate DRL file is –: ");
		logger.info(drl);
		String r = drl.substring(drl.indexOf("rule \""), drl.indexOf("end")+3);
		logger.info(r);
		 final DrlParser parser = new DrlParser(LanguageLevelOption.DRL6);
		 PackageDescr pkg = (PackageDescr) parser.parse(new StringReader(r) );
		 XmlDumper xmldump = new XmlDumper();
		 String xmlrule = xmldump.dump(pkg);
		 logger.info(xmlrule);

	}
}
