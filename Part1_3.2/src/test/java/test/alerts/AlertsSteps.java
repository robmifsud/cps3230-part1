package test.alerts;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.HashMap;

public class AlertsSteps {
    WebDriver driver;
    int alertCount = 0; // Tracks how many alerts were uploaded to be used in Assertions later

    @Before
    public void setup() throws IOException {deleteAlerts();}

    @After
    public void teardown(){driver.quit();}

    @Given("I am a user of marketalertum")
    public void iAmAUserOfMarketalertum() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/rober/Code/Dependencies/chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.marketalertum.com/Alerts/Login");
        WebElement field = driver.findElement(By.id("UserId"));
        field.sendKeys("bad51e47-636b-42f0-be4b-631d34376a98");
        field.sendKeys(Keys.ENTER);
    }

    @When("I login using {word} credentials")
    public void iLoginUsingCredentials(String arg0) {
        // Logging out first as we are testing login functionality
        driver.get("https://www.marketalertum.com/Home/Logout");
        driver.get("https://www.marketalertum.com/Alerts/Login");
        WebElement field = driver.findElement(By.id("UserId"));
        if(arg0.equals("valid")){
            field.sendKeys("bad51e47-636b-42f0-be4b-631d34376a98");
        }
        else if(arg0.equals("invalid")){
            field.sendKeys("asdf");
        }
        field.sendKeys(Keys.ENTER);
    }

    @Then("I should see my alerts")
    public void iShouldSeeMyAlerts() {
        Assertions.assertEquals("https://www.marketalertum.com/Alerts/List", driver.getCurrentUrl());
    }

    @Then("I should see the login screen again")
    public void iShouldSeeTheLoginScreenAgain() {
        Assertions.assertEquals("https://www.marketalertum.com/Alerts/Login", driver.getCurrentUrl());
    }

    @Given("I am an administrator of the website and I upload {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAlerts(int arg0) throws IOException {
        alertCount = arg0;
        for(int i=0;i<arg0;i++){
            uploadAlert();
        }
    }

    @When("I view a list of alerts")
    public void iViewAListOfAlerts() {
        driver.get("https://www.marketalertum.com/Alerts/List");
    }

    @Then("each alert should contain an icon")
    public void eachAlertShouldContainAnIcon() {
        Assertions.assertEquals(alertCount, driver.findElements(By.xpath("//h4/img")).size());
    }

    @And("each alert should contain a heading")
    public void eachAlertShouldContainAHeading() {
        Assertions.assertEquals(alertCount, driver.findElements(By.xpath("//h4")).size());
    }

    @And("each alert should contain a description")
    public void eachAlertShouldContainADescription() {
        Assertions.assertEquals(alertCount, driver.findElements(By.xpath("//tbody/tr[3]/td")).size());
    }

    @And("each alert should contain an image")
    public void eachAlertShouldContainAnImage() {
        Assertions.assertEquals(alertCount, driver.findElements(By.xpath("//td/img")).size());
    }

    @And("each alert should contain a price")
    public void eachAlertShouldContainAPrice() {
        Assertions.assertEquals(alertCount, driver.findElements(By.xpath("//td/b/following-sibling::text()/parent::td")).size());
    }

    @And("each alert should contain a link to the original product website")
    public void eachAlertShouldContainALinkToTheOriginalProductWebsite() {
        Assertions.assertEquals(alertCount, driver.findElements(By.xpath("//td/a")).size());
    }

    @Given("I am an administrator of the website and I upload more than {int} alerts")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadMoreThanAlerts(int arg0) throws IOException {
        for(int i=0;i<(arg0+1);i++){
            uploadAlert();
        }
    }

    @When("I should see {int} alerts")
    public void iViewAListOfAlertsIShouldSeeAlerts(int arg0) {
        Assertions.assertEquals(arg0, driver.findElements(By.xpath("//table")).size());
    }

    @Given("I am an administrator of the website and I upload an alert of type {int}")
    public void iAmAnAdministratorOfTheWebsiteAndIUploadAnAlertOfTypeAlertType(int arg0) throws IOException {
        HashMap<String, Object> temp = new HashMap<>();

        temp.put("alertType", arg0);
        temp.put("heading", "Streamplify Streaming Webcam 1080P w/ Built-in Microphone and Stand");
        temp.put("description", "2.0 megapixel webcam with Full HD 1080p resolution");
        temp.put("url", "https://www.scanmalta.com/shop/streamplify-streaming-webcam-1080p-w-built-in-microphone-and-stand.html");
        temp.put("imageUrl", "https://www.scanmalta.com/shop/pub/media/catalog/product/cache/51cb816cf3b30ca1f94fc6cfcae49286/z/u/zuwc_034_01_800x800.jpg");
        temp.put("postedBy", "bad51e47-636b-42f0-be4b-631d34376a98");
        temp.put("priceInCents", 8995);

        JSONObject json = new JSONObject(temp);
        String jsonTemp = json.toString();

        StringEntity entity = new StringEntity(jsonTemp, ContentType.APPLICATION_JSON);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://api.marketalertum.com/Alert");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
    }

    @And("the icon displayed should be {word}")
    public void theIconDisplayedShouldBeIconFileName(String arg0) {
        Assertions.assertTrue(driver.findElement(By.xpath("//h4/img")).getAttribute("src").contains(arg0));
    }

    public void uploadAlert() throws IOException {
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

        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
    }

    public void deleteAlerts() throws IOException {
        HttpDelete request = new HttpDelete("https://api.marketalertum.com/Alert?userId=bad51e47-636b-42f0-be4b-631d34376a98");
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.toString());
    }

}
