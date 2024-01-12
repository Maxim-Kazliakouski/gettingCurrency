package Steps;

import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import pages.BasePage;
import pages.CurrencyPage;

import static java.lang.String.format;

@Log4j2
public class CurrencyPageSteps {
    BasePage basePage;
    CurrencyPage currencyPage;

    public CurrencyPageSteps() {
        basePage = new BasePage();
        currencyPage = new CurrencyPage();
    }

    @Step("Open currency page")
    public CurrencyPageSteps openCurrencyPage() {
        log.info("Opening currency page...");
        currencyPage
                .openPage()
                .isOpened();
        log.info("Currency page is opened!");
        return this;
    }

    @Step("Getting currency")
    public CurrencyPageSteps gettingCurrency(String currency) {
        log.info(format("Getting currency '%s'", currency));
        currencyPage
                .gettingCurrencySell(currency);
        return this;
    }
}
