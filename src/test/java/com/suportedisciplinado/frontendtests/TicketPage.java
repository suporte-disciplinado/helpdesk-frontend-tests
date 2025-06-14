package com.suportedisciplinado.frontendtests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// page_url = http://localhost:5173/ticket
public class TicketPage {
    @FindBy(name = "title")
    public WebElement inputTitle;

    @FindBy(id = "category")
    public WebElement inputCategory;

    @FindBy(name = "description")
    public WebElement inputDescription;

    @FindBy(name = "priority")
    public WebElement inputPriority;

    @FindBy(name = "status")
    public WebElement inputStatus;

    @FindBy(id = "assignedAgent")
    public WebElement inputAssignedAgent;

    @FindBy(xpath = "//*[text() = 'Salvar']")
    public WebElement buttonSalvar;

    @FindBy(id = "newComment")
    public WebElement inputNewComment;

    @FindBy(id = "sendComment")
    public WebElement buttonSendComment;

    public TicketPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}