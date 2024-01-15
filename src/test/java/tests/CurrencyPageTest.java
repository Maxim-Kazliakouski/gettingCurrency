package tests;

import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CurrencyPageTest extends BaseTest {
    @DataProvider(name = "testData")
    public Object[][] testData() {
        return new Object[][]{
                {"USD", "sell"},
                {"RUB", "buy"}
        };
    }

    @Test(dataProvider = "testData")
    @Description("Test for getting currency")
    public void gettingCurrency(String cur, String action) {
        currencyPage
                .openPage()
//                .isOpened()
                .gettingCurrencySell(cur, action);
    }
}
