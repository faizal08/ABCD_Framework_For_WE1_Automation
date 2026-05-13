package com.eit.automation.actions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.HidesKeyboard;
import java.time.Duration;

public class MobileActions {

    private AppiumDriver driver;
    private WebDriverWait wait;

    public MobileActions(AppiumDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * Standard click for Mobile (handles Accessibility IDs or XPaths)
     */
    public void tap(String locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(parseLocator(locator)));
        element.click();
    }

    /**
     * Swipe logic: Up, Down, Left, Right
     */
    public void swipe(String direction) {
        Dimension size = driver.manage().window().getSize();
        int startX, startY, endX, endY;

        switch (direction.toLowerCase()) {
            case "up":
                startX = size.width / 2;
                startY = (int) (size.height * 0.8);
                endX = size.width / 2;
                endY = (int) (size.height * 0.2);
                break;
            case "down":
                startX = size.width / 2;
                startY = (int) (size.height * 0.2);
                endX = size.width / 2;
                endY = (int) (size.height * 0.8);
                break;
            default:
                return;
        }

        new TouchAction((PerformsTouchActions) driver)
                .press(PointOption.point(startX, startY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
                .moveTo(PointOption.point(endX, endY))
                .release()
                .perform();
    }

    /**
     * Hide keyboard - essential for mobile forms
     */
    public void hideKeyboard() {
        try {
            if (driver instanceof HidesKeyboard) {
                ((HidesKeyboard) driver).hideKeyboard();
            }
        } catch (Exception e) {
            // Ignore if keyboard is already hidden or not supported
        }
    }

    /**
     * Helper to distinguish between XPath and Mobile Accessibility IDs
     */
    private By parseLocator(String locator) {
        if (locator.startsWith("//") || locator.startsWith("(//")) {
            return By.xpath(locator);
        }
        // Default to Accessibility ID for mobile-friendly strings
        return By.id(locator);
    }
}
