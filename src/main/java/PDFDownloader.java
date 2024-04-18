import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class PDFDownloader {
    @SuppressWarnings("deprecation")
    public static void downloadPDF(String url, String localStoragePath) throws IOException {
        URL pdfUrl = new URL(url);
        URLConnection urlConnection = pdfUrl.openConnection();

        try(InputStream inputStream = urlConnection.getInputStream()){
            try (FileOutputStream outputStream = new FileOutputStream(localStoragePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                throw new Exception(STR."Error writing for link: \{url}",e);
            }
        } catch (Exception e) {
            throw new IOException(STR."Error downloading from link: \{url}", e);
        }

    }
}
