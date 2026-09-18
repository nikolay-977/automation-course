package task_5;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testHomePageVisual() throws IOException {
        page.navigate("https://the-internet.herokuapp.com");
        Path actual = getTimestampPath("actual.png");
        page.screenshot(new Page.ScreenshotOptions().setPath(actual));

        long mismatch = Files.mismatch(actual, Paths.get("screenshots/expected/expected.png"));
        assertThat(mismatch).isEqualTo(-1); // -1 = файлы идентичны
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