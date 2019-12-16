// Imports the Google Cloud client library
import java.io.FileInputStream;
import java.io.IOException;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists; // TODO might be another Lists?

public class QuickstartSample {
  public static void main(String... args) throws Exception {
    // authExplicit("/Users/averychan/Desktop/Categorize Terms-2a3b4beffed6.json");
    authExplicit("/Users/averychan/Desktop/Categorize Terms-43393400f5a2.json");

    // Instantiates a client
    try (LanguageServiceClient language = LanguageServiceClient.create()) {

      // The text to analyze
      String text = "Hello, world!";
      Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

      // Detects the sentiment of the text
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

      System.out.printf("Text: %s%n", text);
      System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
    }
  }

  static void authExplicit(String jsonPath) throws IOException {
    // You can specify a credential file by providing a path to GoogleCredentials.
    // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
    GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

    System.out.println("Buckets:");
    Page<Bucket> buckets = storage.list();
    for (Bucket bucket : buckets.iterateAll()) {
      System.out.println(bucket.toString());
    }
  }
}
