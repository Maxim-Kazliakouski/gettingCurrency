package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.io.FileWriter;
import java.io.IOException;

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

    public void gettingCurrencySell(String currency, String action) {
        SelenideElement rateForecast = null;
        String cur = null;
        if (action.equals("sell")) {
            cur = $x(format(GETTING_CURRENCY_VALUE_SELL, currency)).shouldBe(Condition.visible).getText();
            rateForecast = $x(format(RATE_FORECAST_SELL, currency)).shouldBe(Condition.visible);
        } else if (action.equals("buy")) {
            cur = $x(format(GETTING_CURRENCY_VALUE_BUY, currency)).shouldBe(Condition.visible).getText();
            rateForecast = $x(format(RATE_FORECAST_BUY, currency)).shouldBe(Condition.visible);
        }
        assert rateForecast != null;
        String forecastPositiveOrNegative = rateForecast.getAttribute("class");
        assert forecastPositiveOrNegative != null;
        String currencyText;
        currencyText = cur;
        String forecast;
        if (forecastPositiveOrNegative.contains("positive")) {
//            forecast = (format("%s will grow%n", currency));
            forecast = (format("%s will grow", currency));
        } else {
//            forecast = (format("%s will fall%n", currency));
            forecast = (format("%s will fall", currency));
        }
        try {
            FileWriter writer = new FileWriter("currency.txt", true);
//            writer.write("\"" + currency + " --> " + currencyText + forecast + "\"");
            writer.write(currency + " --> " + currencyText + forecast +"%0A");
//            writer.write(currency + "_" + currencyText);
//            writer.write(forecast);
//            writer.write("-------------------" + "\n");
            writer.close();
            System.out.println("Запись в файл выполнена успешно.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
            e.printStackTrace();
        }
    }
}
