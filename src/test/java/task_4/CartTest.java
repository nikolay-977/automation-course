package task_4;

import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTest {

    private Page page;
    private BrowserContext context;
    private Browser browser;
    private Playwright playwright;

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get("videos"));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/")));
        page = context.newPage();
    }

    @Test
    void testCartActionsSuccess() {
        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page.click("button[onclick='addElement()']");
        page.click("button.added-manually");

        int count = page.locator("button.added-manually").count();
        assertTrue(count == 0, "Ожидается 0 кнопок");
    }

    @Test
    void testCartActionsFailure() {
        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");
        page.click("button[onclick='addElement()']");
        page.click("button.added-manually");

        int count = page.locator("button.added-manually").count();
        assertTrue(count == 1, "Ожидается 1 кнопка (специально неверно для демо)");
    }

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext ctx, Throwable cause) {
            try {
                if (page != null && !page.isClosed()) {
                    byte[] shot = page.screenshot(
                            new Page.ScreenshotOptions().setFullPage(true));
                    Allure.addAttachment("Screenshot on Failure", "image/png",
                            new ByteArrayInputStream(shot), ".png");
                }
            } finally {
                closeAll();
            }
        }

        @Override
        public void testSuccessful(ExtensionContext ctx) {
            closeAll();
        }

        private void closeAll() {
            if (context != null) context.close();
            if (browser != null) browser.close();
            if (playwright != null) playwright.close();
        }
    };
}