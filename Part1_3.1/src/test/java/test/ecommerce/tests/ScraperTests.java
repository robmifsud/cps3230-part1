package test.ecommerce.tests;

import com.ecommerce.pageobjects.ECommercePageObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.LinkedList;

public class ScraperTests {
    WebDriver driver;
    ECommercePageObject site;

    @BeforeEach
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "C:/Users/rober/Code/Dependencies/chromedriver_win32/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.scanmalta.com/shop/");

        site = new ECommercePageObject(driver, "bad51e47-636b-42f0-be4b-631d34376a98");
    }

    @AfterEach
    public void teardown(){
        driver.quit();
    }

    @Test
    public void testScrapeProducts(){
        LinkedList<HashMap<String, Object>> products = site.scrapeProducts("webcam", 6);

        Assertions.assertEquals(5, products.size());
    }

    @Test
    public void testURLnotFound(){
        site = Mockito.mock(ECommercePageObject.class);
        Mockito.when(site.scrapeProducts(Mockito.anyString(), Mockito.anyInt())).thenCallRealMethod();
        Mockito.when(site.getElementByXpath(Mockito.anyString())).thenCallRealMethod();
        Mockito.when(site.getElementById(Mockito.anyString())).thenCallRealMethod();
        Mockito.when(site.getCurrentSiteUrl()).thenThrow(WebDriverException.class);
        Mockito.doCallRealMethod().when(site).setDriver(Mockito.any(WebDriver.class));
        Mockito.doCallRealMethod().when(site).setUserId(Mockito.anyString());

        site.setDriver(driver);
        site.setUserId("bad51e47-636b-42f0-be4b-631d34376a98");

        LinkedList<HashMap<String, Object>> products = site.scrapeProducts("webcam", 6);

        for(HashMap<String, Object> product : products){
            Assertions.assertEquals("n/a",product.get("url"));
        }
    }

    @Test
    public void testScrapeWithMissingSearchBox() {
        ECommercePageObject site = Mockito.mock(ECommercePageObject.class);
        Mockito.when(site.getElementById("search")).thenThrow(NoSuchElementException.class);
        Mockito.when(site.scrapeProducts(Mockito.anyString(), Mockito.anyInt())).thenCallRealMethod();

        LinkedList<HashMap<String, Object>> products = site.scrapeProducts("webcam", 6);

        Assertions.assertEquals(0, products.size());

    }

    @Test
    public void testScrapeWithMissingElement() {
        ECommercePageObject site = Mockito.mock(ECommercePageObject.class);

        Mockito.when(site.scrapeProducts(Mockito.anyString(), Mockito.anyInt())).thenCallRealMethod();
        Mockito.when(site.getElementByXpath(Mockito.anyString())).thenThrow(NoSuchElementException.class);
        Mockito.when(site.getElementById(Mockito.anyString())).thenCallRealMethod();
        Mockito.doCallRealMethod().when(site).setDriver(Mockito.any(WebDriver.class));
        Mockito.doCallRealMethod().when(site).setUserId(Mockito.anyString());

        System.setProperty("webdriver.chrome.driver", "C:/Users/rober/Code/Dependencies/chromedriver_win32/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.scanmalta.com/shop/");

        site.setDriver(driver);
        site.setUserId("bad51e47-636b-42f0-be4b-631d34376a98");

        LinkedList<HashMap<String, Object>> products = site.scrapeProducts("webcam", 6);

        driver.quit();

        for(HashMap<String, Object> product : products){
            Assertions.assertEquals("n/a",product.get("heading"));
        }

    }

    @Test
    public void testScrapeWithOneResult(){
        LinkedList<HashMap<String, Object>> products = site.scrapeProducts("fibre", 6);

        Assertions.assertEquals(1, products.size());
    }

    @Test
    public void testScrapeWithNoResults(){
        LinkedList<HashMap<String, Object>> products = site.scrapeProducts("asdf", 6);

        Assertions.assertEquals(0, products.size());
    }

}
