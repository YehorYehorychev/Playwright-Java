package com.yehorychev.playwright.login;

import com.yehorychev.playwright.domain.User;
import com.yehorychev.playwright.helpers.PlaywrightTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LoginWithRegisteredUserTest extends PlaywrightTestCase {

    @Test
    @DisplayName("Should be able to login with a registered user")
    void shouldLoginWithRegisteredUser() {
        User user = User.randomUser();

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);

        assertThat(loginPage.title()).isEqualTo("My account");
    }
}
