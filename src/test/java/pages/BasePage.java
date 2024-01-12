package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Alert;
import utils.PropertyReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.*;
//import static constants.SignUpLoginPageLocators.ERROR_NOTIFICATION;
import static java.lang.String.format;

@Log4j2
public class BasePage {

    public void acceptBrowserNotification() {
        Alert alert = switchTo().alert();
        alert.accept();
    }

    public void scrollToTheEndOfThePage() {
        executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToTheCertainElement(SelenideElement element) {
        executeJavaScript("arguments[0].scrollIntoView(true);", element);
    }

    public void jsClick(SelenideElement element) {
        executeJavaScript("arguments[0].click();", element);
    }


    public void clickOnButton(SelenideElement element) {
        element.shouldBe(Condition.visible).click();
    }
}