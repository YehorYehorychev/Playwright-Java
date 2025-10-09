package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Open cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    @Step("Open home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Step("Navigate to the contact page")
    public void toTheContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }
}