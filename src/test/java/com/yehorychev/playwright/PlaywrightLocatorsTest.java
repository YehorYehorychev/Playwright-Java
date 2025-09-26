package com.yehorychev.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightLocatorsTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
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

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("By id")
        @Test
        void locateTheFirstNameFieldByID() {
            page.locator("#first_name").fill("Sarah-Jane");
            assertThat(page.locator("#first_name")).hasValue("Sarah-Jane");
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCssClass() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();
            Assertions.assertTrue(!alertMessages.isEmpty());

        }

        @DisplayName("By attribute")
        @Test
        void locateTheSendButtonByAttribute() {
            page.locator("input[placeholder='Your last name *']").fill("Smith");
            assertThat(page.locator("#last_name")).hasValue("Smith");
        }
    }

    @DisplayName("Locating elements by text using CSS")
    @Nested
    class LocatingElementsByTextUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        // :has-text matches any element containing specified text somewhere inside.
        @DisplayName("Using :has-text")
        @Test
        void locateTheSendButtonByText() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator("#last_name").fill("Smith");
            page.locator("input:has-text('Send')").click();
        }

        // :text matches the smallest element containing specified text.
        @DisplayName("Using :text")
        @Test
        void locateAProductItemByText() {
            page.locator(".navbar :text('Home')").click();
            page.locator(".card :text('Bolt')").click();
            assertThat(page.locator("[data-test=product-name]")).hasText("Bolt Cutters");
        }

        // Exact matches
        @DisplayName("Using :text-is")
        @Test
        void locateAProductItemByTextIs() {
            page.locator(".navbar :text('Home')").click();
            page.locator(".card :text-is('Bolt Cutters')").click();
            assertThat(page.locator("[data-test=product-name]")).hasText("Bolt Cutters");
        }

        // matching with regular expressions
        @DisplayName("Using :text-matches")
        @Test
        void locateAProductItemByTextMatches() {
            page.locator(".navbar :text('Home')").click();
            page.locator(".card :text-matches('Bolt \\\\w+')").click();
            assertThat(page.locator("[data-test=product-name]")).hasText("Bolt Cutters");
        }
    }

    @DisplayName("Locating visible elements")
    @Nested
    class LocatingVisibleElements {
        @BeforeEach
        void openContactPage() {
            openPage();
        }

        @DisplayName("Finding visible and invisible elements")
        @Test
        void locateVisibleAndInvisibleItems() {
            int dropdownItems = page.locator(".dropdown-item").count();
            Assertions.assertTrue(dropdownItems > 0);
        }

        @DisplayName("Finding only visible elements")
        @Test
        void locateVisibleItems() {
            int dropdownItems = page.locator(".dropdown-item:visible").count();
            Assertions.assertTrue(dropdownItems == 0);
        }
    }

    @DisplayName("Locating elements by role")
    @Nested
    class LocatingElementsByRole {

        @DisplayName("Using the BUTTON role")
        @Test
        void byButton() {
            page.navigate("https://practicesoftwaretesting.com/contact");

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Send"))
                    .click();

            List<String> errorMessages = page.getByRole(AriaRole.ALERT).allTextContents();
            Assertions.assertTrue(!errorMessages.isEmpty());
        }

        @DisplayName("Using the HEADING role")
        @Test
        void byHeaderRole() {
            openPage();

            page.locator("#search-query").fill("Pliers");

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Search"))
                    .click();

            Locator searchHeading = page.getByRole(AriaRole.HEADING,
                    new Page.GetByRoleOptions().setName(Pattern.compile("Searched for:.*")));

            assertThat(searchHeading).isVisible();
            assertThat(searchHeading).hasText("Searched for: Pliers");
        }

        @DisplayName("Using the HEADING role and level")
        @Test
        void byHeaderRoleLevel() {
            openPage();

            List<String> level4Headings
                    = page.getByRole(AriaRole.HEADING,
                            new Page.GetByRoleOptions()
                                    .setName("Pliers")
                                    .setLevel(5))
                    .allTextContents();

            org.assertj.core.api.Assertions.assertThat(level4Headings).isNotEmpty();
        }

        @DisplayName("Identifying checkboxes")
        @Test
        void byCheckboxes() {
            playwright.selectors().setTestIdAttribute("data-test");

            openPage();
            page.getByLabel("Hammer").click();
            page.getByLabel("Chisels").click();
            page.getByLabel("Wrench").click();

            int selectedCount =
                    page.getByTestId("filters").
                            getByRole(AriaRole.CHECKBOX,
                                    new Locator.GetByRoleOptions().setChecked(true))
                            .count();

            org.assertj.core.api.Assertions.assertThat(selectedCount).isEqualTo(3);

            List<String> selectedOptions =
                    page.getByTestId("filters").
                            getByRole(AriaRole.CHECKBOX,
                                    new Locator.GetByRoleOptions().setChecked(true))
                            .all()
                            .stream()
                            .map(Locator::inputValue)
                            .toList();

            org.assertj.core.api.Assertions.assertThat(selectedOptions).hasSize(3);
        }
    }

    @DisplayName("Locating elements by placeholders and labels")
    @Nested
    class LocatingElementsByPlaceholdersAndLabels {

        @DisplayName("Using a label")
        @Test
        void byLabel() {
            page.navigate("https://practicesoftwaretesting.com/contact");

            page.getByLabel("First name").fill("Obi-Wan");
            page.getByLabel("Last name").fill("Kenobi");
            page.getByLabel("Email address").fill("obi-wan@kenobi.com");
            page.getByLabel("Subject").selectOption(new SelectOption().setLabel("Customer service"));
            page.getByLabel("Message *").fill("Hello there");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send"));
        }

        @DisplayName("Using a placeholder text")
        @Test
        void byPlaceholder() {
            page.navigate("https://practicesoftwaretesting.com/contact");

            page.getByPlaceholder("Your first name").fill("Obi-Wan");

            page.getByPlaceholder("Your last name").fill("Kenobi");
            page.getByPlaceholder("Your email").fill("obi-wan@kenobi.com");
            page.getByLabel("Subject").selectOption(new SelectOption().setLabel("Customer service"));
            page.getByLabel("Message *").fill("Hello there");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send"));
        }
    }

    @DisplayName("Locating elements by text")
    @Nested
    class LocatingElementsByText {

        @BeforeEach
        void openTheCatalogPage() {
            openPage();
        }

        @DisplayName("Locating an element by text contents")
        @Test
        void byText() {
            page.getByText("Bolt Cutters").click();

            PlaywrightAssertions.assertThat(page.getByText("MightyCraft Hardware")).isVisible();
        }

        @DisplayName("Using alt text")
        @Test
        void byAltText() {
            page.getByAltText("Combination Pliers").click();

            PlaywrightAssertions.assertThat(page.getByText("ForgeFlex Tools")).isVisible();
        }

        @DisplayName("Using title")
        @Test
        void byTitle() {
            page.getByAltText("Combination Pliers").click();

            page.getByTitle("Practice Software Testing - Toolshop").click();
        }
    }

    @DisplayName("Locating elements by test Id")
    @Nested
    class LocatingElementsByTestID {

        @BeforeAll
        static void setTestId() {
            playwright.selectors().setTestIdAttribute("data-test");
        }

        @DisplayName("Using a custom data-test field")
        @Test
        void byTestId() {
            openPage();

            playwright.selectors().setTestIdAttribute("data-test");

            page.getByTestId("search-query").fill("Pliers");
            page.getByTestId("search-submit").click();
        }
    }

    @DisplayName("Nested locators")
    @Nested
    class NestedLocators {

        @BeforeAll
        static void setTestId() {
            playwright.selectors().setTestIdAttribute("data-test");
        }

        @DisplayName("Using roles")
        @Test
        void locatingAMenuItemUsingRoles() {
            openPage();

            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Home"))
                    .click();
        }

        @DisplayName("Using roles with other strategies")
        @Test
        void locatingAMenuItemUsingRolesAndOtherStrategies() {
            openPage();

            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByText("Home")
                    .click();
        }

        @DisplayName("filtering locators by text")
        @Test
        void filteringMenuItems() {
            openPage();

            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByText("Categories")
                    .click();

            page.getByRole(AriaRole.MENUBAR, new Page.GetByRoleOptions().setName("Main Menu"))
                    .getByText("Power Tools")
                    .click();

            page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);

            List<String> allProducts = page.getByTestId("product-name")
                    .filter(new Locator.FilterOptions().setHasText("Sander"))
                    .allTextContents();

            org.assertj.core.api.Assertions.assertThat(allProducts).allMatch(name -> name.contains("Sander"));
        }

        @DisplayName("filtering locators by locator")
        @Test
        void filteringMenuItemsByLocator() {
            openPage();

            List<String> allProducts = page.locator(".card")
                    .filter(new Locator.FilterOptions().setHas(page.getByText("Out of stock")))
                    .getByTestId("product-name")
                    .allTextContents();

            org.assertj.core.api.Assertions.assertThat(allProducts).hasSize(1)
                    .allMatch(name -> name.contains("Long Nose Pliers"));
        }
    }

    private void openPage() {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
}