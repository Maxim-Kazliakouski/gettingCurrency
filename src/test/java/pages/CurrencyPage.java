package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.sql.SQLOutput;

import static Constants.CurrencyPageLocators.*;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static java.lang.String.format;

public class CurrencyPage extends BasePage {

    public CurrencyPage openPage() {
        open("/exchange/digital");
        return this;
    }

    public CurrencyPage isOpened() {
        $x(CURRENCY_PAGE_HEADER).shouldBe(Condition.visible);
        return this;
    }

    public void gettingCurrencySell(String currency) {
        SelenideElement cur = $x(format(GETTING_CURRENCY_VALUE_SELL, currency)).shouldBe(Condition.visible);
        SelenideElement rateForecast = $x(format(RATE_FORECAST, currency)).shouldBe(Condition.visible);
        String forecastPositiveOrNegative = rateForecast.getAttribute("class");
        assert forecastPositiveOrNegative != null;
        String currencyText;
        currencyText = cur.getText();
        System.setProperty("currencyText", currencyText);
        String forecast;
        if (forecastPositiveOrNegative.contains("positive")) {
            forecast = (format("%s будет расти, не стоит покупать%n", currency));
            System.out.printf("%s будет расти, не стоит покупать%n", currency);
        }
        else {
            forecast = (format("%s будет падать, скоро можно будет покупать%n", currency));
            System.out.printf("%s будет падать, скоро можно будет покупать%n", currency);
        }
        System.setProperty("FORECAST", forecast);
        System.out.println("123 + " + System.getProperty("currencyText"));
    }
}
