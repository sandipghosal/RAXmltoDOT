import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RAToDot {
	
	//private static ArrayList<Method> methods = new ArrayList<Method>();
	private static HashMap<String, Method> methods = new HashMap<String, Method>();
	private static HashMap<String, Location> locations = new HashMap<String, Location>();
	private static Set<Transitions> transitions = new LinkedHashSet<Transitions>();
	//private static Set<Location> locations = new LinkedHashSet<Location>();
	
	public void addParameters(Element e, Method m) {
		NodeList params = e.getElementsByTagName("param");
		Parameter paramObj;
		if(params.getLength()>0) {
			for(int j=0; j<params.getLength(); j++) {
				Element param = (Element)params.item(j);
				String paramName = param.getAttribute("name");
				String paramType = param.getAttribute("type");
				paramObj = new Parameter(paramName, paramType);
				m.addParameter(paramObj);
			}
		}
	}
	
	public void getMethods(Document doc){
		NodeList inputs = doc.getElementsByTagName("inputs");
		NodeList outputs = doc.getElementsByTagName("outputs");
		NodeList inputSymbols = ((Element)inputs.item(0)).getElementsByTagName("symbol");
		NodeList outputSymbols = ((Element)outputs.item(0)).getElementsByTagName("symbol");
		
		for(int i=0; i<inputSymbols.getLength(); i++) {
			Element method = (Element)inputSymbols.item(i);
			String methodName = method.getAttribute("name");
			Method methodObj = new Method(methodName, false);
			addParameters(method, methodObj);
			methods.put(methodName, methodObj);
			//System.out.println(methodObj);
		}
		
		for(int i=0; i<outputSymbols.getLength(); i++) {
			Element method = (Element)outputSymbols.item(i);
			String methodName = method.getAttribute("name");
			Method methodObj = new Method(methodName, true);
			addParameters(method, methodObj);
			methods.put(methodName, methodObj);
			//System.out.println(methodObj);
		}
			
	}
	
	
	public void getLocations(Document doc) {
		NodeList locationsList = doc.getElementsByTagName("location");
		for(int i=0; i< locationsList.getLength(); i++) {
			Element location = (Element)locationsList.item(i);
			String name = location.getAttribute("name");
			Location locObj = new Location(name);
			//System.out.println(locObj);
			locations.put(name, locObj);
		}
	}
	
	
	public ArrayList<Assignment> getAssignments(Element e){
		ArrayList<Assignment> assignments = new ArrayList<>();
		NodeList assigns = e.getElementsByTagName("assign");
		if (assigns == null) return null;
		for(int i=0; i<assigns.getLength(); i++) {
			String lhs = ((Element)assigns.item(i)).getAttribute("to");
			String rhs = assigns.item(i).getTextContent();
			Assignment<String, String> assignObj = new Assignment<>(lhs, rhs);
			assignments.add(assignObj);
			//System.out.println(assignObj);
		}
		return assignments;
	}
	
	
	public void getTransitions(Document doc) {
		NodeList transList = doc.getElementsByTagName("transition");
		for(int i=0; i<transList.getLength(); i++) {
			Element transition = (Element)transList.item(i);
			String fromLoc = transition.getAttribute("from");
			String toLoc = transition.getAttribute("to");
			String symbol = transition.getAttribute("symbol");
			NodeList guards = transition.getElementsByTagName("guard");
			String guard = "";
			if(guards.item(0) != null) {
				guard += guards.item(0).getTextContent();
			}
			ArrayList<Assignment> assignments = getAssignments(transition);
			
			if(methods.get(symbol).isOutputMethod)
				locations.get(fromLoc).isIntermediate = true;
			Transitions transObj = new Transitions(locations.get(fromLoc), 
													locations.get(toLoc), 
													methods.get(symbol), 
													assignments, 
													guard);
			transitions.add(transObj);
			//System.out.println(transObj);
		}
	}
	
	
	public void writeOutput(String outFile) {
		try (FileWriter fileWriter = new FileWriter(outFile)){
			fileWriter.write("digraph RA {\n");
			fileWriter.write("\"\" [shape=none,label=<>]\n");
			
			for(String k : locations.keySet()) {
				fileWriter.write(locations.get(k).toString() + "\n");
			}
			
			fileWriter.write("\"\" -> \"l0\"\n");
			
			for(Iterator<Transitions> iter = transitions.iterator(); iter.hasNext();) {
				fileWriter.write(iter.next().toString() + "\n");
			}
			
			fileWriter.write("}\n");
			System.out.println(outFile +
					" generated successfully");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Usage: java RAToDot <input_xml_file> <output_dot_file>");
            return;
		}
		
		String inputFile = args[0];
		String outputFile = args[1];
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder(); 
			Document doc = builder.parse(new File(inputFile));
			doc.getDocumentElement().normalize();
			
			RAToDot rtd = new RAToDot();
			rtd.getMethods(doc);
			rtd.getLocations(doc);
			rtd.getTransitions(doc);
			rtd.writeOutput(outputFile);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
