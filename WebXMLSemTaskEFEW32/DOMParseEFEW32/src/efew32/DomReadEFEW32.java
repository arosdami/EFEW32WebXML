package efew32;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import java.io.File;
import java.io.FileOutputStream;

public class DomReadEFEW32 {
    
    private Document document;
    private String inputFilePath;
    private String outputFilePath;
    
    public DomReadEFEW32(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }
    
    public void loadXmlDocument() throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(inputFilePath));
            
            System.out.println("XML dokumentum sikeresen beolvasva: " + inputFilePath);
            System.out.println("Gyoker elem: " + document.getDocumentElement().getNodeName());
            
        } catch (Exception e) {
            System.err.println("Hiba az XML beolvasasa soran: " + e.getMessage());
            throw e;
        }
    }
    
    public void printDocumentToConsole() {
        System.out.println("\n=== XML Dokumentum Blokkokban ===\n");
        printNodeInBlocks(document.getDocumentElement(), 0);
    }
    
    private void printNodeInBlocks(Element element, int level) {
        String indent = "  ".repeat(level);
        
        System.out.println(indent + "--- " + element.getTagName() + " ---");
        
        if (element.getAttributes().getLength() > 0) {
            for (int i = 0; i < element.getAttributes().getLength(); i++) {
                Node attr = element.getAttributes().item(i);
                System.out.println(indent + "  " + attr.getNodeName() + ": " + attr.getNodeValue());
            }
        }
        
        String textContent = element.getTextContent().trim();
        if (!textContent.isEmpty() && element.getChildNodes().getLength() == 1) {
            System.out.println(indent + "  " + textContent);
        }
        
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                printNodeInBlocks((Element) child, level + 1);
            }
        }
        
        System.out.println(indent + "--- /" + element.getTagName() + " ---");
        System.out.println();
    }
    
    public void saveXmlDocument() throws Exception {
        if (document == null) {
            System.err.println("Nincs beolvasott dokumentum!");
            return;
        }
        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(outputFilePath));
            
            transformer.transform(source, result);
            
            System.out.println("XML dokumentum sikeresen mentve: " + outputFilePath);
            
        } catch (Exception e) {
            System.err.println("Hiba mentesnel: " + e.getMessage());
            throw e;
        }
    }
    
    public static void main(String[] args) {
        try {
            String inputFile = "/home/schoolbox/Asztal/WebXMLSemTaskEFEW32/XMLEFEW32.xml";
            String outputFile = "/home/schoolbox/Asztal/WebXMLSemTaskEFEW32/XMLEFEW321.xml";
            
            DomReadEFEW32 parser = new DomReadEFEW32(inputFile, outputFile);
            
            parser.loadXmlDocument();
            parser.printDocumentToConsole();
            parser.saveXmlDocument();
            
            System.out.println("\nProgram sikeresen lefutott!");
            
        } catch (Exception e) {
            System.err.println("Program hiba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
