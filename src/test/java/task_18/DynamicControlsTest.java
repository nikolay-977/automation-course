package task_18;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DynamicControlsTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
    }

    @Test
    void testDynamicCheckbox() {

        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");

        // Находим чекбокс с атрибутом type="checkbox"
        Locator checkbox = page.locator("input[type='checkbox']");
        assertThat(checkbox).isVisible();

        // Кликаем на кнопку "Remove"
        page.click("#checkbox-example button:has-text('Remove')");

        // Ожидаем исчезновения чекбокса
        checkbox.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.DETACHED));

        // Проверяем, что появляется текст "It's gone!"
        Locator goneMessage = page.locator("#message");
        assertThat(goneMessage).hasText("It's gone!");

        // Кликаем на кнопку "Add"
        page.click("#checkbox-example button:has-text('Add')");

        // Проверяем, что чекбокс снова отображается
        checkbox.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        assertThat(checkbox).isVisible();
    }

    @AfterEach
    void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }
}