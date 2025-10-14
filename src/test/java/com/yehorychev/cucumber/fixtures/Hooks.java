package com.yehorychev.cucumber.fixtures;

import com.microsoft.playwright.*;
import io.cucumber.java.*;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class Hooks {

    private static ThreadLocal<Playwright> playwright = ThreadLocal.withInitial(() -> {
        Playwright pw = Playwright.create();
        pw.selectors().setTestIdAttribute("data-test");
        return pw;
    });

    private static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    @Before(order = 0)
    public void init() {
        context.set(browser.get().newContext());
        page.set(context.get().newPage());
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = page.get().screenshot(
                    new Page.ScreenshotOptions().setFullPage(true)
            );
            Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));
        }
        context.get().close();
    }

    @AfterAll
    public static void closeAll() {
        browser.get().close();
        playwright.get().close();
    }

    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getBrowserContext() {
        return context.get();
    }
}