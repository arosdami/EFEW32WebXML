package EFEW32;

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
import org.w3c.dom.NodeList;
import java.io.File;

public class xPathEFEW32 {

    public static void main(String[] args) {
        try {
            String EFEW32 = "EFEW32";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("studentEFEW32.xml"));

            XPath xpath = XPathFactory.newInstance().newXPath();

            NodeList hallgatok = (NodeList) xpath.evaluate("//hallgato", document, XPathConstants.NODESET);

            for (int i = 0; i < hallgatok.getLength(); i++) {
                String id = xpath.evaluate("@id", hallgatok.item(i));
                String keresztnev = xpath.evaluate("keresztnev", hallgatok.item(i));
                String vezeteknev = xpath.evaluate("vezeteknev", hallgatok.item(i));
                String becenev = xpath.evaluate("becenev", hallgatok.item(i));
                String kor = xpath.evaluate("kor", hallgatok.item(i));

                System.out.println("Hallgató ID: " + id);
                System.out.println("  Keresztnév: " + keresztnev);
                System.out.println("  Vezetéknév: " + vezeteknev);
                System.out.println("  Becenév: " + becenev);
                System.out.println("  Kor: " + kor);
            }

            String neptunkod_1 = "/class/hallgato";
            String neptunkod_2 = "/class/hallgato[kor>21]";
            String neptunkod_3 = "/class/hallgato[@id='01']";
            String neptunkod_4 = "/class/hallgato/becenev";
            String neptunkod_5 = "//hallgato[contains(becenev, 'i')]";
            String neptunkod_6 = "/class/hallgato[position()=2]";
            String neptunkod_7 = "/class/hallgato[kor>=20 and kor<=22]";
            String neptunkod_8 = "count(/class/hallgato)";

            NodeList osszesHallgato = (NodeList) xpath.evaluate(neptunkod_1, document, XPathConstants.NODESET);
            System.out.println("1. Összes hallgató:");
            for (int i = 0; i < osszesHallgato.getLength(); i++) {
                String id = xpath.evaluate("@id", osszesHallgato.item(i));
                String nev = xpath.evaluate("vezeteknev", osszesHallgato.item(i)) + " " +
                        xpath.evaluate("keresztnev", osszesHallgato.item(i));
                System.out.println("   ID: " + id + ", Név: " + nev);
            }

            NodeList idosebbHallgatok = (NodeList) xpath.evaluate(neptunkod_2, document, XPathConstants.NODESET);
            System.out.println("\n2. 21 évnél idősebb hallgatók:");
            for (int i = 0; i < idosebbHallgatok.getLength(); i++) {
                String id = xpath.evaluate("@id", idosebbHallgatok.item(i));
                String nev = xpath.evaluate("vezeteknev", idosebbHallgatok.item(i)) + " " +
                        xpath.evaluate("keresztnev", idosebbHallgatok.item(i));
                String kor = xpath.evaluate("kor", idosebbHallgatok.item(i));
                System.out.println("   ID: " + id + ", Név: " + nev + ", Kor: " + kor);
            }

            NodeList hallgato01 = (NodeList) xpath.evaluate(neptunkod_3, document, XPathConstants.NODESET);
            System.out.println("\n3. Az '01' azonosítójú hallgató:");
            for (int i = 0; i < hallgato01.getLength(); i++) {
                String id = xpath.evaluate("@id", hallgato01.item(i));
                String keresztnev = xpath.evaluate("keresztnev", hallgato01.item(i));
                String vezeteknev = xpath.evaluate("vezeteknev", hallgato01.item(i));
                String becenev = xpath.evaluate("becenev", hallgato01.item(i));
                String kor = xpath.evaluate("kor", hallgato01.item(i));
                System.out.println("   ID: " + id + ", Teljes név: " + vezeteknev + " " + keresztnev + ", Becenév: " + becenev + ", Kor: " + kor);
            }

            NodeList becenevek = (NodeList) xpath.evaluate(neptunkod_4, document, XPathConstants.NODESET);
            System.out.println("\n4. Összes becenév:");
            for (int i = 0; i < becenevek.getLength(); i++) {
                System.out.println("   " + becenevek.item(i).getTextContent());
            }

            NodeList iBecenevek = (NodeList) xpath.evaluate(neptunkod_5, document, XPathConstants.NODESET);
            System.out.println("\n5. 'i' betűt tartalmazó becenevű hallgatók:");
            for (int i = 0; i < iBecenevek.getLength(); i++) {
                String id = xpath.evaluate("@id", iBecenevek.item(i));
                String becenev = xpath.evaluate("becenev", iBecenevek.item(i));
                System.out.println("   ID: " + id + ", Becenév: " + becenev);
            }

            NodeList masodikHallgato = (NodeList) xpath.evaluate(neptunkod_6, document, XPathConstants.NODESET);
            System.out.println("\n6. Második hallgató:");
            for (int i = 0; i < masodikHallgato.getLength(); i++) {
                String id = xpath.evaluate("@id", masodikHallgato.item(i));
                String nev = xpath.evaluate("vezeteknev", masodikHallgato.item(i)) + " " +
                        xpath.evaluate("keresztnev", masodikHallgato.item(i));
                System.out.println("   ID: " + id + ", Név: " + nev);
            }

            NodeList korKozott = (NodeList) xpath.evaluate(neptunkod_7, document, XPathConstants.NODESET);
            System.out.println("\n7. 20-22 év közötti hallgatók:");
            for (int i = 0; i < korKozott.getLength(); i++) {
                String id = xpath.evaluate("@id", korKozott.item(i));
                String nev = xpath.evaluate("vezeteknev", korKozott.item(i)) + " " +
                        xpath.evaluate("keresztnev", korKozott.item(i));
                String kor = xpath.evaluate("kor", korKozott.item(i));
                System.out.println("   ID: " + id + ", Név: " + nev + ", Kor: " + kor);
            }

            String hallgatokSzama = xpath.evaluate(neptunkod_8, document);
            System.out.println("\n8. Hallgatók száma: " + hallgatokSzama);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("studentEYZWG91.xml"));
            transformer.transform(source, result);

            System.out.println("\nAz eredmény kiírva a studentEYZWG91.xml fájlba.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}