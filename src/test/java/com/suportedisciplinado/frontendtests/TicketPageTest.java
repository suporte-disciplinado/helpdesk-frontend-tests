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

class TicketPageTest {

    private static final String BASE_URL = "http://localhost:5173";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String TICKETS_URL = BASE_URL + "/tickets";
    private static final String TICKET_URL = BASE_URL + "/ticket";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "1234";

    private static final String SUCCESS_MESSAGE = "Ticket cadastrado com sucesso!";
    private static final String SUCCESS_ADD_COMMENT = "Comentário adicionado com sucesso!";

    WebDriver webDriver;    
    TicketPage ticketPage;
    LoginPage loginPage;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
        wait = new WebDriverWait(webDriver, TIMEOUT);
        webDriver.get(LOGIN_URL);

        loginPage = new LoginPage(webDriver);
        ticketPage = new TicketPage(webDriver);
    }

    private void loginAdmin() {
        loginPage.inputLoginEmail.sendKeys(ADMIN_EMAIL);
        loginPage.inputLoginPassword.sendKeys(ADMIN_PASSWORD);
        loginPage.buttonLogin.click();
        wait.until(ExpectedConditions.urlToBe(TICKETS_URL));
    }

    private void selectCategory(String categoryName) {
        ticketPage.inputCategory.click();
        ticketPage.inputCategory.clear();
        ticketPage.inputCategory.sendKeys(categoryName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiAutocomplete-popper")));

        WebElement firstOption = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector("li[role='option']:first-child")
            )
        );
        firstOption.click();
    }

    private void selectAssignedAgent(String agentName) {
        ticketPage.inputAssignedAgent.click();
        ticketPage.inputAssignedAgent.clear();
        ticketPage.inputAssignedAgent.sendKeys(agentName);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiAutocomplete-popper")));

        WebElement firstOption = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("li[role='option']:first-child")
                )
        );
        firstOption.click();
    }

    @Test
    void testNewTicket() {
        loginAdmin();
        webDriver.get(TICKETS_URL);

        WebElement buttonNewTicket = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Novo Ticket']"))
        );
        buttonNewTicket.click();

        wait.until(ExpectedConditions.visibilityOf(ticketPage.inputTitle));

        String ticketTitle = "Ticket de Teste " + System.currentTimeMillis();
        String ticketCategory = "Geral";
        String ticketDescription = "Descrição do ticket de teste";
        String ticketPriority = "MEDIUM";
        String ticketStatus = "OPEN";
        String ticketAssignedAgent = "User Admin";

        ticketPage.inputTitle.sendKeys(ticketTitle);
        selectCategory(ticketCategory);
        ticketPage.inputDescription.sendKeys(ticketDescription);
        ticketPage.inputPriority.sendKeys(ticketPriority);
        ticketPage.inputStatus.sendKeys(ticketStatus);
        selectAssignedAgent(ticketAssignedAgent);
        ticketPage.buttonSalvar.click();

        WebElement msgSuccessTicket = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + SUCCESS_MESSAGE + "')]"))
        );

        assertNotNull(msgSuccessTicket);
        assertEquals(SUCCESS_MESSAGE, msgSuccessTicket.getText());
    }

    @Test
    void testAddCommentToFirstTicket() {
        loginAdmin();
        webDriver.get(TICKETS_URL);

        List<WebElement> ticketRows = wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("[role='row']"), 1
                )
        );

        WebElement firstTicketRow = ticketRows.get(1);
        WebElement ticketLink = firstTicketRow.findElement(
                By.cssSelector("div[data-field='id'] a")
        );
        ticketLink.click();

        wait.until(ExpectedConditions.urlContains(TICKET_URL));

        WebElement commentsTab = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'MuiTab-root') and text()='Comentários']")
            )
        );
        commentsTab.click();

        String commentText = "Comentário de teste " + System.currentTimeMillis();
        ticketPage.inputNewComment.sendKeys(commentText);
        ticketPage.buttonSendComment.click();

        WebElement msgSuccessAddComment = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + SUCCESS_ADD_COMMENT + "')]"))
        );

        assertNotNull(msgSuccessAddComment);
        assertEquals(SUCCESS_ADD_COMMENT, msgSuccessAddComment.getText());
    }

    @AfterEach
    void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}