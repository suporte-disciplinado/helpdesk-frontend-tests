package com.suportedisciplinado.frontendtests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class LoginPageTest {

    private static final String BASE_URL = "http://localhost:5173";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String TICKETS_URL = BASE_URL + "/tickets";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final String VALID_EMAIL = "admin@gmail.com";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_EMAIL = "admin@gmail123.com";
    private static final String INVALID_PASSWORD = "123";

    private static final String ERROR_MESSAGE = "Erro. Credenciais Inválidas!";

    WebDriver webDriver;
    LoginPage loginPage;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
        webDriver.get(LOGIN_URL);
        
        loginPage = new LoginPage(webDriver);
        wait = new WebDriverWait(webDriver, TIMEOUT);
    }

    @Test
    void testLoginSuccess() {
        loginPage.inputLoginEmail.sendKeys(VALID_EMAIL);
        loginPage.inputLoginPassword.sendKeys(VALID_PASSWORD);
        loginPage.buttonLogin.click();

        wait.until(ExpectedConditions.urlToBe(TICKETS_URL));
        assertEquals(TICKETS_URL, webDriver.getCurrentUrl());
        
        WebElement userMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Sair")));
        assertNotNull(userMenu);
    }

    @Test
    void testLoginFailure() {
        loginPage.inputLoginEmail.sendKeys(INVALID_EMAIL);
        loginPage.inputLoginPassword.sendKeys(INVALID_PASSWORD);
        loginPage.buttonLogin.click();

        WebElement msgErrorLogin = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + ERROR_MESSAGE + "')]"))
        );

        assertNotNull(msgErrorLogin);
        assertEquals(ERROR_MESSAGE, msgErrorLogin.getText());
        assertEquals(LOGIN_URL, webDriver.getCurrentUrl());
        
        assertTrue(loginPage.inputLoginEmail.getText().isEmpty());
        assertTrue(loginPage.inputLoginPassword.getText().isEmpty());
    }

    @Test
    void testLogoutSuccess() {
        loginPage.inputLoginEmail.sendKeys(VALID_EMAIL);
        loginPage.inputLoginPassword.sendKeys(VALID_PASSWORD);
        loginPage.buttonLogin.click();

        wait.until(ExpectedConditions.urlToBe(TICKETS_URL));

        WebElement buttonLogout = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Sair")));
        buttonLogout.click();

        wait.until(ExpectedConditions.urlToBe(LOGIN_URL));
        assertEquals(LOGIN_URL, webDriver.getCurrentUrl());
        
        assertTrue(loginPage.inputLoginEmail.isDisplayed());
        assertTrue(loginPage.inputLoginPassword.isDisplayed());
    }

    @Test
    void testLoginWithEmptyFields() {
        loginPage.buttonLogin.click();

        assertEquals(LOGIN_URL, webDriver.getCurrentUrl());
    }

    @AfterEach
    void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}