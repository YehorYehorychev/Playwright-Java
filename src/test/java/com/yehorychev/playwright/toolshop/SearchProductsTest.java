package com.yehorychev.playwright.toolshop;

import com.yehorychev.playwright.toolshop.fixtures.BaseToolShopTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SearchProductsTest extends BaseToolShopTest {

    @Test
    @DisplayName("Search products by keyword")
    void searchProductsByKeyword() {
        searchComponent.searchBy("tape");

        List<String> matchingProducts = productList.getProductNames();

        Assertions.assertThat(matchingProducts)
                .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
    }
}