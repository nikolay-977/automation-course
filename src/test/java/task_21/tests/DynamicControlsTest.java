package task_21.tests;

import org.junit.jupiter.api.*;
import task_21.context.TestContext;
import task_21.pages.DynamicControlsPage;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DynamicControlsTest {
    private TestContext context;
    private DynamicControlsPage controlsPage;

    @BeforeEach
    public void setup() {
        // Ручное внедрение зависимостей (DI)
        context = new TestContext();
        controlsPage = new DynamicControlsPage(context.getPage()).navigate();
    }

    @Test
    public void testCheckboxRemoval() {
        controlsPage.clickRemoveButton().waitForCheckboxDetached();
        assertFalse(controlsPage.isCheckboxVisible());
    }

    @AfterEach
    public void teardown() {
        context.close();
    }
}
