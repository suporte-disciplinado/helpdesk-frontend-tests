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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsersPageTest {

    private static final String BASE_URL = "http://localhost:5173";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String TICKETS_URL = BASE_URL + "/tickets";
    private static final String USERS_URL = BASE_URL + "/users";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";
    private static final String NEW_USER_NAME = "User Admin 02";
    private static final String NEW_USER_EMAIL = "admin2@gmail.com";
    private static final String NEW_USER_ROLE = "ADMIN";
    private static final String NEW_USER_PASSWORD = "1234";

    private static final String SUCCESS_MESSAGE = "Usuário cadastrado com sucesso!";

    WebDriver webDriver;
    UsersPage usersPage;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, TIMEOUT);
        
        webDriver.get(LOGIN_URL);
        usersPage = new UsersPage(webDriver);
    }

    private void loginAdmin() {
        usersPage.inputLoginEmail.sendKeys(ADMIN_EMAIL);
        usersPage.inputLoginPassword.sendKeys(ADMIN_PASSWORD);
        usersPage.buttonLogin.click();
        wait.until(ExpectedConditions.urlToBe(TICKETS_URL));
    }

    private void createNewUser(String name, String email, String role, String password) {
        webDriver.get(USERS_URL);
        
        WebElement buttonNewUser = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Novo Usuário']"))
        );
        buttonNewUser.click();

        wait.until(ExpectedConditions.visibilityOf(usersPage.inputName));

        usersPage.inputName.sendKeys(name);
        usersPage.inputEmail.sendKeys(email);
        usersPage.inputRole.sendKeys(role);
        usersPage.inputPassword.sendKeys(password);
        usersPage.buttonConfirmar.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), '" + SUCCESS_MESSAGE + "')]")
        ));
    }

    private boolean deleteUserByEmail(String email) {
        webDriver.get(USERS_URL);
        
        List<WebElement> userRows = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[role='row']"), 1
                )
        );
        
        List<WebElement> dataRows = userRows.subList(1, userRows.size());

        for (WebElement row : dataRows) {
            if (row.getText().contains(email)) {
                WebElement buttonDeleteUser = row.findElement(By.id("btDeleteUser"));
                buttonDeleteUser.click();

                WebElement buttonConfirmDelete = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//*[text() = 'Confimar']"))
                );
                buttonConfirmDelete.click();

                wait.until(ExpectedConditions.invisibilityOf(buttonConfirmDelete));
                return true;
            }
        }
        return false;
    }

    @Test
    void testNewUser() {
        loginAdmin();
        createNewUser(NEW_USER_NAME, NEW_USER_EMAIL, NEW_USER_ROLE, NEW_USER_PASSWORD);
        assertEquals(USERS_URL, webDriver.getCurrentUrl());
    }

    @Test
    void testDeleteUser() {
        loginAdmin();
        createNewUser(NEW_USER_NAME, NEW_USER_EMAIL, NEW_USER_ROLE, NEW_USER_PASSWORD);

        boolean userDeleted = deleteUserByEmail(NEW_USER_EMAIL);
        assertTrue(userDeleted, "Deletado");
    }

    @Test
    void testListUsers() {
        loginAdmin();
        webDriver.get(USERS_URL);

        List<WebElement> userRows = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[role='row']"), 1
                )
        );

        List<WebElement> dataRows = userRows.subList(1, userRows.size());
        assertFalse(dataRows.isEmpty());
        
        for (WebElement row : dataRows) {
            assertTrue(row.isDisplayed());
            assertTrue(row.getText().contains("@"));
        }
    }

    @AfterEach
    void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
