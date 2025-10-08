package com.yehorychev.playwright.toolshop.fixtures;

import com.microsoft.playwright.*;
import com.yehorychev.playwright.pages.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.nio.file.Paths;

@Execution(ExecutionMode.SAME_THREAD)
public abstract class BaseToolShopTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected SearchComponent searchComponent;
    protected ProductList productList;
    protected ProductDetails productDetails;
    protected NavBar navBar;
    protected CheckoutCart checkoutCart;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
        );
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://practicesoftwaretesting.com");

        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @AfterEach
    void cleanUp(TestInfo testInfo) {
        if (context != null) {
            try {
                context.tracing().stop(
                        new Tracing.StopOptions()
                                .setPath(Paths.get("target/trace/" + testInfo.getDisplayName() + ".zip"))
                );
                System.out.println("Trace saved successfully.");
            } catch (PlaywrightException e) {
                System.out.println("Trace could not be stopped: " + e.getMessage());
            }

            context.close();
        }
    }

    @AfterAll
    static void tearDownBrowser() {
        browser.close();
        playwright.close();
    }
}