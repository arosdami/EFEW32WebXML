package EFEW32;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.io.File;

public class xPathModifyEFEW32 {

    public static void main(String[] args) {
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("studentEFEW32.xml"));

            XPath xpath = XPathFactory.newInstance().newXPath();

            Node hallgato = (Node) xpath.evaluate("/class/hallgato[@id='01']", document, XPathConstants.NODE);

            if (hallgato != null) {

                Node keresztnev = (Node) xpath.evaluate("keresztnev", hallgato, XPathConstants.NODE);
                Node vezeteknev = (Node) xpath.evaluate("vezeteknev", hallgato, XPathConstants.NODE);
                Node becenev = (Node) xpath.evaluate("becenev", hallgato, XPathConstants.NODE);
                Node kor = (Node) xpath.evaluate("kor", hallgato, XPathConstants.NODE);

                keresztnev.setTextContent("ÚjKeresztnev");
                vezeteknev.setTextContent("ÚjVezeteknev");
                becenev.setTextContent("ÚjBecenév");
                kor.setTextContent("25");

                System.out.println("ID: " + xpath.evaluate("@id", hallgato));
                System.out.println("Keresztnév: " + keresztnev.getTextContent());
                System.out.println("Vezetéknév: " + vezeteknev.getTextContent());
                System.out.println("Becenév: " + becenev.getTextContent());
                System.out.println("Kor: " + kor.getTextContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}