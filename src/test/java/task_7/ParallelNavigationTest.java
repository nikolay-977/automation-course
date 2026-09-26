package task_7;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
public class ParallelNavigationTest {

    private static final String BASE_URL = "https://the-internet.herokuapp.com";

    private Playwright playwright;
    private Browser browser;

    @BeforeEach
    void setup(TestInfo testInfo) {
        // Определяем браузер из имени теста: "[1] chromium -> /login"
        String displayName = testInfo.getDisplayName();
        String browserName = displayName.contains("firefox") ? "firefox" : "chromium";

        playwright = Playwright.create();
        browser = "firefox".equals(browserName)
                ? playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true))
                : playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterEach
    void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @CsvSource({
            "chromium, /",
            "chromium, /login",
            "chromium, /dropdown",
            "chromium, /javascript_alerts",
            "chromium, /checkboxes",
            "chromium, /hovers",
            "chromium, /status_codes",
            "firefox,  /",
            "firefox,  /login",
            "firefox,  /dropdown",
            "firefox,  /javascript_alerts",
            "firefox,  /checkboxes",
            "firefox,  /hovers",
            "firefox,  /status_codes"
    })
    void testPageLoad(String browserName, String path) {
        // browser уже создан в @BeforeEach — используем его
        try (BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            Response response = page.navigate(BASE_URL + path);

            assertEquals(response.status(), 200);

            assertThat(page).hasTitle(Pattern.compile(".+"));
        }
    }
}