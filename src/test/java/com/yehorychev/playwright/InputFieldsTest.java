package com.yehorychev.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class InputFieldsTest {

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields {

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Input Fields")
        @Test
        void completeFormTest(Page page) {
            var firstNameField = page.getByLabel("First name");
            var lastNameField  = page.getByLabel("Last name");
            var emailField     = page.getByLabel("Email address");

            firstNameField.fill("Yehor");
            lastNameField.fill("Yehorychev");
            emailField.fill("yehor@example.com");

            assertThat(firstNameField).hasValue("Yehor");
            assertThat(lastNameField).hasValue("Yehorychev");
            assertThat(emailField).hasValue("yehor@example.com");
        }
    }
}