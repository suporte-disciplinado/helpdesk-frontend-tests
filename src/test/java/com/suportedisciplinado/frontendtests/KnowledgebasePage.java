package com.suportedisciplinado.frontendtests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

// page_url = about:blank
public class KnowledgebasePage {
    public KnowledgebasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
}