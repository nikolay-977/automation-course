package task_12;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TodoApiTest {
    Playwright playwright;
    APIRequestContext requestContext;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        requestContext = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://jsonplaceholder.typicode.com")
        );
    }

    @Test
    void testTodoApi() throws Exception {

        // 1. Выполнение GET-запроса напрямую через API
        APIResponse response = requestContext.get("/todos/1");

        // 2. Проверка статуса
        assertEquals(200, response.status());

        // 3. Парсинг JSON
        String body = response.text();
        JsonNode json = objectMapper.readTree(body);

        // 4. Проверка структуры
        assertTrue(json.has("userId"), "Ответ должен содержать поле userId");
        assertTrue(json.has("id"), "Ответ должен содержать поле id");
        assertTrue(json.has("title"), "Ответ должен содержать поле title");
        assertTrue(json.has("completed"), "Ответ должен содержать поле completed");

        assertEquals(1, json.get("id").asInt());
        assertEquals(1, json.get("userId").asInt());
        assertFalse(json.get("completed").asBoolean());
    }

    @AfterEach
    void tearDown() {
        requestContext.dispose();
        playwright.close();
    }
}