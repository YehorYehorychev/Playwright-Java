package com.yehorychev.cucumber.stepdefinitions;

import com.yehorychev.playwright.pages.NavBar;
import com.yehorychev.playwright.pages.ProductList;
import com.yehorychev.playwright.pages.SearchComponent;
import io.cucumber.java.en.*;

public class ProductCatalogStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {

    }

    @When("she searches for {string}")
    public void she_searches_for(String string) {

    }

    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String string) {

    }
}
