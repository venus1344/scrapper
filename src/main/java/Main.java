import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void getPages(
            List<String> pagesToScrape,
            Set<String> pagesDiscovered
    ) {
        String url = pagesToScrape.removeFirst();
        pagesDiscovered.add(url);

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Chrome/108.0.0.0")
                    .header("Accept-Language","*")
                    .get();

            List<Element> rows = doc.select("table[id=atable] > tbody > tr");
            Map<Integer, String> data = new HashMap<>();
            int i = 0;
            for (Element row : rows) {
                String link = row.select("td:nth-of-type(2)").first()
                        .select("a").first().attr("href");
                String[] parts= link
                        .split(" ");
                if (parts.length >= 2) {
                    data.put(i, parts[1]);
                } else {
                    data.put(i,link);
                }
                i+=1;
            }

            if(!data.isEmpty()) {
                data.forEach((key, value) -> {
                    System.out.println("Now: " + value);
                    String[] cops = value.split("/");
                    try {
                        PDFDownloader.downloadPDF(value,"I:\\Projects\\agro\\scrapper\\files\\"+cops[cops.length - 1]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Set<String> pagesDiscovered = new HashSet<>();
        List<String> pagesToScrape = new ArrayList<>();

        pagesToScrape.add("https://www.kilimo.go.tz/resources/category/machapisho");

        while (!pagesToScrape.isEmpty()) {
            getPages(pagesToScrape, pagesDiscovered);
        }
    }


}
