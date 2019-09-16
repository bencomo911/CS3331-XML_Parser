/*CS3331 - Advanced Object-oriented programming - Dr.Roach - M,W 3:00pm - 4:20pm
 * This program serves as an XML Parser which allows the user to navigate and 
 * edit object type tag values. In addition to this, it allows a user to 
 * create and save their updated objects as a new XML file. 
 * Last updated: Sep 9th, 2019 by Dafne Bencomo - 80592788
 */


import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class DOM {
	private String object_type;
	private static String [][] parameter_name;
	
	public DOM(String root) {
		object_type = root;
	}
	
	/****** Setters and Getters**************************************************/
	
	private String getObject_type() {
		return object_type;
	}
	
	public static String [][] getParameter_name() {
		return parameter_name;
	}
	
	public static void setParameter_name(String [][] parameter_name) {
		DOM.parameter_name = parameter_name;
	}
	
	/***************************************************************************/
	
	
	/****** Methods*************************************************************/
	
	public static void Menu() {
		System.out.println("\nCOMMAND MENU");
		System.out.println("**************");
		System.out.println("SHOW print the contents of the current element\n" + 
				"\nCHANGE param value change the <param> of the current element \nto value WRITE filename write the DOM object to an XML \ntext file\n" + 
				"\nNEXT output the contents of the next element\n" + 
				"\nPREVIOUS output the contents of the previous element\n" + 
				"\nEXIT quit the program");
		System.out.println("*************\n\n");
	}
	
	public static int totalTagNumber(NodeList objectTypes) {
		
		int valid_Node_Counter=0;
		
		for(int j=0; j<objectTypes.getLength(); j++) {
			Node innerNode = objectTypes.item(j);
			
			
			if(innerNode.getNodeType() == Node.ELEMENT_NODE) {
				valid_Node_Counter++;
			}
		}
		
		return valid_Node_Counter;
	}
	
	
	
	/*
	 * Method show prints current element information
	 */
	public static void show(DOM element) {
		System.out.println("CURRENT ELEMENT: " + element.getObject_type());
		System.out.println("--------------------------");
		for(int row = 0; row < DOM.getParameter_name().length; row++) {
			for(int column=0; column < DOM.getParameter_name()[0].length; column++) {
				System.out.print(DOM.getParameter_name()[row][column] + " "); 
			}
		}
		System.out.println("\n---------------------------------------");
	}
	
	
	/*
	 * Method change changes value of current element's tag to newValue as 
	 * indicated by the user's input
	 */
	public static void change(DOM element, String tag, String newValue) {
		for(int row = 0; row < DOM.parameter_name.length; row++) {
			if(tag.equals(DOM.parameter_name[row][0])) {
				DOM.parameter_name[row][1] = newValue;
				break;
			}
		}
	}

	/***************************************************************************/
	
	public static void main(String[] args) {

		try {
			File inputFile = new File("input.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Element root = doc.getDocumentElement();
			
			NodeList nList = root.getChildNodes();//Creates NodeList of object types in file
			
			DOM newObject = null;
			
			char userInput = 'X';
		
			OUTER_LOOP://For-Loop Label to facilitate user commands
			for(int i=1; i<nList.getLength() && i>0;) {//Iterates through objects in root to create object_type
				
				Node nNode = nList.item(i);
				
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
				
					newObject = new DOM(nNode.getNodeName()); //DOM object declaration
					
					
					
					NodeList innerNodeList = nNode.getChildNodes();
					int rowNumber = totalTagNumber(innerNodeList);
					setParameter_name(new  String[rowNumber][2]); //Initializes DOM object parameter_name
					
					int rowCounter=0;//Counter to populate rows 
					
					for(int j=0; j<innerNodeList.getLength(); j++) {//Iterates through object_type to create parameter_names
						Node innerNode = innerNodeList.item(j);
						
						
						
						if(innerNode.getNodeType() == Node.ELEMENT_NODE) {
						
							
							String tag = innerNode.getNodeName();
							String value = innerNode.getTextContent();
							
						
							
							getParameter_name()[rowCounter][0] = tag;//Populates first column with valid tags
							getParameter_name()[rowCounter][1] = value;//Populates second column with tag value respectively
							rowCounter++;
							
							
						}//Close if
					
					}//Close for
					
				}//Close if
				
				/**User Interface************************************************************/
				
				
				show(newObject);
				System.out.println("Show Command Menu(M)");
				
				Scanner s = new Scanner(System.in);
				String[] input = s.nextLine().split("\\s+");//Saves user input in an array to allow multiple word inputs
					userInput = Character.toUpperCase(input[0].charAt(0));
					
				
				switch(userInput) {	
				case 'M':
					Menu();
					userInput = 'X';
					continue OUTER_LOOP;
				case 'N':
					userInput = 'X';
					i+=2;
					continue OUTER_LOOP;
				case 'P':
					userInput = 'X';
					i-=2;
					continue OUTER_LOOP;
				case 'C':
					change(newObject, input[1], input[2]);
					show(newObject);
					userInput='X';
				case 'E':
					s.close();
					System.exit(0); 
				case 'W':
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
			        Transformer transformer = transformerFactory.newTransformer();
			        DOMSource source = new DOMSource(doc);
			        StreamResult result = new StreamResult(new File("newTest.xml"));
			        transformer.transform(source, result);
			        System.out.println("New XML File Created.");
			        break OUTER_LOOP;
				case 'X':
					Scanner newScanner = new Scanner(System.in);
					input = newScanner.nextLine().split("\\s+");//Saves user input in an array to allow multiple word inputs
					if(input.length < 2) {
						userInput = input[0].charAt(0);
					}
				default:
					System.out.println("Invalid entry, try again.");
					break OUTER_LOOP;
				}
				
				/***************************************************************************/
				
				
			}//Close while/for
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//Close main


}//Close class