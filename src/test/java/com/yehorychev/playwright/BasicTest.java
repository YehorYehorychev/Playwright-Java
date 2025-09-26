package com.yehorychev.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@UsePlaywright(BasicTest.MyOptions.class)
public class BasicTest {

    public static class MyOptions implements OptionsFactory {

        @Override
        public Options getOptions() {
            return new Options()
                    .setBrowserName("chromium")
                    .setHeadless(false)
                    .setBaseUrl("https://practicesoftwaretesting.com")
                    .setLaunchOptions(new BrowserType.LaunchOptions()
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
                    );
        }
    }

    @Test
    void shouldShowThePageTitle(Page page) {
        page.navigate("/");
        String pageTitle = page.title();
        Assertions.assertTrue(pageTitle.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword(Page page) {
        page.navigate("/");
        page.locator("//input[@id='search-query']").fill("Pliers");
        page.locator("//button[@class='btn btn-secondary']").click();

        int matchingSearchResults = page.locator("//*[contains(text(),'Pliers')]").count();

        Assertions.assertTrue(matchingSearchResults > 0);
    }

    // How the fixtures look before -> @UsePlaywright impl
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
