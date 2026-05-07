package efew32;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.FileOutputStream;

/**
 * DOM parser - XML dokumentum feldolgozása
 * Beolvassa az XML dokumentumot, kiírja blokk formában a konzolra
 * és mentése a kimenet fájlba
 * @author EFEW32
 */
public class DomReadEFEW32 {
    
    private Document document;
    private String inputFilePath;
    private String outputFilePath;
    
    /**
     * Konstruktor
     * @param inputFilePath az XML bemeneti fájl elérési útja
     * @param outputFilePath a feldolgozott XML kimeneti fájl elérési útja
     */
    public DomReadEFEW32(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }
    
    /**
     * XML dokumentum beolvasása és feldolgozása
     * @throws Exception az XML beolvasásakor felmerülő hibákat dobja
     */
    public void loadXmlDocument() throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(inputFilePath));
            
            System.out.println("XML dokumentum sikeresen beolvasva: " + inputFilePath);
            System.out.println("Gyökér elem: " + document.getDocumentElement().getNodeName());
            
        } catch (Exception e) {
            System.err.println("Hiba az XML beolvasása során: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Teljes XML dokumentum kiírása blokk formában a konzolra
     */
    public void printDocumentToConsole() {
        System.out.println("\n=== Teljes XML Dokumentum Tartalma ===\n");
        printNode(document.getDocumentElement(), "");
    }
    
    /**
     * Rekurzív node kiíró metódus
     * Fává szerkezetű kiírással jeleníti meg az XML-t
     * @param node a feldolgozandó csomópont
     * @param indent az aktuális behúzás
     */
    private void printNode(Node node, String indent) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            System.out.println(indent + "<" + element.getTagName() + ">");
            
            // Attribútumok kiírása
            if (element.getAttributes().getLength() > 0) {
                for (int i = 0; i < element.getAttributes().getLength(); i++) {
                    Node attr = element.getAttributes().item(i);
                    System.out.println(indent + "  @" + attr.getNodeName() + "=" + attr.getNodeValue());
                }
            }
            
            // Gyerek elemek rekurzív kiírása
            NodeList children = element.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.TEXT_NODE && !child.getTextContent().trim().isEmpty()) {
                    System.out.println(indent + "  " + child.getTextContent().trim());
                } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                    printNode(child, indent + "  ");
                }
            }
            
            System.out.println(indent + "</" + element.getTagName() + ">");
        }
    }
    
    /**
     * XML dokumentum mentése fájlba formázott formában
     * @throws Exception az XML mentésekor felmerülő hibákat dobja
     */
    public void saveXmlDocument() throws Exception {
        if (document == null) {
            System.err.println("Nincs beolvasott dokumentum!");
            return;
        }
        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            // Kimeneti beállítások
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(outputFilePath));
            
            transformer.transform(source, result);
            
            System.out.println("XML dokumentum sikeresen mentve: " + outputFilePath);
            
        } catch (Exception e) {
            System.err.println("Hiba mentésnél: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Fő metódus - Program belépési pont
     * 1. Beolvassa az XML dokumentumot
     * 2. Kiírja blokk formában a konzolra
     * 3. Mentése a kimenet fájlba
     * @param args parancssori argumentumok (nem használt)
     */
    public static void main(String[] args) {
        try {
            // Fájl elérési utak
            String inputFile = "XMLEFEW32.xml";
            String outputFile = "XMLEFEW321.xml";
            
            // DOM parser objektum létrehozása
            DomReadEFEW32 parser = new DomReadEFEW32(inputFile, outputFile);
            
            // XML dokumentum beolvasása
            parser.loadXmlDocument();
            
            // Teljes dokumentum kiírása blokk formában a konzolra
            parser.printDocumentToConsole();
            
            // XML dokumentum mentése fájlba
            parser.saveXmlDocument();
            
            System.out.println("\nProgram sikeresen lefutott!");
            
        } catch (Exception e) {
            System.err.println("Program hiba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
