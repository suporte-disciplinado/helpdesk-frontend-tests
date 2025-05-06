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

    WebDriver webDriver;
    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
        webDriver.get("http://localhost:5173/login");

        loginPage = new LoginPage(webDriver);
    }

    @Test
    public void testLoginSuccess() {
        loginPage.inputLoginEmail.sendKeys("admin@gmail.com");
        loginPage.inputLoginPassword.sendKeys("1234");
        loginPage.buttonLogin.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/tickets"));

        String urlAfterLogin = webDriver.getCurrentUrl();
        assertEquals("http://localhost:5173/tickets", urlAfterLogin);
    }

    @Test
    public void testLoginFailure() {
        loginPage.inputLoginEmail.sendKeys("admin@gmail123.com");
        loginPage.inputLoginPassword.sendKeys("123");
        loginPage.buttonLogin.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        WebElement msgErrorLogin = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Erro. Credenciais Inválidas!')]"))
        );

        assertNotNull(msgErrorLogin);
        assertEquals("Erro. Credenciais Inválidas!", msgErrorLogin.getText());
    }

    @Test
    public void testLogoutSuccess() {
        loginPage.inputLoginEmail.sendKeys("admin@gmail.com");
        loginPage.inputLoginPassword.sendKeys("1234");
        loginPage.buttonLogin.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/tickets"));

        WebElement buttonLogout = webDriver.findElement(By.partialLinkText("Sair"));
        buttonLogout.click();

        String urlAfterLogin = webDriver.getCurrentUrl();
        assertEquals("http://localhost:5173/login", urlAfterLogin);
    }

    @AfterEach
    void tearDown() {
        webDriver.quit();
    }
}