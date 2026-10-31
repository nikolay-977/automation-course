package task_19.pages;

import com.microsoft.playwright.Page;

public abstract class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public BasePage navigateTo(String url) {
        page.navigate(url);
        return this;
    }
}
