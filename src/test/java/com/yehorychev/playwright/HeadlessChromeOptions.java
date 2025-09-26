package com.yehorychev.playwright;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.Arrays;

public class HeadlessChromeOptions implements OptionsFactory {

    @Override
    public Options getOptions() {
        return new Options()
                .setBrowserName("chromium")
                .setHeadless(true)
                .setBaseUrl("https://practicesoftwaretesting.com")
                .setLaunchOptions(new BrowserType.LaunchOptions()
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
                )
                .setTestIdAttribute("data-test");
    }
}