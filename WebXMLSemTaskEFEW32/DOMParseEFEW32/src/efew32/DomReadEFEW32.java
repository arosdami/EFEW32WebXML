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
 * DOM parser a tagsági rendszerhez
 * Beolvassa az XML-t és kiírja a konzolra
 * @author EFEW32
 */
public class DomReadEFEW32 {
    
    private Document document;
    private String inputFilePath;
    private String outputFilePath;
    
    // Konstruktor
    public DomReadEFEW32(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }
    
    // XML beolvasása
    public void loadXmlDocument() throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);  // kikapcsolva, hogy ne legyen schema hiba
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(inputFilePath));
            
            System.out.println("XML dokumentum sikeresen beolvasva: " + inputFilePath);
            System.out.println("Gyökér elem: " + document.getDocumentElement().getNodeName());
            
        } catch (Exception e) {
            System.err.println("Hiba az XML beolvasása során: " + e.getMessage());
            throw e;
        }
    }
    
    // Menü indítása
    public void startQuerySystem() {
        if (document == null) {
            System.err.println("Nincs beolvasott dokumentum!");
            return;
        }
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice;
        
        do {
            System.out.println("\n--- Tagsági Rendszer ---");
            System.out.println("1. Összes felhasználó listázása");
            System.out.println("2. Aktív tagok listázása");
            System.out.println("3. Események listázása");
            System.out.println("4. Tagsági típusok listázása");
            System.out.println("5. Felhasználó keresése név szerint");
            System.out.println("6. Esemény keresése név szerint");
            System.out.println("7. Tag részletes adatainak megjelenítése");
            System.out.println("8. Statisztikák megjelenítése");
            System.out.println("9. Teljes dokumentum kiírása konzolra");
            System.out.println("0. Kilépés");
            System.out.print("Válasz: ");
            
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        listAllUsers();
                        break;
                    case 2:
                        listActiveMembers();
                        break;
                    case 3:
                        listEvents();
                        break;
                    case 4:
                        listMembershipTypes();
                        break;
                    case 5:
                        System.out.print("Adja meg a keresendő nevet: ");
                        String name = scanner.nextLine();
                        searchUserByName(name);
                        break;
                    case 6:
                        System.out.print("Adja meg a keresendő esemény nevét: ");
                        String eventName = scanner.nextLine();
                        searchEventByName(eventName);
                        break;
                    case 7:
                        System.out.print("Adja meg a tag ID-jét (pl. T001): ");
                        String tagId = scanner.nextLine();
                        showMemberDetails(tagId);
                        break;
                    case 8:
                        printStatistics();
                        break;
                    case 9:
                        printDocumentToConsole();
                        break;
                    case 0:
                        System.out.println("Viszlát!");
                        break;
                    default:
                        System.out.println("Rossz választás!");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Hibás bemenet!");
                scanner.nextLine();
                choice = -1;
            }
            
        } while (choice != 0);
        
        scanner.close();
    }
    
        
    // XML mentése
    public void saveXmlDocument() throws Exception {
        if (document == null) {
            System.err.println("Nincs beolvasott dokumentum!");
            return;
        }
        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            
            // beállítások
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(outputFilePath));
            
            transformer.transform(source, result);
            
            System.out.println("XML dokumentum sikeresen mentve: " + outputFilePath);
            
        } catch (Exception e) {
            System.out.println("Hiba mentésnél: " + e.getMessage());
            throw e;
        }
    }
    
    // Összes felhasználó
    public void listAllUsers() {
        System.out.println("\n--- Felhasználók ---");
        NodeList felhasznalok = document.getElementsByTagName("Felhasznalo");
        
        for (int i = 0; i < felhasznalok.getLength(); i++) {
            Element felhasznalo = (Element) felhasznalok.item(i);
            String id = felhasznalo.getAttribute("FelhasznaloID");
            String nev = getElementText(felhasznalo, "Felhasznalonev");
            String szerepkor = getElementText(felhasznalo, "Szerepkor");
            
            System.out.printf("ID: %s, Név: %s, Szerepkör: %s\n", id, nev, szerepkor);
        }
        System.out.println("--- vége ---\n");
    }
    
    // Aktív tagok
    public void listActiveMembers() {
        System.out.println("\n--- Aktív tagok ---");
        NodeList tagok = document.getElementsByTagName("Tag");
        
        for (int i = 0; i < tagok.getLength(); i++) {
            Element tag = (Element) tagok.item(i);
            String id = tag.getAttribute("TagID");
            String vezeteknev = getElementText(tag, "Vezeteknev");
            String keresztnev = getElementText(tag, "Keresztnev");
            String aktiv = getElementText(tag, "Aktiv");
            
            if ("Igen".equals(aktiv)) {
                System.out.printf("ID: %s, Név: %s %s, Státusz: %s\n", id, vezeteknev, keresztnev, "Aktív");
            }
        }
        System.out.println("--- vége ---\n");
    }
    
    // Események
    public void listEvents() {
        System.out.println("\n--- Események ---");
        NodeList esemenyek = document.getElementsByTagName("Esemeny");
        
        for (int i = 0; i < esemenyek.getLength(); i++) {
            Element esemeny = (Element) esemenyek.item(i);
            String id = esemeny.getAttribute("EsemenyID");
            String nev = getElementText(esemeny, "Megnevezes");
            String datum = getElementText(esemeny, "Datum");
            String alapHelyszin = getElementText(esemeny, "AlapHelyszin");
            String esoHelyszin = getElementText(esemeny, "EsoHelyszin");
            
            System.out.printf("ID: %s, Név: %s, Dátum: %s, Helyszín: %s (Eső: %s)\n", id, nev, datum, alapHelyszin, esoHelyszin);
        }
        System.out.println("--- vége ---\n");
    }
    
    // Tagsági típusok
    public void listMembershipTypes() {
        System.out.println("\n--- Tagsági típusok ---");
        NodeList tipusok = document.getElementsByTagName("TagsagiTipus");
        
        for (int i = 0; i < tipusok.getLength(); i++) {
            Element tipus = (Element) tipusok.item(i);
            String id = tipus.getAttribute("TipusID");
            String nev = getElementText(tipus, "Megnevezes");
            String ar = getElementText(tipus, "Ar");
            String idotartam = getElementText(tipus, "Idotartam");
            
            System.out.printf("ID: %s, Név: %s, Ár: %s Ft, Időtartam: %s hónap\n", id, nev, ar, idotartam);
        }
        System.out.println("--- vége ---\n");
    }
    
    // Keresés név szerint
    public void searchUserByName(String name) {
        System.out.println("\n--- Keresés: " + name + " ---");
        NodeList tagok = document.getElementsByTagName("Tag");
        boolean found = false;
        
        for (int i = 0; i < tagok.getLength(); i++) {
            Element tag = (Element) tagok.item(i);
            String vezeteknev = getElementText(tag, "Vezeteknev");
            String keresztnev = getElementText(tag, "Keresztnev");
            String teljesNev = vezeteknev + " " + keresztnev;
            
            if (teljesNev.toLowerCase().contains(name.toLowerCase())) {
                String id = tag.getAttribute("TagID");
                String email = getElementText(tag, "Email");
                String aktiv = getElementText(tag, "Aktiv");
                
                System.out.printf("ID: %s, Név: %s, Email: %s, Státusz: %s\n", id, teljesNev, email, 
                    "Igen".equals(aktiv) ? "Aktív" : "Inaktív");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Nincs találat a keresésre.");
        }
        System.out.println("--- keresés vége ---\n");
    }
    
    // Esemény keresés
    public void searchEventByName(String eventName) {
        System.out.println("\n--- Esemény keresés: " + eventName + " ---");
        NodeList esemenyek = document.getElementsByTagName("Esemeny");
        boolean found = false;
        
        for (int i = 0; i < esemenyek.getLength(); i++) {
            Element esemeny = (Element) esemenyek.item(i);
            String nev = getElementText(esemeny, "Megnevezes");
            
            if (nev.toLowerCase().contains(eventName.toLowerCase())) {
                String id = esemeny.getAttribute("EsemenyID");
                String datum = getElementText(esemeny, "Datum");
                String alapHelyszin = getElementText(esemeny, "AlapHelyszin");
                String dij = getElementText(esemeny, "Dij");
                
                System.out.printf("ID: %s, Név: %s, Dátum: %s, Helyszín: %s, Díj: %s Ft\n", 
                    id, nev, datum, alapHelyszin, dij);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Nincs találat a keresésre.");
        }
        System.out.println("--- keresés vége ---\n");
    }
    
    // Tag részletek
    public void showMemberDetails(String tagId) {
        System.out.println("\n--- Tag adatok: " + tagId + " ---");
        NodeList tagok = document.getElementsByTagName("Tag");
        
        for (int i = 0; i < tagok.getLength(); i++) {
            Element tag = (Element) tagok.item(i);
            String id = tag.getAttribute("TagID");
            
            if (id.equals(tagId)) {
                String vezeteknev = getElementText(tag, "Vezeteknev");
                String keresztnev = getElementText(tag, "Keresztnev");
                System.out.println("Tag ID: " + id);
                System.out.println("Név: " + vezeteknev + " " + keresztnev);
                System.out.println("Email: " + getElementText(tag, "Email"));
                System.out.println("Születési dátum: " + getElementText(tag, "SzuletesiDatum"));
                System.out.println("Tagság kezdete: " + getElementText(tag, "TagsagKezdete"));
                System.out.println("Aktív: " + getElementText(tag, "Aktiv"));
                
                // Telefonszámok
                NodeList telefonszamok = document.getElementsByTagName("Telefonszam");
                System.out.println("Telefonszámok:");
                for (int j = 0; j < telefonszamok.getLength(); j++) {
                    Element telefon = (Element) telefonszamok.item(j);
                    String telefonTagId = getElementText(telefon, "TagID");
                    if (telefonTagId.equals(tagId)) {
                        String tipus = getElementText(telefon, "Tipus");
                        String szam = getElementText(telefon, "Telefonszam");
                        System.out.println("  " + tipus + ": " + szam);
                    }
                }
                
                // Megújítások
                NodeList megujitasok = document.getElementsByTagName("Megujitas");
                System.out.println("Megújítások:");
                for (int j = 0; j < megujitasok.getLength(); j++) {
                    Element megujitas = (Element) megujitasok.item(j);
                    String megujitasTagId = getElementText(megujitas, "TagID");
                    if (megujitasTagId.equals(tagId)) {
                        String datum = getElementText(megujitas, "Datum");
                        String netto = getElementText(megujitas, "Netto");
                        String brutto = getElementText(megujitas, "Brutto");
                        String modszer = getElementText(megujitas, "Modszert");
                        System.out.println("  " + datum + " - " + modszer + " (Nettó: " + netto + " Ft, Bruttó: " + brutto + " Ft)");
                    }
                }
                
                // Események
                NodeList tagEsemenyek = document.getElementsByTagName("TagEsemeny");
                System.out.println("Események:");
                for (int j = 0; j < tagEsemenyek.getLength(); j++) {
                    Element tagEsemeny = (Element) tagEsemenyek.item(j);
                    String tagEsemenyTagId = getElementText(tagEsemeny, "TagID");
                    if (tagEsemenyTagId.equals(tagId)) {
                        String esemenyId = getElementText(tagEsemeny, "EsemenyID");
                        String reszveteliDij = getElementText(tagEsemeny, "ReszveteliDij");
                        String megjegyzes = getElementText(tagEsemeny, "Megjegyzes");
                        
                        // Esemény nevének keresése
                        NodeList esemenyek = document.getElementsByTagName("Esemeny");
                        for (int k = 0; k < esemenyek.getLength(); k++) {
                            Element esemeny = (Element) esemenyek.item(k);
                            if (esemeny.getAttribute("EsemenyID").equals(esemenyId)) {
                                String esemenyNev = getElementText(esemeny, "Megnevezes");
                                System.out.println("  " + esemenyNev + " - Részvételi díj: " + reszveteliDij + " Ft (" + megjegyzes + ")");
                                break;
                            }
                        }
                    }
                }
                
                return;
            }
        }
        
        System.out.println("Nincs ilyen ID-jű tag.");
        System.out.println("--- adatok vége ---\n");
    }
    
    // Teljes dokumentum kiírása blokk formában a konzolra
    public void printDocumentToConsole() {
        System.out.println("\n=== Teljes XML Dokumentum Tartalma ===\n");
        printNode(document.getDocumentElement(), "");
    }
    
    // Rekurzív node kiíró metódus
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
    
    // segédfüggvény
    private String getElementText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
    
    // statisztikák
    public void printStatistics() {
        if (document == null) {
            System.err.println("Nincs beolvasott dokumentum!");
            return;
        }
        
        System.out.println("\n--- Statisztikák ---");
        
        // Felhasználók száma
        NodeList felhasznalok = document.getElementsByTagName("Felhasznalo");
        System.out.println("Felhasználók száma: " + felhasznalok.getLength());
        
        // Tagok száma
        NodeList tagok = document.getElementsByTagName("Tag");
        System.out.println("Tagok száma: " + tagok.getLength());
        
        // Telefonszámok száma
        NodeList telefonszamok = document.getElementsByTagName("Telefonszam");
        System.out.println("Telefonszámok száma: " + telefonszamok.getLength());
        
        // Események száma
        NodeList esemenyek = document.getElementsByTagName("Esemeny");
        System.out.println("Események száma: " + esemenyek.getLength());
        
        // Megújítások száma
        NodeList megujitasok = document.getElementsByTagName("Megujitas");
        System.out.println("Megújítások száma: " + megujitasok.getLength());
        
        // Tagsági típusok száma
        NodeList tagsagiTipusok = document.getElementsByTagName("TagsagiTipus");
        System.out.println("Tagsági típusok száma: " + tagsagiTipusok.getLength());
        
        // Tag-Esemény kapcsolatok száma
        NodeList tagEsemenyek = document.getElementsByTagName("TagEsemeny");
        System.out.println("Tag-Esemény kapcsolatok száma: " + tagEsemenyek.getLength());
        
        System.out.println("--- stat vége ---\n");
    }
    
    // fő metódus
    public static void main(String[] args) {
        try {
            // fájlok
            String inputFile = "XMLEFEW32.xml";
            String outputFile = "XMLEFEW321.xml";
            
            // parser
            DomReadEFEW32 parser = new DomReadEFEW32(inputFile, outputFile);
            
            // beolvasás
            parser.loadXmlDocument();
            
            // Teljes dokumentum kiírása blokk formában a konzolra (feladatkiírás szerint)
            parser.printDocumentToConsole();
            
            // mentés fájlba (feladatkiírás szerint)
            parser.saveXmlDocument();
            
            // menü indítása
            parser.startQuerySystem();
            
            System.out.println("Program sikeresen lefutott!");
            
        } catch (Exception e) {
            System.err.println("Program hiba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
