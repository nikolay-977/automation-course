package task_10;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicLoadingTraceTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @Test
    void testDynamicLoadingWithTrace() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();

        // Настройка трассировки: скриншоты + снапшоты DOM
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true));

        page = context.newPage();

        // Шаги теста
        page.navigate("https://the-internet.herokuapp.com/dynamic_loading/1");
        page.click("button"); // Клик на "Start"

        // Ожидание появления текста
        page.locator("#finish").waitFor();
        assertEquals("Hello World!", page.locator("#finish h4").textContent());

        // Сохранение трассировки
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace-dynamic-loading.zip")));
    }

    @AfterEach
    void tearDown() {
        context.close();
        browser.close();
        playwright.close();
    }
}
