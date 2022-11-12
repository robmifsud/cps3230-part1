package com.ecommerce.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;

public class ECommercePageObject {
    WebDriver driver;
    String userId;

    public ECommercePageObject(WebDriver driver, String userId){
        this.driver = driver;
        this.userId = userId;
    }

    public WebElement getElementByXpath(String path) throws NoSuchElementException {
        return driver.findElement(By.xpath(path));
    }

    public WebElement getElementById(String id) throws NoSuchElementException{
        return driver.findElement(By.id(id));
    }

    public String getCurrentSiteUrl() throws WebDriverException{
        return driver.getCurrentUrl();
    }

    public void setDriver(WebDriver driver){
        this.driver = driver;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public LinkedList<HashMap<String, Object>> scrapeProducts(String search, int alertType){
        LinkedList<HashMap<String, Object>> products = new LinkedList<>();

        try {
            WebElement searchBox = getElementById("search");
            searchBox.sendKeys(search + Keys.ENTER);
        }
        catch(NoSuchElementException e){
            System.out.println("Search box not found");
            return products;
        }

        for(int x=1;x<=5;x++){
            boolean breakLoop = false;
            try{
                driver.findElement(By.xpath("//li[position() =" + x + "]//a[contains(@class, 'product-item-photo')]")).click();
            }
            catch(ElementNotInteractableException e){
                return products;
            }
            catch(NoSuchElementException e){
                System.out.println("One result");
                breakLoop = true;
            }

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@class='fotorama__img']")));

            HashMap<String, Object> temp = new HashMap<>();

            temp.put("alertType", alertType);
            try {
                temp.put("heading", getElementByXpath("//span[@class='base']").getText());
            }
            catch(NoSuchElementException e){
                System.out.println("No heading found");
                temp.put("heading", "n/a");
            }
            try {
                temp.put("description", getElementByXpath("//div[@itemprop='description']//li").getText());
            }
            catch(NoSuchElementException e){
                System.out.println("No description found");
                temp.put("description", "n/a");
            }
            try {
                temp.put("url", getCurrentSiteUrl());
            }
            catch(WebDriverException e){
                System.out.println("No URL found");
                temp.put("url", "n/a");
            }
            try {
                temp.put("imageUrl", getElementByXpath("//img[@class='fotorama__img']").getAttribute("src"));
            }
            catch(NoSuchElementException e){
                System.out.println("No iamge URL found");
                temp.put("imageUrl", "n/a");
            }
            temp.put("postedBy", userId);
            try{
                String price = getElementByXpath("//div[@data-role='priceBox']//span[@class='price']").getText();
                temp.put("priceInCents", Integer.parseInt(price.replaceAll("[€.,]","")));
            }
            catch(NoSuchElementException e){
                System.out.println("No price found");
                temp.put("priceInCents", 0);
            }

            products.add(temp);

            if(breakLoop){
                break;
            }

            driver.navigate().back();
        }

        return products;
    }
}
