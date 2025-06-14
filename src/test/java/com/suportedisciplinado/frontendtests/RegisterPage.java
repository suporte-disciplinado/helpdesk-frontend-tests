package com.suportedisciplinado.frontendtests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = http://localhost:5173/register
public class RegisterPage {
    @FindBy(id = "nameInput")
    public WebElement inputName;

    @FindBy(id = "emailInput")
    public WebElement inputEmail;

    @FindBy(id = "passwordInput")
    public WebElement inputPassword;

    @FindBy(xpath = "//*[text() = 'Registrar']")
    public WebElement buttonRegistrar;

    public RegisterPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}