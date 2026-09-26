package task_6;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests {

    private static Playwright playwright;

    @BeforeAll
    static void setup() {
        playwright = Playwright.create();
    }

    @ParameterizedTest
    @ValueSource(strings = {"chromium", "firefox", "webkit"})
    void testLoginPage(String browserType) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType type = switch (browserType) {
                case "chromium" -> playwright.chromium();
                case "firefox" -> playwright.firefox();
                case "webkit" -> playwright.webkit();
                default -> throw new IllegalArgumentException("Unknown browser: " + browserType);
            };

            try (Browser browser = type.launch(
                    new BrowserType.LaunchOptions().setHeadless(true))) {

                BrowserContext context = browser.newContext();
                Page page = context.newPage();

                page.navigate("https://the-internet.herokuapp.com/login");
                assertEquals("The Internet", page.title());

                context.close();
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"chromium", "firefox", "webkit"})
    void testAddRemoveElements(String browserType) {
        try (Playwright playwright = Playwright.create()) {
            BrowserType type = switch (browserType) {
                case "chromium" -> playwright.chromium();
                case "firefox" -> playwright.firefox();
                case "webkit" -> playwright.webkit();
                default -> throw new IllegalArgumentException("Unknown browser: " + browserType);
            };

            try (Browser browser = type.launch(
                    new BrowserType.LaunchOptions().setHeadless(true))) {

                BrowserContext context = browser.newContext();
                Page page = context.newPage();

                page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
                page.click("button:text('Add Element')");
                assertTrue(page.isVisible("button.added-manually"));

                context.close();
            }
        }
    }

    @AfterAll
    static void teardown() {
        playwright.close();
    }
}
