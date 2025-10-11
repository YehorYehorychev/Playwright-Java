package com.yehorychev.playwright.toolshop;

import com.yehorychev.playwright.toolshop.fixtures.BaseToolShopTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@Feature("Search for the product")
public class SearchProductsTest extends BaseToolShopTest {

    @Disabled("Temporarily skipping this test until product data is updated")
    @Test
    @Story("Product search")
    @DisplayName("Search products by keyword")
    void searchProductsByKeyword() {
        searchComponent.searchBy("tape");

        List<String> matchingProducts = productList.getProductNames();

        Assertions.assertThat(matchingProducts)
                .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }
}