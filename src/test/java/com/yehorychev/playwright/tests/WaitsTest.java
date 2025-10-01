package com.yehorychev.playwright.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class WaitsTest {

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

    @Nested
    class WaitingForState {

        // Our explicit wait | Waiting for images on the screen
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
            page.waitForSelector(".card-img-top");
        }

        @Test
        void shouldShowAllProductNamesTest() {
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
        }

        @Test
        void shouldShowAllProductImagesTest() {
            List<String> productImageTitles = page.locator(".card-img-top").all()
                    .stream()
                    .map(img -> img.getAttribute("alt"))
                    .toList();

            Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters", "Hammer");
        }
    }

    @Nested
    class AutomaticWaits {

        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

        // Automatic wait
        @Test
        @DisplayName("Should wait for the filter checkbox options to appear before clicking")
        void shouldWaitForTheFilterCheckboxesTest() {
            var screwdriverFilter = page.getByLabel(" Screwdriver ");
            screwdriverFilter.click();

            assertThat(screwdriverFilter).isChecked();
        }

        @Test
        @DisplayName("Should filter products by category")
        void shouldFilterProductsByCategoryTest() {
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUITEM).getByText("Power Tools").click();

            // Wait ->
            page.waitForSelector(".card", new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(2000));

            var filterProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filterProducts).contains("Sheet Sander", "Belt Sander", "Circular Saw");
        }
    }

    @Nested
    class WaitingForElementsToAppearAndDisappear {
        @BeforeEach
        void openHomePage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void shouldDisplayToasterMessageTest() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            // Wait for the toaster message to appear
            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @Test
        @DisplayName("Should update the cart item count")
        void shouldUpdateCartItemCountTest() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
            // page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
        }

        // Wait for an element to have a particular state
        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void shouldDisplayTheCartItemCountTest() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            // Wait for the cart quantity to update
            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
            // Or
            page.waitForSelector("[data-test='cart-quantity']:has-text('1')");
        }
    }

    @Nested
    class WaitingForAPICalls {

        @Test
        void sortByDescendingPrice() {
            page.navigate("https://practicesoftwaretesting.com");

            // https://api.practicesoftwaretesting.com/products?page=0&sort=price,desc&between=price,1,100
            // Sort by descending price
            page.waitForResponse(
                    response -> response.url().contains("products")
                            && response.url().contains("sort=price,desc")
                            && response.status() == 200,
                    () -> page.getByTestId("sort").selectOption("Price (High - Low)")
            );

            // Find all the prices on the page
            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICalls::extractPrice)
                    .toList();

            // Are the prices in the correct order
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }

        private static double extractPrice(String price) {
            return Double.parseDouble(price.replace("$", ""));
        }
    }
}