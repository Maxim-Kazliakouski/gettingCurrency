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
//        System.setProperty("currencyText", currencyText);
        String forecast;
        if (forecastPositiveOrNegative.contains("positive")) {
            forecast = (format("%s будет расти, не стоит покупать%n", currency));
            System.out.printf("%s будет расти, не стоит покупать%n", currency);
        }
        else {
            forecast = (format("%s будет падать, скоро можно будет покупать%n", currency));
            System.out.printf("%s будет падать, скоро можно будет покупать%n", currency);
        }
        try {
            FileWriter writer = new FileWriter("currency.txt");
            writer.write(currency + " --> " + currencyText);
            writer.write(forecast);
            writer.close();
            System.out.println("Запись в файл выполнена успешно.");
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
            e.printStackTrace();
        }
//        System.setProperty("FORECAST", forecast);
//        System.out.println("1234 + " + System.getProperty("currencyText"));
    }
}
