/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Rafal
 */
public class SettingsIO {
    Document document;
    public SettingsIO(String filePath) {
        DocumentBuilderFactory builderFactory =
        DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  
        }
        try {
             document = builder.parse(filePath);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String Get(String node)
    {
       try {
            	XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
            //read a string value
                String expression = "/settings/"+node+"/text()";
            Node myNode = (Node) xpath.compile(expression).evaluate(document, XPathConstants.NODE);
            System.out.println(myNode.getNodeValue());
            return myNode.getNodeValue();
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SettingsIO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }
    public boolean Save(String node, String value) throws ParserConfigurationException{
        
        String filePath = "assets/Settings/ProgramSettings.xml";  
        File file = new File(filePath);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = null;
        try {
            doc = docBuilder.parse(file);
        } catch (SAXException ex) {
            Logger.getLogger(SettingsIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SettingsIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Change the content of node
        Node nodes = doc.getElementsByTagName(node).item(0);
        // I changed the below line form nodes.setNodeValue to nodes.setTextContent
        System.out.println(nodes.getNodeValue());
        nodes.setTextContent(value);

        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(SettingsIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        // initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(file);
        DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(SettingsIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        return true;
    }
    
}
