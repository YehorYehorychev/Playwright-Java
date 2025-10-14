package com.yehorychev.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.yehorychev.playwright.utils.ScreenshotManager;
import io.qameta.allure.Step;

public class SearchComponent {
    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    @Step("Search for product: {keyword}")
    public void searchBy(String keyword) {
        page.getByPlaceholder("Search").fill(keyword);
        ScreenshotManager.takeScreenshot(page, "Filled search field: " + keyword);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        page.waitForSelector("text=Searched for:", new Page.WaitForSelectorOptions().setTimeout(10000));
        page.waitForSelector("[data-test='product-name']", new Page.WaitForSelectorOptions().setTimeout(10000));

        ScreenshotManager.takeScreenshot(page, "Search results for: " + keyword);
    }

    public void filterBy(String filterName) {
        page.waitForResponse("**/products?**by_category=**", () -> {
            page.getByLabel(filterName).click();
        });
    }

    public void sortBy(String sortFilter) {
        page.waitForResponse("**/products**sort=**", () -> {
            page.getByTestId("sort").selectOption(sortFilter);
        });
        page.waitForTimeout(250);
    }
}