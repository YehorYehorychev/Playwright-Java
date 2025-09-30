package com.yehorychev.playwright.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;

public class LegacyFixturesBasicTest {
    // Shared browser-context for all tests
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
        );

        browserContext = browser.newContext(
                new Browser.NewContextOptions()
                        .setBaseURL("https://practicesoftwaretesting.com")
        );
    }

    @BeforeEach
    public void setup() {
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowThePageTitle() {
        page.navigate("/");
        String pageTitle = page.title();
        Assertions.assertTrue(pageTitle.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword() {
        page.navigate("/");
        page.locator("//input[@id='search-query']").fill("Pliers");
        page.locator("//button[@class='btn btn-secondary']").click();

        int matchingSearchResults = page.locator("//*[contains(text(),'Pliers')]").count();

        Assertions.assertTrue(matchingSearchResults > 0);
    }
}