package task_8;

import com.microsoft.playwright.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Epic("Веб-интерфейс тестов")
@Feature("Операции с чекбоксами")
public class CheckboxTest {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    @Step("Инициализация браузера и контекста")
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    @Story("Проверка работы чекбоксов")
    @DisplayName("Тестирование выбора/снятия чекбоксов")
    @Severity(SeverityLevel.CRITICAL)
    void testCheckboxes() {
        navigateToCheckboxesPage();
        verifyInitialState();
        toggleCheckboxes();
        verifyToggledState();
    }

    @Step("Переход на страницу /checkboxes")
    private void navigateToCheckboxesPage() {
        page.navigate("https://the-internet.herokuapp.com/checkboxes");
        page.waitForLoadState();
    }

    @Step("Проверка начального состояния чекбоксов")
    private void verifyInitialState() {
        Locator checkboxes = page.locator("input[type='checkbox']");

        // На этой странице всегда 2 чекбокса
        assertThat(checkboxes).hasCount(2);

        // По умолчанию: первый снят, второй установлен
        assertThat(checkboxes.nth(0)).not().isChecked();
        assertThat(checkboxes.nth(1)).isChecked();
    }

    @Step("Изменение состояния чекбоксов")
    private void toggleCheckboxes() {
        Locator checkboxes = page.locator("input[type='checkbox']");

        // Кликаем по обоим — состояния инвертируются
        checkboxes.nth(0).click();
        checkboxes.nth(1).click();
    }

    @Step("Проверка состояния чекбоксов")
    private void verifyToggledState() {
        Locator checkboxes = page.locator("input[type='checkbox']");

        // После кликов: первый установлен, второй снят
        assertThat(checkboxes.nth(0)).isChecked();
        assertThat(checkboxes.nth(1)).not().isChecked();
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

        @Step("Закрытие ресурсов")
        private void closeAll() {
            if (context != null) context.close();
            if (browser != null) browser.close();
            if (playwright != null) playwright.close();
        }
    };
}
