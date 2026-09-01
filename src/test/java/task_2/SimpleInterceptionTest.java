package task_2;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

public class SimpleInterceptionTest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeEach
    void setup() {
        context = browser.newContext();
        page = context.newPage();
    }

    @Test
    void simpleInterceptionTest() {
        // 1. Настраиваем перехват
        page.route("**/authenticate", route -> {
            System.out.println("Запрос перехвачен!");

            // Получаем оригинальные данные
            String originalPostData = route.request().postData();

            // Меняем username
            String modifiedPostData = originalPostData.replace("tomsmith", "HACKED_USER");

            // Создаем ResumeOptions с новыми данными
            Route.ResumeOptions options = new Route.ResumeOptions()
                    .setPostData(modifiedPostData.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Отправляем измененный запрос
            route.resume(options);
        });

        // 2. Переходим на страницу
        page.navigate("https://the-internet.herokuapp.com/login");

        // 3. Заполняем форму
        page.fill("#username", "tomsmith");
        page.fill("#password", "SuperSecretPassword!");

        // 4. Нажимаем кнопку
        page.click("button[type='submit']");

        // 5. Ждем и проверяем результат
        page.waitForTimeout(2000);

        String flashMessage = page.textContent("#flash");
        Assertions.assertTrue(flashMessage.contains("invalid"),
                "Должно появиться сообщение об ошибке, так как username был изменен");
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @AfterEach
    void tearDown() {
        context.close();
    }
}