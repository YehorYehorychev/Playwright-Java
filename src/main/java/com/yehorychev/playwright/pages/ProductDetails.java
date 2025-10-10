package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.yehorychev.playwright.utils.ScreenshotManager;
import io.qameta.allure.Step;

public class ProductDetails {
    private final Page page;

    public ProductDetails(Page page) {
        this.page = page;
    }

    @Step("Increment the quantity")
    public void increaseQuanityBy(int increment) {
        for (int i = 1; i <= increment; i++) {
            page.getByTestId("increase-quantity").click();
        }
        ScreenshotManager.takeScreenshot(page, "Increment the quantity");
    }

    @Step("Add to cart")
    public void addToCart() {
        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () -> {
                    page.getByText("Add to cart").click();
                    page.getByRole(AriaRole.ALERT).click();
                }
        );
        ScreenshotManager.takeScreenshot(page, "Add to cart");
    }
}