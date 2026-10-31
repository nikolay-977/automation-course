package task_20;

import com.github.javafaker.Faker;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FakerGenerTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();

            // Генерация данных: случайное имя пользователя
            Faker faker = new Faker();
            String fakeUsername = faker.name().username();

            // Мокирование API: перехват запроса dynamic_content и подмена ответа
            page.route("**/dynamic_content", route -> {
                route.fulfill(new Route.FulfillOptions()
                        .setStatus(200)
                        .setContentType("text/html")
                        .setBody("<html><body>" + fakeUsername + "</body></html>"));
            });

            // Запуск теста
            page.navigate("https://the-internet.herokuapp.com/dynamic_content");

            // Проверка, что сгенерированное имя отображается на странице
            assertThat(page.locator("body")).containsText(fakeUsername);

            browser.close();
        }
    }
}
