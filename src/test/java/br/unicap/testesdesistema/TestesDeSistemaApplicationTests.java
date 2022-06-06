package br.unicap.testesdesistema;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestesDeSistemaApplicationTests {

    static final String URL = "https://app.thestorygraph.com/";


    WebDriver driver;


    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get(URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    @DisplayName("Given an URL, when navigate to it, then new URL and given URL should match")
    void Given_URL_When_Navigate_Then_URLShouldMatch() {
        // then
        assertEquals(URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Given a link text, when searching an element, then should not throw error")
    void Given_LinkText_When_SearchingAnElement_Then_ShouldNotThrowError() {
        // given
        final String linkText = "Browse";

        // when + then
        assertDoesNotThrow(() -> driver.findElement(By.linkText(linkText)));
    }

    @Test
    @DisplayName("Given a search text, when submit, then should redirect to results page")
    void Given_SearchText_When_Submit_Then_ShouldRedirectToResultsPage() throws ExecutionException, InterruptedException {
        // given
        final String searchInputId = "search";
        final String searchText = "Harry Potter";
        final String newUrl = URL + "browse?search_term=Harry+Potter";

        // when
        WebElement element = driver.findElement(By.id(searchInputId));
        element.sendKeys(searchText);
        CompletableFuture<Void> async = runAsync(element::submit);

        // then
        async.thenRun(() -> assertEquals(newUrl, driver.getCurrentUrl()));      // done this way because submit() is asynchronolous
    }

    @Test
    @DisplayName("Given a contact us button, when clicked, then get in touch form should appear")
    void Given_ContactUsButton_When_Cliked_Then_GetInTouchFormShouldAppear() {
        // given
        final String contactUsButton = "launcher-button";
        final String getInTouchForm = "main-page";


        runAsync(() -> driver.get(URL)).thenRun(() -> {      // done this because the button only appear when the page is already fully loaded
            // when
            WebElement element = driver.findElement(By.className(contactUsButton));
            element.click();

            // then
            assertDoesNotThrow(() -> driver.findElement(By.className(getInTouchForm)));
            System.out.println("After assert");
        });
    }


}
