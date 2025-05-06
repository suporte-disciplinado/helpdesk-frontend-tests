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

    WebDriver webDriver;
    UsersPage usersPage;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        webDriver.get("http://localhost:5173/login");

        usersPage = new UsersPage(webDriver);
    }

    void loginAdmin() {
        usersPage.inputLoginEmail.sendKeys("admin@gmail.com");
        usersPage.inputLoginPassword.sendKeys("1234");
        usersPage.buttonLogin.click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/tickets"));
    }

    @Test
    public void testNewUser() throws InterruptedException {
        loginAdmin();
        webDriver.get("http://localhost:5173/users");

        WebElement buttonNewUser = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Novo Usuário']"))
        );
        buttonNewUser.click();

        Thread.sleep(1000);

        usersPage.inputName.sendKeys("User Admin 02");
        usersPage.inputEmail.sendKeys("admin2@gmail.com");
        usersPage.inputRole.sendKeys("ADMIN");
        usersPage.inputPassword.sendKeys("1234");
        usersPage.buttonConfirmar.click();

        wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        WebElement msgSuccessAddUser = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Usuário cadastrado com sucesso!')]"))
        );

        assertNotNull(msgSuccessAddUser);
        assertEquals("Usuário cadastrado com sucesso!", msgSuccessAddUser.getText());

        String urlAfterLogin = webDriver.getCurrentUrl();
        assertEquals("http://localhost:5173/users", urlAfterLogin);
    }

    @Test
    public void testDeleteUser() throws InterruptedException {
        loginAdmin();
        webDriver.get("http://localhost:5173/users");

        wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        List<WebElement> userRows = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[role='row']"), 1
                )
        );
        
        List<WebElement> dataRows = userRows.subList(1, userRows.size());

        boolean searchUserDelete = false;
        for (WebElement row : dataRows) {
            if (row.getText().contains("admin2@gmail.com")) {
                WebElement buttonDeleteUser = row.findElement(By.id("btDeleteUser"));
                buttonDeleteUser.click();

                WebElement buttonConfirmDelete = wait.until(
                        ExpectedConditions.elementToBeClickable(By.xpath("//*[text() = 'Confimar']"))
                );
                buttonConfirmDelete.click();

                searchUserDelete = true;
                break;
            }
        }

        assertTrue(searchUserDelete);
    }

    @Test
    public void testListUsers() throws InterruptedException {
        loginAdmin();
        webDriver.get("http://localhost:5173/users");

        List<WebElement> userRows = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[role='row']"), 1
                )
        );

        List<WebElement> dataRows = userRows.subList(1, userRows.size());
        assertFalse(dataRows.isEmpty());
    }

    @AfterEach
    void tearDown() {
        webDriver.quit();
    }
}
