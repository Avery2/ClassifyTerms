import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;

public class ClassifyTerms {

  private static ClassifyTextRequest request;
  private static ClassifyTextResponse response;

  public static void main(String[] args) throws IOException {
    String content = null;
    System.out.println("start");

    // TODO have json file reader instead
//    File file = new File("myactivity.txt");
//    BufferedReader inputStream = new BufferedReader(new FileReader(file));

    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set content to the text string
      // content = "google calendar automatic data collection"; // works
//      for (int i = 0; i < 5; i++) {
//        content = inputStream.readLine();
//        while (content.trim().length() < 1) {
//          content = inputStream.readLine();
//        }
//      }
      
      content = "Concatenate a string like in printf - Stack Overflow";
      
      content = correctTokenAmount(content);

      System.out.print(classifyContent(language, content));
    } catch (com.google.api.gax.rpc.InvalidArgumentException e) {
      System.out.println("not enough tokens, probably");
      System.out.println();
    }

//    inputStream.close();
    System.out.println("end");
  }

  private static String classifyContent(LanguageServiceClient language, String content) {

    String output = "";

    // set content (into correct format) TODO do I have to build this every time?
    Document doc = Document.newBuilder().setContent(content).setType(Type.PLAIN_TEXT).build();
    request = ClassifyTextRequest.newBuilder().setDocument(doc).build();
    // detect categories in the given text
    response = language.classifyText(request);

    if (response.getCategoriesCount() == 0) {
      System.out.println("classification failed.");
    }

    for (ClassificationCategory category : response.getCategoriesList()) {
      output += String.format("Category name : %s, Confidence : %.3f\n", category.getName(),
          category.getConfidence());
    }

    return output;
  }

  private static String correctTokenAmount(String input) {
    // Important: You must supply a text block (document) with at least twenty tokens (words) to
    // the classifyText method.
    String output = input;
    while (countTokens(output) < 20) {
      output += " " + input.trim();
    }
    return output.trim();
  }

  private static int countTokens(String str) {
    if (str == null || str.isEmpty()) {
      return 0;
    }

    String[] words = str.split("\\s+");
    return words.length;
  }
}
