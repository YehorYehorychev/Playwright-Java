package com.yehorychev.playwright.tests;

import com.microsoft.playwright.*;
import com.yehorychev.playwright.pages.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
import java.util.List;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightPageObjectTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    Page page;

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
        page.navigate("https://practicesoftwaretesting.com");

        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @AfterEach
    void tearDownContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDownBrowser() {
        browser.close();
        playwright.close();
    }

    @Nested
    class WhenSearchingProductsByKeyword {

        @Test
        @DisplayName("Search products by keyword")
        void searchProductsByKeyword() {
            searchComponent.searchBy("tape");

            List<String> matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts)
                    .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
        }
    }

    @Nested
    class WhenAddingItemsToTheCart {

        @Test
        @DisplayName("Add one product with increased quantity to cart")
        void addSingleProductWithQuantity() {
            searchComponent.searchBy("pliers");
            productList.viewProductDetails("Combination Pliers");

            productDetails.increaseQuanityBy(2);
            productDetails.addToCart();

            navBar.openCart();

            List<CartLineItem> lineItems = checkoutCart.getLineItems();

            Assertions.assertThat(lineItems)
                    .hasSize(1)
                    .first()
                    .satisfies(item -> {
                        Assertions.assertThat(item.title()).contains("Combination Pliers");
                        Assertions.assertThat(item.quantity()).isEqualTo(3);
                        Assertions.assertThat(item.total())
                                .isEqualTo(item.quantity() * item.price());
                    });
        }

        @Test
        @DisplayName("Checkout multiple different products")
        void checkoutMultipleProducts() {
            // Add first product
            navBar.openHomePage();
            productList.viewProductDetails("Bolt Cutters");
            productDetails.increaseQuanityBy(2);
            productDetails.addToCart();

            // Add second product
            navBar.openHomePage();
            productList.viewProductDetails("Slip Joint Pliers");
            productDetails.addToCart();

            navBar.openCart();

            List<CartLineItem> lineItems = checkoutCart.getLineItems();

            Assertions.assertThat(lineItems).hasSize(2);
            List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
            Assertions.assertThat(productNames)
                    .contains("Bolt Cutters", "Slip Joint Pliers");

            Assertions.assertThat(lineItems)
                    .allSatisfy(item -> {
                        Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                        Assertions.assertThat(item.price()).isGreaterThan(0.0);
                        Assertions.assertThat(item.total())
                                .isEqualTo(item.quantity() * item.price());
                    });
        }
    }
}