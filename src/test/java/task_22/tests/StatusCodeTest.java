package task_22.tests;

import com.microsoft.playwright.*;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import task_22.config.EnvironmentConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusCodeTest {
    private EnvironmentConfig config;
    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    public void setup() {
        System.setProperty("env", System.getProperty("env", "dev"));
        config = ConfigFactory.create(EnvironmentConfig.class, System.getenv());

        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @Test
    public void testStatusCode200() {
        String url = config.baseUrl() + "/status_codes/200";

        Response response = page.waitForResponse(
                r -> r.url().equals(url) && r.request().isNavigationRequest(),
                () -> page.navigate(url)
        );

        assertEquals(200, response.status());
    }

    @Test
    public void testStatusCode404() {
        String url = config.baseUrl() + "/status_codes/404";

        Response response = page.waitForResponse(
                r -> r.url().equals(url) && r.request().isNavigationRequest(),
                () -> page.navigate(url)
        );

        assertEquals(404, response.status());
    }

    @AfterEach
    public void teardown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}