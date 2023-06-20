import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LogScanner {
    public static void main(String[] args) {
        String serverName = args[0];
        String tmpFilePath = "/path/to/temp_file.txt";

        Set<String> uniqueErrors = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(tmpFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Store unique errors, warnings, or exceptions
                uniqueErrors.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Construct email notification with unique errors
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Server: ").append(serverName).append("\n");
        emailContent.append("Unique Errors, Warnings, or Exceptions:\n");
        for (String error : uniqueErrors) {
            emailContent.append(error).append("\n");
        }

        // TODO: Use JavaMail or any email library to send the email notification
    }
}
