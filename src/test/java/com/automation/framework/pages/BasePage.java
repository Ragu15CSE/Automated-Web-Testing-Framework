package com.automation.framework.pages;

import com.automation.framework.controls.CommonControls;
import com.automation.framework.library.LoggerUtil;

/**
 * BasePage serves as the parent class for all Page Object classes.
 * Inherits all common controls and exposes page-level convenience methods.
 */
public abstract class BasePage extends CommonControls {

    public BasePage() {
        super();
    }

    /**
     * Get the title of the current page.
     *
     * @return Page Title
     */
    public String getPageTitle() {
        String title = getDriver().getTitle();
        LoggerUtil.info("Current Page Title: " + title);
        return title;
    }

    /**
     * Get the current URL of the page.
     *
     * @return Current Page URL
     */
    public String getCurrentUrl() {
        String url = getDriver().getCurrentUrl();
        LoggerUtil.info("Current Page URL: " + url);
        return url;
    }
}
