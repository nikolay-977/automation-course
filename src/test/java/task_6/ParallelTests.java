package task_6;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelTests {

    private static final String BASE_URL = "https://the-internet.herokuapp.com";

    private Playwright playwright;
    private Browser browser;

    @BeforeEach
    void setup(TestInfo testInfo) {
        String displayName = testInfo.getDisplayName();
        String browserName = displayName.contains("firefox") ? "firefox"
                : displayName.contains("webkit") ? "webkit" : "chromium";

        playwright = Playwright.create();
        browser = switch (browserName) {
            case "firefox" -> playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
            case "webkit" -> playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(true));
            default -> playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        };
    }

    @AfterEach
    void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @ParameterizedTest(name = "[{index}] {0} -> /login")
    @CsvSource({
            "chromium",
            "firefox",
            "webkit"
    })
    void testLoginPage(String browserName) {
        try (BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            page.navigate(BASE_URL + "/login");
            assertEquals("The Internet", page.title());
        }
    }

    @ParameterizedTest(name = "[{index}] {0} -> /add_remove_elements")
    @CsvSource({
            "chromium",
            "firefox",
            "webkit"
    })
    void testAddRemoveElements(String browserName) {
        try (BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            page.navigate(BASE_URL + "/add_remove_elements/");
            page.click("button:text('Add Element')");
            assertTrue(page.isVisible("button.added-manually"));
        }
    }
}
