package webeng.contactlist;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ContactsPage {

    public static ContactsPage create(WebDriver driver, int port){
        driver.navigate().to("http://localhost:"+port+"/contacts");
        return PageFactory.initElements(driver, ContactsPage.class);
    }

    @FindBy(css = "#contacts nav a")
    private List<WebElement> links;

    @FindBy(css="#contacts table")
    private List<WebElement> tables;

    @FindBy(css="#searchText")
    private List<WebElement> searchBox;

    @FindBy(css="#subSearch")
    private List<WebElement> submitSearch;

    @FindBy(css="#subClear")
    private List<WebElement> submitClear;

    public List<WebElement> getLinks() {
        return links;
    }

    public List<WebElement> getTables() {
        return tables;
    }

    public WebElement detailsTable(){
        return tables.get(0);
    }

    public WebElement getSearchBox() {
        return searchBox.get(0);
    }

    public WebElement getSubmitSearch() {
        return submitSearch.get(0);
    }

    public WebElement getSubmitClear() {
        return submitClear.get(0);
    }
}
