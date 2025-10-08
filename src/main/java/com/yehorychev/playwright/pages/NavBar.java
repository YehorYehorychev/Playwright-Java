package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;

public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    public void toTheContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }
}