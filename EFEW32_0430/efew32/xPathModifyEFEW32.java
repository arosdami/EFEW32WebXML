package efew32;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class xPathModifyEFEW32 {
    
    public static void main(String[] args) {
        try {
            // 1. XML dokumentum beolvasása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("studentEFEW32.xml"));
            
            // XPath objektum létrehozása
            XPath xpath = XPathFactory.newInstance().newXPath();
            
            // 2. Az id="01" hallgató megkeresése
            Node hallgatoNode = (Node) xpath.evaluate("//hallgato[@id='01']", document, XPathConstants.NODE);
            
            if (hallgatoNode != null) {
                System.out.println("Módosítás előtt - id='01' hallgató:");
                kiirHallgato(hallgatoNode, xpath);
                
                // 3. Adatok módosítása
                Element hallgatoElement = (Element) hallgatoNode;
                
                // Keresztnév módosítása
                Node keresztnevNode = (Node) xpath.evaluate("keresztnev", hallgatoElement, XPathConstants.NODE);
                if (keresztnevNode != null) {
                    keresztnevNode.setTextContent("Módosított");
                }
                
                // Vezetéknév módosítása
                Node vezeteknevNode = (Node) xpath.evaluate("vezeteknev", hallgatoElement, XPathConstants.NODE);
                if (vezeteknevNode != null) {
                    vezeteknevNode.setTextContent("Hallgató");
                }
                
                // Becenév módosítása
                Node becenevNode = (Node) xpath.evaluate("becenev", hallgatoElement, XPathConstants.NODE);
                if (becenevNode != null) {
                    becenevNode.setTextContent("Módi");
                }
                
                // Kor módosítása
                Node korNode = (Node) xpath.evaluate("kor", hallgatoElement, XPathConstants.NODE);
                if (korNode != null) {
                    korNode.setTextContent("25");
                }
                
                System.out.println("\nMódosítás után - id='01' hallgató:");
                kiirHallgato(hallgatoNode, xpath);
                
                // 4. A módosított dokumentum mentése (opcionális)
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(new File("studentEFEW32_modositott.xml"));
                transformer.transform(source, result);
                
                System.out.println("\nA módosított XML kiírva a studentEFEW32_modositott.xml fájlba.");
                
            } else {
                System.out.println("Nem található hallgató id='01' azonosítóval.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void kiirHallgato(Node hallgatoNode, XPath xpath) {
        try {
            String id = xpath.evaluate("@id", hallgatoNode);
            String keresztnev = xpath.evaluate("keresztnev", hallgatoNode);
            String vezeteknev = xpath.evaluate("vezeteknev", hallgatoNode);
            String becenev = xpath.evaluate("becenev", hallgatoNode);
            String kor = xpath.evaluate("kor", hallgatoNode);
            
            System.out.println("  ID: " + id);
            System.out.println("  Keresztnév: " + keresztnev);
            System.out.println("  Vezetéknév: " + vezeteknev);
            System.out.println("  Becenév: " + becenev);
            System.out.println("  Kor: " + kor);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
