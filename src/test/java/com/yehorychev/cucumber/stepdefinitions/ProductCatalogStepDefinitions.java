package com.yehorychev.cucumber.stepdefinitions;

import com.yehorychev.cucumber.fixtures.Hooks;
import com.yehorychev.playwright.pages.NavBar;
import com.yehorychev.playwright.pages.ProductSummary;
import com.yehorychev.playwright.pages.ProductsList;
import com.yehorychev.playwright.pages.SearchComponent;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

public class ProductCatalogStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductsList productList;

    @Before
    public void setupPageObjects() {
        navBar = new NavBar(Hooks.getPage());
        searchComponent = new SearchComponent(Hooks.getPage());
        productList = new ProductsList(Hooks.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        navBar.openHomePage();
    }

    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchBy(searchTerm);
    }

    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);
    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData) {
        return new ProductSummary(productData.get("Product"), productData.get("Price"));
    }

    @Then("the following products should be displayed:")
    public void theFollowingProductsShouldBeDisplayed(List<ProductSummary> expectedProducts) {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProducts);
    }
}
