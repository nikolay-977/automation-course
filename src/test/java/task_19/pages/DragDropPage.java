package task_19.pages;

import com.microsoft.playwright.Page;
import task_19.components.DragDropArea;

public class DragDropPage extends BasePage {
    private DragDropArea dragDropArea;

    public DragDropPage(Page page) {
        super(page);
    }

    public DragDropArea dragDropArea() {
        if (dragDropArea == null) {
            dragDropArea = new DragDropArea(page);
        }
        return dragDropArea;
    }

    @Override
    public DragDropPage navigateTo(String url) {
        super.navigateTo(url);
        return this;
    }
}
