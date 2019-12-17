import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class ClassifyTerms {

  private static ClassifyTextRequest request;
  private static ClassifyTextResponse response;

  public static void main(String[] args) throws IOException {
    // String content = null;
    // System.out.println("start");
    //
    // // TODO have json file reader instead
    // // File file = new File("myactivity.txt");
    // // BufferedReader inputStream = new BufferedReader(new FileReader(file));
    //
    // // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    // try (LanguageServiceClient language = LanguageServiceClient.create()) {
    // // set content to the text string
    // // content = "google calendar automatic data collection"; // works
    // // for (int i = 0; i < 5; i++) {
    // // content = inputStream.readLine();
    // // while (content.trim().length() < 1) {
    // // content = inputStream.readLine();
    // // }
    // // }
    //
    // content = "Concatenate a string like in printf - Stack Overflow";
    //
    // content = correctTokenAmount(content);
    //
    //// System.out.print(sentimentAndClassifyContent(language, content));
    // } catch (com.google.api.gax.rpc.InvalidArgumentException e) {
    // System.out.println("not enough tokens, probably");
    // System.out.println();
    // }
    //
    // // inputStream.close();
    // System.out.println("end");
  }

  public static String sentimentAndClassifyContent(LanguageServiceClient language, String content) {

    String output = "";
    String conf;
    String[] subject;

    Document doc = Document.newBuilder().setContent(content).setLanguage("en")
        .setType(Type.PLAIN_TEXT).build();
    // set content (into correct format)
    try {
      request = ClassifyTextRequest.newBuilder().setDocument(doc).build();
      // detect categories in the given text
      response = language.classifyText(request);



      // write classification
      // name: "/Arts & Entertainment/TV & Video/TV Shows & Programs"\nconfidence: 0.83

      // confidence: 0.83
      //
      // Arts & Entertainment
      // TV & Video
      // TV Shows & Programs
      // 1

      if (response.getCategoriesCount() == 0) { // if failed
        output += ",,,";
      } else {
        conf = response.getCategories(0).toString().split("\n")[1].split(" ")[1];
        subject = response.getCategories(0).toString().split("\"")[1].split("/");


//        for (String s : subject) {
//          System.out.println(s);
//        }
        if (subject.length < 3) {
          output += subject[1] + "," + "," + conf + ",";
        } else {
          output += subject[1] + "," + subject[2] + "," + conf + ",";
        }
      }



    } catch (com.google.api.gax.rpc.InvalidArgumentException e) {
      System.out.println("not enough tokens, probably");
    } catch (com.google.api.gax.rpc.ResourceExhaustedException e) {
      System.out.println("\'slow down buddy\' -google");
      try {
        Thread.sleep(60001);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }

    // int i = 0;
    // for (ClassificationCategory category : response.getCategoriesList()) {
    //// if (i >= 3) { // TODO, what do i do? just have one category?
    //// break;
    //// }
    // output += String.format("Category name : %s, Confidence : %.3f\n", category.getName(),
    // category.getConfidence());
    // i++;
    // }
    // if (i < 3) { // allow for maximum of 3 categories, and allow CSV format to allow this
    // for (int j = i; j <= 3; j++) {
    // output += "\n";
    // }
    // }

    // sentiment analysis

    AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
    Sentiment sentiment = response.getDocumentSentiment();
    if (sentiment == null) {
      System.out.println("sentiment failed");
      output += (",,");
    } else {
      // output += String.format("Sentiment magnitude: %.3f,", sentiment.getMagnitude());
      output += sentiment.getMagnitude() + ",";
      // output += String.format("Sentiment score: %.3f,", sentiment.getScore());
      output += sentiment.getScore() + ",";
    }

    return output;
  }

  public static String correctTokenAmount(String input) {
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
