package webeng.contactlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ContactsPageIT {

    @LocalServerPort
    private int port;

    private ContactsPage page;

    @BeforeEach
    public void setUp(){
        HtmlUnitDriver driver = new HtmlUnitDriver();
        this.page = ContactsPage.create(driver,port);
    }

    @Test
    public void contactLinksPresent(){
        assertEquals(30,page.getLinks().size());
    }

    @Test
    public void contactLinkClick(){
        page.getLinks().get(0).click();

        List<WebElement> tables = page.getTables();
        assertFalse(tables.isEmpty());

        WebElement detailsTable = page.detailsTable();
        assertTrue(detailsTable.getText().contains("Librarian"));
    }

    @Test
    public void contactSearchTest(){
        //preparte
        WebElement search = page.getSearchBox();

        //act
        search.sendKeys("cat");
        page.getSubmitSearch().click();

        //assert
        assertEquals(2,page.getLinks().size());

    }

    @Test
    public void contactSearchClearTest(){
        //preparte
        WebElement search = page.getSearchBox();

        search.sendKeys("cat");
        page.getSubmitSearch().click();

        //pre assert
        assertEquals(2,page.getLinks().size());

        //act
        page.getSubmitClear().click();

        assertEquals(30,page.getLinks().size());
    }

}
