package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;
import com.yehorychev.playwright.utils.ScreenshotManager;
import io.qameta.allure.Step;

import java.util.List;

public class ProductsList {
    private final Page page;

    public ProductsList(Page page) {
        this.page = page;
    }

    @Step("Get product names")
    public List<String> getProductNames() {
        page.waitForSelector(".card-title", new Page.WaitForSelectorOptions().setTimeout(10000));

        page.waitForTimeout(500);

        List<String> names = page.locator(".card-title").allInnerTexts();

        ScreenshotManager.takeScreenshot(page, "Captured product names: " + names);
        return names;
    }

    @Step("View product details for {productName}")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
        ScreenshotManager.takeScreenshot(page, "Opened product details: " + productName);
    }
}