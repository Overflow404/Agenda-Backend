package service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

public class EndToEndBookingServiceIT {

    /*
    * Effects the real database.
    * */
    @Test
    public void stressTest() throws InterruptedException, IOException {
        final int N_THREAD = 2;
        final int N_BOOKING = 256;

        Thread[] threads = new Thread[N_THREAD];
        HttpClient client = HttpClientBuilder.create().build();

        for (int i = 0; i < N_BOOKING; i++) {
            String url = generateRandomUrl();

            for (int j = 0; j < N_THREAD; j++) {
                threads[j] = new Thread(bookingThread(url, client));
                threads[j].start();
            }

            for (int j = 0; j < N_THREAD; j++) {
                threads[j].join();
            }

        }

        String url1 = "http://localhost:8080/Agenda-1.0-SNAPSHOT/rest/date/retrieveAll";
        HttpGet request = new HttpGet(url1);
        HttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(responseString).getAsJsonObject();
        int count = Integer.parseInt(obj.get("content").getAsString());

        Assert.assertEquals(N_BOOKING, count);
    }

    private Runnable bookingThread(final String url, final HttpClient client) {
        return () -> {
            HttpGet request = new HttpGet(url);
            try {
                client.execute(request);
            } catch (IOException e) {
                System.out.println("I/O exception!");
            } finally {
                request.releaseConnection();
            }
        };
    }

    private String generateRandomUrl() {
        Date randomStart = randomDate();
        Date randomEnd = new Date(DateUtils.addMilliseconds(randomStart, 1).getTime());

        return "http://localhost:8080/Agenda-1.0-SNAPSHOT/rest/date/book?" +
                "subject=testSubject&" +
                "description=testDescription&" +
                "startDate=" + randomStart.getTime() +
                "&endDate=" + randomEnd.getTime();
    }

    private Date randomDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        long startRange = Timestamp.valueOf("2019-01-01 00:00:00").getTime();
        long endRange = Timestamp.valueOf("2019-12-31 00:00:00").getTime();
        long diff = endRange - startRange + 1;
        return new Date(startRange + (long)(Math.random() * diff));
    }
}
