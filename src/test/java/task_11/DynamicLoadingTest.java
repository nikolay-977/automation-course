package task_11;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicLoadingTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @Test
    void testDynamicLoading() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();

        // Запуск трассировки
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page = context.newPage();
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");

        // Перехват запросов
        page.onResponse(response -> {
            if (response.url().contains("/dynamic_loading")) {
                assertEquals(200, response.status(), "Запрос к /dynamic_loading должен вернуть статус 200");
            }
        });

        page.click("button");
        Locator finishText = page.locator("#finish");
        finishText.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        assertEquals("Hello World!", finishText.textContent().trim());
    }

    @AfterEach
    void tearDown() {
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace/trace-success.zip")));
        page.close();
        browser.close();
        playwright.close();
    }
}
