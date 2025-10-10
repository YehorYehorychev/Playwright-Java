package com.yehorychev.playwright.toolshop;

import com.yehorychev.playwright.pages.CartLineItem;
import com.yehorychev.playwright.toolshop.fixtures.BaseToolShopTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@Feature("Multiple products in the shopping cart")
public class CheckoutMultipleProductsTest extends BaseToolShopTest {

    @Test
    @Story("Checkout a multiple products")
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