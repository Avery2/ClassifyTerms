import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import com.google.cloud.language.v1.LanguageServiceClient;

public class JSONReader {

  /**
   * Will read in data from JSON file and output wanted information as a CSV file
   * 
   * @param args
   * @throws FileNotFoundException
   * @throws IOException
   * @throws ParseException
   */
  public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
    JSONArray myActivity =
        (JSONArray) new JSONParser().parse(new FileReader("search-MyActivity.json"));
    PrintWriter writer =
        new PrintWriter("myactivity-" + java.time.LocalTime.now() + ".csv", "UTF-8");

    writer.println(
        "Year,Month,Day,Hour,Title,HasLocation,Classification1,Classification2,Confidence,Magnitude,Sentiment");

    // typecasting obj to JSONObject
    JSONObject temp;

    int i = 0;
    try (LanguageServiceClient language = LanguageServiceClient.create()) {

      // temp = (JSONObject) myActivity.get(0);
      // 2019-12-16T19:54:35.148Z
      // System.out.println(temp.get("time"));
      // writer.println(temp.get("time"));

      String[] time;
      // TODO
      for (Object o : myActivity) {
        System.out.println(i);
        // setup
        i++;
        temp = (JSONObject) o;

        time = parseTime(temp.get("time").toString()); // write CSV time
        for (String s : time) {
          writer.print(s + ",");
        }

        writer.print(temp.get("title").toString().replace(",", "") + ",");

        if (temp.get("locationInfos") != null) {
          writer.print("true,");
        } else {
          writer.print("false,");
        }

        // attempt sentiment analysis, then classify
        writer.print(ClassifyTerms.sentimentAndClassifyContent(language,
            ClassifyTerms.correctTokenAmount(temp.get("title").toString())) + ",");

        writer.println();

      }
    } catch (Exception e) {
      System.out.println("i: " + i);
      e.printStackTrace();
    }

    writer.close();
  }

  /**
   * Take in time string, return as array in [Y, M, D, H] order
   * 
   * @param time
   * @return
   */
  private static String[] parseTime(String time) {
    String[] temp = time.split("-");
    String[] output = new String[4];
    // 2019-12-16T19:54:35.148Z
    // 2019
    output[0] = temp[0]; // year
    // 12
    output[1] = temp[1]; // month

    temp = temp[2].split("T");

    // 16
    output[2] = temp[0]; // day
    // 19 or 20
    output[3] = temp[1].split(":")[0]; // hour

    return output;
  }
}
