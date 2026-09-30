package task_15;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FilePayload;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class FileUploadTest {
    Playwright playwright;
    APIRequestContext request;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        request = playwright.request().newContext();
    }

    @Test
    void testFileUploadAndDownload() throws Exception {
        // Генерация тестового PNG-файла в памяти
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] testFileBytes = baos.toByteArray();
        assertTrue(testFileBytes.length > 0, "Тестовый PNG-файл должен быть создан");

        // Загрузка файла через multipart/form-data
        APIResponse uploadResponse = request.post(
                "https://httpbin.org/post",
                RequestOptions.create().setMultipart(
                        FormData.create().set("file", new FilePayload(
                                "test.png", "image/png", testFileBytes))
                )
        );

        // Проверка получения файла сервером
        assertEquals(200, uploadResponse.status());
        String responseBody = uploadResponse.text();
        assertTrue(responseBody.contains("data:image/png;base64"),
                "Ответ должен содержать base64-данные изображения");

        // Верификация содержимого
        String base64Data = responseBody.split("\"file\": \"")[1].split("\"")[0];
        byte[] receivedBytes = Base64.getDecoder().decode(base64Data.split(",")[1]);
        assertArrayEquals(testFileBytes, receivedBytes,
                "Содержимое загруженного файла должно совпадать с исходным");

        // Скачивание эталонного PNG-файла
        APIResponse downloadResponse = request.get("https://httpbin.org/image/png");
        assertEquals(200, downloadResponse.status());

        // Проверка MIME-типа
        String contentType = downloadResponse.headers().get("content-type");
        assertNotNull(contentType);
        assertTrue(contentType.contains("image/png"),
                "MIME-тип должен быть image/png, получен: " + contentType);

        // Проверка сигнатуры PNG
        byte[] content = downloadResponse.body();
        assertTrue(content.length > 8, "Файл должен содержать данные");
        assertEquals(0x89, content[0] & 0xFF);
        assertEquals(0x50, content[1] & 0xFF);
        assertEquals(0x4E, content[2] & 0xFF);
        assertEquals(0x47, content[3] & 0xFF);
    }

    @AfterEach
    void tearDown() {
        try {
            if (request != null) request.dispose();
        } finally {
            if (playwright != null) playwright.close();
        }
    }
}