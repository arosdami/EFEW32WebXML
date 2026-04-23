package efew32;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONWriteEFEW32 {
    
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader("../EFEW32_orarend.json"));
            
            JSONArray jsonArray = (JSONArray) obj;
            
            System.out.println("=== EFEW32 Órarend JSON adatok blokk formában ===\n");
            
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> iterator = jsonArray.iterator();
            int oraSzamlalo = 1;
            
            while (iterator.hasNext()) {
                JSONObject oraObject = iterator.next();
                
                System.out.println("--------------------------------------------------");
                System.out.println("Óra #" + oraSzamlalo);
                System.out.println("--------------------------------------------------");
                System.out.println("ID: " + oraObject.get("@id"));
                System.out.println("Típus: " + oraObject.get("@tipus"));
                System.out.println("Kurzus: " + oraObject.get("kurzus"));
                
                JSONObject idopont = (JSONObject) oraObject.get("idopont");
                System.out.println("Időpont:");
                System.out.println("  Nap: " + idopont.get("nap"));
                System.out.println("  Kezdés: " + idopont.get("tol"));
                System.out.println("  Befejezés: " + idopont.get("ig"));
                
                System.out.println("Helyszín: " + oraObject.get("helyszin"));
                System.out.println("Oktató: " + oraObject.get("oktato"));
                System.out.println("Szak: " + oraObject.get("szak"));
                System.out.println();
                
                oraSzamlalo++;
            }
            
            FileWriter fileWriter = new FileWriter("../EFEW32_orarend1.json");
            
            fileWriter.write("[\n");
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> iterator2 = jsonArray.iterator();
            oraSzamlalo = 1;
            
            while (iterator2.hasNext()) {
                JSONObject oraObject = iterator2.next();
                
                fileWriter.write("  {\n");
                fileWriter.write("    \"@id\": \"" + oraObject.get("@id") + "\",\n");
                fileWriter.write("    \"@tipus\": \"" + oraObject.get("@tipus") + "\",\n");
                fileWriter.write("    \"kurzus\": \"" + oraObject.get("kurzus") + "\",\n");
                
                JSONObject idopont = (JSONObject) oraObject.get("idopont");
                fileWriter.write("    \"idopont\": {\n");
                fileWriter.write("      \"nap\": \"" + idopont.get("nap") + "\",\n");
                fileWriter.write("      \"tol\": \"" + idopont.get("tol") + "\",\n");
                fileWriter.write("      \"ig\": \"" + idopont.get("ig") + "\"\n");
                fileWriter.write("    },\n");
                
                fileWriter.write("    \"helyszin\": \"" + oraObject.get("helyszin") + "\",\n");
                fileWriter.write("    \"oktato\": \"" + oraObject.get("oktato") + "\",\n");
                fileWriter.write("    \"szak\": \"" + oraObject.get("szak") + "\"\n");
                
                if (iterator2.hasNext()) {
                    fileWriter.write("  },\n");
                } else {
                    fileWriter.write("  }\n");
                }
                
                oraSzamlalo++;
            }
            
            fileWriter.write("]\n");
            fileWriter.close();
            
            System.out.println("--------------------------------------------------");
            System.out.println("Összesen " + (oraSzamlalo - 1) + " óra található.");
            System.out.println("Az adatok sikeresen kiírva az EFEW32_orarend1.json fájlba.");
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
