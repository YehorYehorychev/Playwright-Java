package com.yehorychev.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        void completeFormTest(Page page) throws URISyntaxException {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email address");
            var messageField = page.getByLabel("Message *");
            var subjectSelector = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            Path fileToUpload = Paths.get(ClassLoader
                    .getSystemResource("data/test_data_for_upload_file_test.txt").toURI());

            page.setInputFiles("#attachment", fileToUpload);

            firstNameField.fill("Yehor");
            lastNameField.fill("Yehorychev");
            emailField.fill("yehor@example.com");
            messageField.fill("Hello World!");
            subjectSelector.selectOption("payments");

            assertThat(firstNameField).hasValue("Yehor");
            assertThat(lastNameField).hasValue("Yehorychev");
            assertThat(emailField).hasValue("yehor@example.com");
            assertThat(messageField).hasValue("Hello World!");
            assertThat(subjectSelector).hasValue("payments");

            String uploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadedFile)
                    .endsWith("upload_file_test.txt");
        }
    }
}