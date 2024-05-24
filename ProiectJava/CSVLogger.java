import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVLogger {
    private static final String FILE_NAME = "log.csv";

    public static void log(String actiune) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            String dataSiOra = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.append(actiune).append(", ").append(dataSiOra).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
