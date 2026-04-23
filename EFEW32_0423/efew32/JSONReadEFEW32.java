package efew32;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReadEFEW32 {
    
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader("../EFEW32_orarend.json"));
            
            JSONArray jsonArray = (JSONArray) obj;
            
            System.out.println("=== EFEW32 Órarend JSON adatok ===\n");
            
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> iterator = jsonArray.iterator();
            int oraSzamlalo = 1;
            
            while (iterator.hasNext()) {
                JSONObject oraObject = iterator.next();
                
                System.out.println("Óra #" + oraSzamlalo + ":");
                System.out.println("  ID: " + oraObject.get("@id"));
                System.out.println("  Típus: " + oraObject.get("@tipus"));
                System.out.println("  Kurzus: " + oraObject.get("kurzus"));
                
                JSONObject idopont = (JSONObject) oraObject.get("idopont");
                System.out.println("  Időpont:");
                System.out.println("    Nap: " + idopont.get("nap"));
                System.out.println("    Kezdés: " + idopont.get("tol"));
                System.out.println("    Befejezés: " + idopont.get("ig"));
                
                System.out.println("  Helyszín: " + oraObject.get("helyszin"));
                System.out.println("  Oktató: " + oraObject.get("oktato"));
                System.out.println("  Szak: " + oraObject.get("szak"));
                System.out.println();
                
                oraSzamlalo++;
            }
            
            System.out.println("Összesen " + (oraSzamlalo - 1) + " óra található.");
            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
