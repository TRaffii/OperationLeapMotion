/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
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
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  
        }
        try {
             document = builder.parse(new FileInputStream(filePath));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean Save(String node, String value){
        try {
            XPath xPath =  XPathFactory.newInstance().newXPath();
            //read a string value
            String expression = "/Employees/Employee/firstname";

            //read an xml node using xpath
            Node myNode = (Node) xPath.compile(expression).evaluate(document, XPathConstants.NODE);
            myNode.setNodeValue("test");
            
            return true;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SettingsIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
