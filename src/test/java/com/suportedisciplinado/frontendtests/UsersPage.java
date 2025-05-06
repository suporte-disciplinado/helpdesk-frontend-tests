package com.suportedisciplinado.frontendtests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

// page_url = http://localhost:5173/users
public class UsersPage {
    @FindBy(name = "email")
    public WebElement inputLoginEmail;

    @FindBy(name = "password")
    public WebElement inputLoginPassword;

    @FindBy(xpath = "//*[text() = 'Entrar']")
    public WebElement buttonLogin;

    @FindBy(name = "name")
    public WebElement inputName;

    @FindBy(name = "email")
    public WebElement inputEmail;

    @FindBy(name = "role")
    public WebElement inputRole;

    @FindBy(name = "password")
    public WebElement inputPassword;

    @FindBy(xpath = "//*[text() = 'Confirmar']")
    public WebElement buttonConfirmar;

    public UsersPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}
