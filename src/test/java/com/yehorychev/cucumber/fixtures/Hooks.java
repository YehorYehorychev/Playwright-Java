package com.yehorychev.cucumber.fixtures;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public abstract class Hooks {

    protected static ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    private static final ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    @Before
    public void setUpBrowserContext() {
        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After
    public void closeContext(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = page.get().screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );
            Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));
        }
        browserContext.get().close();
    }

    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getBrowserContext() {
        return browserContext.get();
    }
}