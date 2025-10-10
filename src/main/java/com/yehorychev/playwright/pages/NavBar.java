package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;
import com.yehorychev.playwright.utils.ScreenshotManager;
import io.qameta.allure.Step;

public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Open cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
        ScreenshotManager.takeScreenshot(page, "Shopping cart");
    }

    @Step("Open home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
        ScreenshotManager.takeScreenshot(page, "Open home page");
    }

    @Step("Navigate to the contact page")
    public void toTheContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
        ScreenshotManager.takeScreenshot(page, "Open contacts page");
    }
}