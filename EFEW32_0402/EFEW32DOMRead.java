import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EFEW32DOMRead {

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {

        File xmlFile = new File("XMLEFEW32.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("etterem");

        for (int i = 0; i < nList.getLength(); i++) {

            Node nNode = nList.item(i);
            System.out.println("\nCurrent Element: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element elem = (Element) nNode;

                String id = elem.getAttribute("ekod");
                String name = elem.getElementsByTagName("nev").item(0).getTextContent();
                
                Element cimElem = (Element) elem.getElementsByTagName("cim").item(0);
                String city = cimElem.getElementsByTagName("varos").item(0).getTextContent();
                String street = cimElem.getElementsByTagName("utca").item(0).getTextContent();
                String number = cimElem.getElementsByTagName("hazszam").item(0).getTextContent();

                String stars = elem.getElementsByTagName("csillag").item(0).getTextContent();

                String adr = city + " , " + street + " utca " + number;

                System.out.println("Étterem ID:" + id);
                System.out.println("Név:" + name);
                System.out.println("Cím:" + adr);
                System.out.println("Csillag:" + stars);

                NodeList fList = elem.getElementsByTagName("foszakacs");
                for (int k = 0; k < fList.getLength(); k++) {
                    Element fElem = (Element) fList.item(k);
                    String fid = fElem.getAttribute("fkod");
                    String fname = fElem.getElementsByTagName("nev").item(0).getTextContent();
                    String age = fElem.getElementsByTagName("eletkor").item(0).getTextContent();
                    
                    String edu = "";
                    NodeList eduList = fElem.getElementsByTagName("vegzettseg");
                    for (int j = 0; j < eduList.getLength(); j++) {
                        edu += eduList.item(j).getTextContent() + (j == eduList.getLength() - 1 ? "" : " ; ");
                    }
                    
                    System.out.println("\n  Főszakács [" + fid + "]: " + fname + " (Kor: " + age + ")");
                    System.out.println("  Végzettségek: " + edu);
                }

                NodeList szList = elem.getElementsByTagName("szakacs");
                for (int k = 0; k < szList.getLength(); k++) {
                    Element szElem = (Element) szList.item(k);
                    String szid = szElem.getAttribute("szkod");
                    String szname = szElem.getElementsByTagName("nev").item(0).getTextContent();
                    String dep = szElem.getElementsByTagName("reszleg").item(0).getTextContent();
                    System.out.println("  Szakács [" + szid + "]: " + szname + " (Részleg: " + dep + ")");
                }

                NodeList gyList = elem.getElementsByTagName("gyakornok");
                for (int k = 0; k < gyList.getLength(); k++) {
                    Element gyElem = (Element) gyList.item(k);
                    String gyid = gyElem.getAttribute("gykod");
                    String gyname = gyElem.getElementsByTagName("nev").item(0).getTextContent();
                    System.out.println("  Gyakornok [" + gyid + "]: " + gyname);
                }
            }
        }

        NodeList vList = doc.getElementsByTagName("vendeg");
        for (int i = 0; i < vList.getLength(); i++) {
            Element vElem = (Element) vList.item(i);
            String vName = vElem.getElementsByTagName("nev").item(0).getTextContent();
            String vKod = vElem.getAttribute("vkod");
            System.out.println("\nVendég: " + vName + " (ID: " + vKod + ")");
        }

        NodeList rList = doc.getElementsByTagName("rendeles");
        for (int i = 0; i < rList.getLength(); i++) {
            Element rElem = (Element) rList.item(i);
            String etel = rElem.getElementsByTagName("etel").item(0).getTextContent();
            String osszeg = rElem.getElementsByTagName("osszeg").item(0).getTextContent();
            System.out.println("\nRendelés: " + etel + " - " + osszeg + " Ft");
        }
    }
}