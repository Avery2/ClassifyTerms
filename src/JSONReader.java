import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JSONReader {
  public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
    // parsing file "JSONExample.json"
    JSONArray ja = (JSONArray) new JSONParser().parse(new FileReader("search-MyActivity.json"));
    // typecasting obj to JSONObject
    JSONObject o1 = (JSONObject) ja.get(0);
    
    System.out.println(o1.get("title"));

//    System.out.println(ja.get(0));
//    System.out.println(ja.get(1));
//    System.out.println(ja.get(2));
  }
}
