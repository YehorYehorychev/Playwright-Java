package com.yehorychev.cucumber.stepdefinitions;

import com.yehorychev.cucumber.fixtures.Hooks;
import com.yehorychev.playwright.pages.NavBar;
import com.yehorychev.playwright.pages.ProductSummary;
import com.yehorychev.playwright.pages.ProductsList;
import com.yehorychev.playwright.pages.SearchComponent;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
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

    @Then("no products should be displayed")
    public void noProductsShouldBeDisplayed() {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).isEmpty();
    }

    @And("the message {string} should be displayed")
    public void theMessageShouldBeDisplayed(String messageText) {
        String completionMessage = productList.getSearchCompletedMessage();
        Assertions.assertThat(completionMessage).isEqualTo(messageText);
    }

    @And("she filters by {string}")
    public void sheFiltersBy(String filterName) {
        searchComponent.filterBy(filterName);
    }

    @When("she sorts by {string}")
    public void sheSortsBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
    }

    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String firstProductName) {
        List<String> productNames = productList.getProductNames();
        Assertions.assertThat(productNames).startsWith(firstProductName);
    }
}
