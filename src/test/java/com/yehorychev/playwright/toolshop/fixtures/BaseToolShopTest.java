package com.yehorychev.playwright.toolshop.fixtures;

import com.microsoft.playwright.*;
import com.yehorychev.playwright.utils.ScreenshotManager;
import com.yehorychev.playwright.pages.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.Arrays;

public abstract class BaseToolShopTest {

    protected static ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(
            () -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    protected static ThreadLocal<Browser> browser
            = ThreadLocal.withInitial(
            () -> {
                return playwright.get().chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(true)
                                .setArgs(Arrays.asList("--no-sandbox", "--disable-extentions", "--disable-gpu"))
                );
            }
    );

    protected BrowserContext context;
    protected Page page;

    protected SearchComponent searchComponent;
    protected ProductsList productList;
    protected ProductDetails productDetails;
    protected NavBar navBar;
    protected CheckoutCart checkoutCart;

    @BeforeEach
    void setUp() {
        context = browser.get().newContext();
        page = context.newPage();
        page.navigate("https://practicesoftwaretesting.com");

        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        searchComponent = new SearchComponent(page);
        productList = new ProductsList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @AfterEach
    void cleanUp(TestInfo testInfo) {
        String traceName = testInfo.getDisplayName().replace(" ", "-").toLowerCase();
        if (context != null) {
            try {
                context.tracing().stop(
                        new Tracing.StopOptions()
                                .setPath(Paths.get("target/trace/" + traceName + ".zip"))
                );
                System.out.println("Trace saved successfully.");
            } catch (PlaywrightException e) {
                System.out.println("Trace could not be stopped: " + e.getMessage());
            }
            ScreenshotManager.takeScreenshot(page, "Final screenshot");
            context.close();
        }
    }

    @AfterAll
    static void tearDownBrowser() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }
}