package task_3;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CartTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get("videos"));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/")));
        page = context.newPage();
    }

    @Test
    void testCartActions() throws IOException {
        page.navigate("https://the-internet.herokuapp.com/add_remove_elements/");

        // Добавление товара
        page.click("button[onclick='addElement()']");
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(getTimestampPath("cart_after_add.png"))
                .setFullPage(true));

        // Удаление товара
        page.click("button.added-manually");
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(getTimestampPath("cart_after_remove.png"))
                .setFullPage(true));
    }

    private Path getTimestampPath(String filename) throws IOException {
        String folder = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path dir = Paths.get("screenshots", folder);
        Files.createDirectories(dir);
        return dir.resolve(filename);
    }

    @AfterEach
    void teardown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}