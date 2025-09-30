package com.yehorychev.playwright.tests;

import com.microsoft.playwright.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class AssertionsTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Making assertions about the contents of a field")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Checking the value of a field")
        @Test
        void fieldValues() {
            var firstNameField = page.getByLabel("First name");

            firstNameField.fill("Sarah-Jane");

            assertThat(firstNameField).hasValue("Sarah-Jane");

            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();
        }

        @DisplayName("Checking the value of a text field")
        @Test
        void textFieldValues() {
            var messageField = page.getByLabel("Message");

            messageField.fill("This is my message");

            assertThat(messageField).hasValue("This is my message");
        }

        @DisplayName("Checking the value of a dropdown field")
        @Test
        void dropdownFieldValues() {
            var subjectField = page.getByLabel("Subject");

            subjectField.selectOption("Warranty");

            assertThat(subjectField).hasValue("warranty");
        }
    }

    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsAboutDataValues {

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
            page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
        }

        @Test
        void allProductPricesShouldBeCorrectValues() {
            List<Double> prices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(price -> Double.parseDouble(price.replace("$", "")))
                    .toList();

            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(price -> price > 0)
                    .doesNotContain(0.0)
                    .allMatch(price -> price < 1000)
                    .allSatisfy(price ->
                            Assertions.assertThat(price)
                                    .isGreaterThan(0.0)
                                    .isLessThan(1000.0));
        }

        @Test
        void shouldSortInAlphabeticalOrder() {
            page.getByTestId("sort").selectOption("name,asc");
            page.waitForSelector("[data-test='product-name']:has-text('Adjustable Wrench')");

            List<String> productNames = page.locator("[data-test='product-name']:visible")
                    .allInnerTexts()
                    .stream()
                    .map(String::trim)
                    .toList();

            System.out.println("Products A-Z: " + productNames);

            Assertions.assertThat(productNames)
                    .isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
        }

        @Test
        void shouldSortInReverseAlphabeticalOrder() {
            page.getByTestId("sort").selectOption("name,desc");
            page.waitForFunction(
                    "() => document.querySelector('[data-test=\"product-name\"]').textContent.includes('Wood Saw')"
            );

            List<String> productNames = page.locator("[data-test='product-name']:visible")
                    .allInnerTexts()
                    .stream()
                    .map(String::trim)
                    .toList();

            System.out.println("Products Z-A: " + productNames);

            Assertions.assertThat(productNames)
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }
    }
}