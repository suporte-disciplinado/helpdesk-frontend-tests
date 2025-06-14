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

class RegisterPageTest {

    private static final String BASE_URL = "http://localhost:5173";
    private static final String REGISTER_URL = BASE_URL + "/register";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final String SUCCESS_MESSAGE = "Sucesso: Usuário cadastrado com sucesso!";
    private static final String EMAIL_EXISTS_MESSAGE = "Email already exists";

    WebDriver webDriver;
    RegisterPage registerPage;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
        webDriver.get(REGISTER_URL);
        
        registerPage = new RegisterPage(webDriver);
        wait = new WebDriverWait(webDriver, TIMEOUT);
    }

    @Test
    void testRegisterSuccess() {
        String name = "João da Silva";
        String email = "joao.silva" + System.currentTimeMillis() + "@gmail.com";
        String password = "1234";

        registerPage.inputName.sendKeys(name);
        registerPage.inputEmail.sendKeys(email);
        registerPage.inputPassword.sendKeys(password);
        registerPage.buttonRegistrar.click();

        WebElement msgSuccessRegister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + SUCCESS_MESSAGE + "')]"))
        );

        assertNotNull(msgSuccessRegister);
        assertEquals(SUCCESS_MESSAGE, msgSuccessRegister.getText());
    }

    @Test
    void testRegisterFailEmailExists() {
        String name = "Administrador";
        String email = "admin@gmail.com";
        String password = "1234";

        registerPage.inputName.sendKeys(name);
        registerPage.inputEmail.sendKeys(email);
        registerPage.inputPassword.sendKeys(password);
        registerPage.buttonRegistrar.click();

        WebElement msgErrorRegister = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + EMAIL_EXISTS_MESSAGE + "')]"))
        );

        assertNotNull(msgErrorRegister);
        assertEquals(EMAIL_EXISTS_MESSAGE, msgErrorRegister.getText());
    }

    @Test
    void testRegisterFailEmptyFields() {
        registerPage.buttonRegistrar.click();

        assertTrue(webDriver.getCurrentUrl().contains("/register"));
    }

    @AfterEach
    void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}