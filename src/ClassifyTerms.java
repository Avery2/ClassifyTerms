import java.io.IOException;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;

public class ClassifyTerms {

  public static void main(String[] args) throws IOException {
    System.out.println("start");

    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      // set content to the text string
      // Important: You must supply a text block (document) with at least twenty tokens (words) to
      // the classifyText method.
      // String content = "cube root of 4000"; // deosnt work
      String content = "google calendar automatic data collection"; // works
      content = correctTokenAmount(content);
      // TODO create method to classify a single string - output will be string category
      Document doc = Document.newBuilder().setContent(content).setType(Type.PLAIN_TEXT).build();
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(doc).build();
      // detect categories in the given text
      ClassifyTextResponse response = language.classifyText(request);

      if (response.getCategoriesCount() == 0) {
        System.out.println("classification failed.");
      }

      for (ClassificationCategory category : response.getCategoriesList()) {
        System.out.printf("Category name : %s, Confidence : %.3f\n", category.getName(),
            category.getConfidence());
      }
    } catch (com.google.api.gax.rpc.InvalidArgumentException e) {
      System.out.println("not enough tokens, probably");
      System.out.println();
    }

    System.out.println("end");
  }

  private static String correctTokenAmount(String input) {
    String output = input;
    // add ' until correct tokens
    // int numTokens = countTokens(input);
    // if (numTokens > 19) {
    // return input;
    // } else {
    // for (int i = 0; i < 20 - numTokens; i++) {
    // input += " ' ";
    // }
    // return input.trim().toLowerCase();
    // }
    // repeat until enough tokens
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
