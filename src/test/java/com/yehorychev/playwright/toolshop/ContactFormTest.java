package com.yehorychev.playwright.toolshop;

import com.microsoft.playwright.options.AriaRole;
import com.yehorychev.playwright.pages.ContactForm;
import com.yehorychev.playwright.pages.NavBar;
import com.yehorychev.playwright.toolshop.fixtures.BaseToolShopTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Feature("Contact form")
public class ContactFormTest extends BaseToolShopTest {

    ContactForm contactForm;
    NavBar navigate;

    @BeforeEach
    void openContactPage() {
        contactForm = new ContactForm(page);
        navigate = new NavBar(page);
        navigate.toTheContactPage();
    }

    @DisplayName("Customers can use the contact form to contact us")
    @Story("Fill the whole form on the contact us screen")
    @Test
    void completeForm() throws URISyntaxException {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        contactForm.setAttachment(fileToUpload);

        contactForm.submitForm();

        Assertions.assertThat(contactForm.getAlertMessage())
                .contains("Thanks for your message! We will contact you shortly.");
    }

    @Story("Mandatory field on the contact us screen")
    @DisplayName("First name, last name, email and message are mandatory")
    @ParameterizedTest(name = "{arguments} is a mandatory field")
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName) {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A long message to trigger validation.");
        contactForm.selectSubject("Warranty");

        page.getByLabel(fieldName).clear();

        page.getByText("Send").click();

        assertThat(page.getByRole(AriaRole.ALERT)).isVisible();

        assertThat(page.getByRole(AriaRole.ALERT)
                .getByText(fieldName + " is required"))
                .isVisible();
    }

    @Story("Short message on the contact us screen")
    @DisplayName("The message must be at least 50 characters long")
    @Test
    void messageTooShort() {

        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A short long message.");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();

        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Message must be minimal 50 characters");
    }

    @Story("Invalid email on the contact us screen")
    @DisplayName("The email address must be correctly formatted")
    @ParameterizedTest(name = "'{arguments}' should be rejected")
    @ValueSource(strings = {"not-an-email", "not-an.email.com", "notanemail"})
    void invalidEmailField(String invalidEmail) {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail(invalidEmail);
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();

        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Email format is invalid");
    }
}