package task_21.context;

import com.microsoft.playwright.*;

public class TestContext implements AutoCloseable {
    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext browserContext;
    private final Page page;

    public TestContext() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    public Page getPage() {
        return page;
    }

    @Override
    public void close() {
        browserContext.close();
        browser.close();
        playwright.close();
    }
}
