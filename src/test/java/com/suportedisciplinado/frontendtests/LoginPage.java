package com.suportedisciplinado.frontendtests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = http://localhost:5173/login
public class LoginPage {
    @FindBy(name = "email")
    public WebElement inputLoginEmail;

    @FindBy(name = "password")
    public WebElement inputLoginPassword;

    @FindBy(xpath = "//*[text() = 'Entrar']")
    public WebElement buttonLogin;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}