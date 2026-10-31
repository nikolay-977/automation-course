package task_19.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task_19.pages.DragDropPage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DragDropTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
    }

    @Test
    void testDragAndDrop() {
        DragDropPage dragDropPage = new DragDropPage(page);
        dragDropPage.navigateTo("https://the-internet.herokuapp.com/drag_and_drop");

        String beforeA = dragDropPage.dragDropArea().getTextA();
        String beforeB = dragDropPage.dragDropArea().getTextB();
        assertEquals("A", beforeA);
        assertEquals("B", beforeB);

        dragDropPage.dragDropArea().dragAToB();

        assertEquals("A", dragDropPage.dragDropArea().getTextB());
    }

    @AfterEach
    void tearDown() {
        browser.close();
        playwright.close();
    }
}
