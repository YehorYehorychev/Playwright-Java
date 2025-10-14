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

    public List<ProductSummary> getProductSummaries() {
        page.waitForSelector("[data-test='product-name']", new Page.WaitForSelectorOptions().setTimeout(10000));
        page.waitForTimeout(500);

        List<ProductSummary> summaries = page.locator(".card").all().stream()
                .map(productCard -> {
                    String productName = productCard.getByTestId("product-name").textContent().strip();
                    String productPrice = productCard.getByTestId("product-price").textContent().strip();
                    return new ProductSummary(productName, productPrice);
                })
                .toList();

        ScreenshotManager.takeScreenshot(page, "Captured product summaries: " + summaries);
        return summaries;
    }

    @Step("View product details for {productName}")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
        ScreenshotManager.takeScreenshot(page, "Opened product details: " + productName);
    }

    public String getSearchCompletedMessage() {
        return page.getByTestId("search_completed").textContent();
    }
}