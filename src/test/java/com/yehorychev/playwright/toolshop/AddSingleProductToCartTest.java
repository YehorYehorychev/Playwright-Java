package com.yehorychev.playwright.toolshop;

import com.yehorychev.playwright.pages.CartLineItem;
import com.yehorychev.playwright.toolshop.fixtures.BaseToolShopTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@Feature("Shopping Cart")
public class AddSingleProductToCartTest extends BaseToolShopTest {

    @Test
    @Story("Add one product to cart")
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
}