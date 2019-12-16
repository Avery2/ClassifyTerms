import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHTML {

  static String[] UNWANTED = {"this area"};

  public static void main(String[] args) throws IOException {
    File input = new File("MyActivity.html");
    Document doc = Jsoup.parse(input, "UTF-8", "");

    PrintWriter writer = new PrintWriter("myactivity.txt", "UTF-8");
    Elements links = doc.select("a");
    // writer.println(doc.body().text());

    int i = 0;
    for (Element e : links) {
      if (!isUnwantedText(e.text())) {
        writer.println(e.text() + "\n");
        i++;
      }
      // TODO need to be able to include time stamp as well
    }

    writer.close();

    System.out.println("Write " + i + " entries.");
  }

  private static boolean isUnwantedText(String st) {
    for (String uw : UNWANTED) {
      if (st.equals(uw))
        return true;
    }
    return false;
  }

}
