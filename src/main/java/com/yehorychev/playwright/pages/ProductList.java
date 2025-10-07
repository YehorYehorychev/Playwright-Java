package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;
import java.util.List;

public class ProductList {
    private final Page page;

    public ProductList(Page page) {
        this.page = page;
    }

    public List<String> getProductNames() {
        return page.getByTestId("product-name").allInnerTexts();
    }

    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }
}