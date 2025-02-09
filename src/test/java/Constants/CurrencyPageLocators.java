package Constants;

public class CurrencyPageLocators {
//    public static final String CURRENCY_PAGE_HEADER = "//h1[contains(text(),'Курсы валют в цифровом банке')]";
    public static final String CURRENCY_PAGE_HEADER = "//h1[contains(text(), ' Курс евро в банках ')]";
//    public static final String GETTING_CURRENCY_VALUE_SELL = "(//h4//span[contains(text(),'(%s)')]//..//..//div[contains(@class,'price__value')]//span)[3]";
    public static final String GETTING_CURRENCY_VALUE_SELL = "(//div[@data-popover-selector='currency%s']//..//span[@class='accent'])[1]";
//    public static final String GETTING_CURRENCY_VALUE_BUY = "(//h4//span[contains(text(),'(%s)')]//..//..//div[contains(@class,'price__value')]//span)[1]";
    public static final String GETTING_CURRENCY_VALUE_BUY = "(//div[@data-popover-selector='currency%s']//..//span[@class='accent'])[2]";
//    public static final String RATE_FORECAST_SELL = "(//h4//span[contains(text(),'(%s)')]//..//..//span[contains(@class,'price__icon')])[2]";
//    public static final String RATE_FORECAST_BUY = "(//h4//span[contains(text(),'(%s)')]//..//..//span[contains(@class,'price__icon')])[1]";
}
