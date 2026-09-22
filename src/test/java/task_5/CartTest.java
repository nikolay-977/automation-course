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
    void testHomePageVisual() throws IOException, InterruptedException {
        page.navigate("https://the-internet.herokuapp.com");
        Path actual = getTimestampPath("actual.png");
        page.screenshot(new Page.ScreenshotOptions().setPath(actual));

        Path expected = Paths.get("screenshots/expected/expected.png");

        // Если эталона нет — сохраняем текущий скриншот как эталон
        if (!Files.exists(expected)) {
            Files.createDirectories(expected.getParent());
            Files.copy(actual, expected);
            return;
        }

        Path diff = getTimestampPath("diff.png");

        long diffPixelCount = ImageComparison.compare(actual, expected, diff);

        assertThat(diffPixelCount).isLessThan(10); // Допуск 10 пикселей
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