package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.io.FileWriter;
import java.io.IOException;
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
        String forecast;
        if (forecastPositiveOrNegative.contains("positive")) {
            forecast = (format("%s will grow, not worth buying%n", currency));
            System.out.printf("%s will grow, not worth buying%n", currency);
        }
        else {
            forecast = (format("%s will fall, soon it will be possible to buy%n", currency));
            System.out.printf("%s will fall, soon it will be possible to buy%n", currency);
        }
        try {
            FileWriter writer = new FileWriter("currency.txt");
            writer.write(currency + " --> " + currencyText + "\n");
            writer.write(forecast);
            writer.close();
            System.out.println("Запись в файл выполнена успешно.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
            e.printStackTrace();
        }
    }
}
