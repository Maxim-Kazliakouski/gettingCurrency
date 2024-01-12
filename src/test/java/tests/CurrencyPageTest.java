package tests;

import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class CurrencyPageTest extends BaseTest {
    @Test
    @Description("Test for getting currency")
    public void gettingCurrency() {
        currencyPage
                .openPage()
                .isOpened()
                .gettingCurrencySell("USD");
    }
}
