package com.yehorychev.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class BasicTest {

    @Test
    void shouldShowThePageTitle(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        String pageTitle = page.title();

        Assertions.assertTrue(pageTitle.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword(Page page) {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("//input[@id='search-query']").fill("Pliers");
        page.locator("//button[@class='btn btn-secondary']").click();

        int matchingSearchResults = page.locator("//*[contains(text(),'Pliers')]").count();

        Assertions.assertTrue(matchingSearchResults > 0);
    }

    // Legacy fixtures BEFORE -> @UsePlaywright
    /*
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }
    */
}
