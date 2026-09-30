package task_17;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HoverTest {
    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void testHoverProfiles() {
        page.navigate("https://the-internet.herokuapp.com/hovers");

        Locator figures = page.locator(".figure");
        int count = figures.count();

        for (int i = 0; i < count; i++) {
            Locator figure = figures.nth(i);
            figure.hover();

            // Проверяем, что появилась ссылка "View profile"
            Locator profileLink = figure.locator("text=View profile");
            assertThat(profileLink).isVisible();

            // Кликаем
            profileLink.click();

            // Проверяем, что URL соответствует /users/{id}
            assertThat(page).hasURL(Pattern.compile(".*/users/\\d+"));
            assertTrue(page.url().matches(".*/users/\\d+"),
                    "URL должен содержать /users/{id}, получен: " + page.url());

            // Возвращаемся назад
            page.goBack();
        }
    }

    @AfterEach
    void tearDown() {
        context.close();
    }

    @AfterAll
    static void teardownClass() {
        browser.close();
        playwright.close();
    }
}