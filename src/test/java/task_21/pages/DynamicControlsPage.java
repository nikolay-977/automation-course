package task_21.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class DynamicControlsPage {
    private final Page page;
    private Locator checkbox;

    public DynamicControlsPage(Page page) {
        this.page = page;
    }

    private Locator checkbox() {
        if (checkbox == null) {
            checkbox = page.locator("#checkbox input[type='checkbox']");
        }
        return checkbox;
    }

    public DynamicControlsPage navigate() {
        page.navigate("https://the-internet.herokuapp.com/dynamic_controls");
        return this;
    }

    public DynamicControlsPage clickRemoveButton() {
        page.locator("#checkbox-example button:has-text('Remove')").click();
        return this;
    }

    public boolean isCheckboxVisible() {
        return checkbox().isVisible();
    }

    public DynamicControlsPage waitForCheckboxDetached() {
        checkbox().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.DETACHED));
        return this;
    }
}