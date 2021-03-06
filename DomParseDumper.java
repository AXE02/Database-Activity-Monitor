

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomParseDumper {

	//No generics
	List myEmpls;
	Document dom;

	public DomParseDumper(){
		//create a list to hold the employee objects
		myEmpls = new ArrayList();
	}

	public void runExample() {
		
		//parse the xml file and get the dom object
		parseXmlFile();
		
		//get each employee element and create a Employee object
		parseDocument();
		
		//Iterate through the list and print the data
		printData();
		
	}
	
	
	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			dom = db.parse("Audit.xml");
			

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	protected void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		
		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("AUDIT_RECORD");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				
				//get the employee element
				Element el = (Element)nl.item(i);
				
				//get the Employee object
				Dumper a = getDumper(el);
				//add it to list
				myEmpls.add(a);
			}
		}
	}


	/**
	 * I take an employee element and read the values in, create
	 * an Employee object and return it
	 * @param empEl
	 * @return
	 */
	protected Dumper getDumper(Element empEl) {
		
		//for each <employee> element get text or int values of 
		//name ,id, age and name
		String TIMESTAMPA = getTextValue(empEl,"TIMESTAMP");
		String NAME = getTextValue(empEl,"NAME");
		int CONNECTION_ID = getIntValue(empEl,"CONNECTION_ID");
		String USER = getTextValue(empEl,"USER");
		String HOST = getTextValue(empEl,"HOST");
		String IP = getTextValue(empEl,"IP");
		String COMMAND_CLASS = getTextValue(empEl,"COMMAND_CLASS");
		String SQLTEXT = getTextValue(empEl,"SQLTEXT");

		//Create a new Employee with the value read from the xml nodes
		Dumper a = new Dumper(TIMESTAMPA,NAME,CONNECTION_ID,USER,HOST,IP,COMMAND_CLASS,SQLTEXT);
		return a;
	}


	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	
	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
	
	/**
	 * Iterate through the list and print the 
	 * content to console
	 */
	private void printData(){
		
		System.out.println("No of Employees '" + myEmpls.size() + "'.");
		char choice;
		Iterator it = myEmpls.iterator();
		while(it.hasNext()) 
		{
		System.out.println(it.next().toString());
		}
		
	}

	
	public static void main(String[] args){
		//create an instance
		DomParseDumper dpe = new DomParseDumper();
		
		//call run example
		dpe.runExample();
	}

}
