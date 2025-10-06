package com.yehorychev.playwright.login;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import com.yehorychev.playwright.domain.User;

public class UserApiClient {
    private final Page page;

    private static final String REGISTER_USER = "https://api.practicesoftwaretesting.com/users/register";

    public UserApiClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {
        var response = page.request().post(
                REGISTER_USER,
                RequestOptions.create()
                        .setData(user)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json"));
        if (response.status() != 201) {
            throw new IllegalStateException("Could not create user: " + response.text());
        }
    }
}