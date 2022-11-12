package test.ecommerce.tests;

import com.ecommerce.alerts.Alerts;
import com.ecommerce.pageobjects.ECommercePageObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class AlertsTests {

    WebDriver driver;
    ECommercePageObject site;

    @BeforeEach
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/rober/Code/Dependencies/chromedriver_win32/chromedriver.exe");
    }

    @Test
    public void testUploadAlerts() throws IOException {

        LinkedList<HashMap<String, Object>> products = new LinkedList<>();

        for(int i=0; i<4; i++){
            HashMap<String, Object> temp = new HashMap<>();

            temp.put("alertType", 6);
            temp.put("heading", "Streamplify Streaming Webcam 1080P w/ Built-in Microphone and Stand");
            temp.put("description", "2.0 megapixel webcam with Full HD 1080p resolution");
            temp.put("url", "https://www.scanmalta.com/shop/streamplify-streaming-webcam-1080p-w-built-in-microphone-and-stand.html");
            temp.put("imageUrl", "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/z/u/zuwc_034_01_800x800.jpg");
            temp.put("postedBy", "bad51e47-636b-42f0-be4b-631d34376a98");
            temp.put("priceInCents", 8995);

            products.add(temp);
        }

        Alerts alerts = new Alerts("https://api.marketalertum.com/Alert");

        Assertions.assertEquals(products.size(), alerts.uploadAlerts(products));

    }

    @Test
    public void testUpload() throws IOException {
        String json = """
                        {
                            "alertType": 6,
                            "heading":"Streamplify Streaming Webcam 1080P w/ Built-in Microphone and Stand",
                            "description":"2.0 megapixel webcam with Full HD 1080p resolution",
                            "url":"https://www.scanmalta.com/shop/streamplify-streaming-webcam-1080p-w-built-in-microphone-and-stand.html",
                            "imageUrl":"https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/z/u/zuwc_034_01_800x800.jpg",
                            "postedBy":"bad51e47-636b-42f0-be4b-631d34376a98",
                            "priceInCents": 8995
                        }
                        """;

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://api.marketalertum.com/Alert");
        request.setEntity(entity);

        System.out.println(request);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());

        Assertions.assertEquals(201, response.getCode());
    }

    @Test
    public void testUploadWithNoResults() throws IOException{
        LinkedList<HashMap<String, Object>> products = new LinkedList<>();

        Alerts alerts = new Alerts("https://api.marketalertum.com/Alert");

        Assertions.assertEquals(products.size(), alerts.uploadAlerts(products));
    }

    @Test
    public void testUploadWithOneResult() throws IOException{
        LinkedList<HashMap<String, Object>> products = new LinkedList<>();

        HashMap<String, Object> temp = new HashMap<>();

        temp.put("alertType", 6);
        temp.put("heading", "Streamplify Streaming Webcam 1080P w/ Built-in Microphone and Stand");
        temp.put("description", "2.0 megapixel webcam with Full HD 1080p resolution");
        temp.put("url", "https://www.scanmalta.com/shop/streamplify-streaming-webcam-1080p-w-built-in-microphone-and-stand.html");
        temp.put("imageUrl", "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/z/u/zuwc_034_01_800x800.jpg");
        temp.put("postedBy", "bad51e47-636b-42f0-be4b-631d34376a98");
        temp.put("priceInCents", 8995);

        products.add(temp);

        Alerts alerts = new Alerts("https://api.marketalertum.com/Alert");

        Assertions.assertEquals(products.size(), alerts.uploadAlerts(products));
    }

    @Test
    public void testUploadWithLessThanFiveResults() throws IOException{
        LinkedList<HashMap<String, Object>> products = new LinkedList<>();

        for(int i=0; i<4; i++){
            HashMap<String, Object> temp = new HashMap<>();

            temp.put("alertType", 6);
            temp.put("heading", "Streamplify Streaming Webcam 1080P w/ Built-in Microphone and Stand");
            temp.put("description", "2.0 megapixel webcam with Full HD 1080p resolution");
            temp.put("url", "https://www.scanmalta.com/shop/streamplify-streaming-webcam-1080p-w-built-in-microphone-and-stand.html");
            temp.put("imageUrl", "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/z/u/zuwc_034_01_800x800.jpg");
            temp.put("postedBy", "bad51e47-636b-42f0-be4b-631d34376a98");
            temp.put("priceInCents", 8995);

            products.add(temp);
        }

        Alerts alerts = new Alerts("https://api.marketalertum.com/Alert");

        Assertions.assertEquals(products.size(), alerts.uploadAlerts(products));
    }

    @Test
    public void testDeleteAlerts() throws IOException {
        Alerts alerts = new Alerts("https://api.marketalertum.com/Alert?userId=bad51e47-636b-42f0-be4b-631d34376a98");
        Assertions.assertEquals(200, alerts.deleteAlerts());
    }

}
