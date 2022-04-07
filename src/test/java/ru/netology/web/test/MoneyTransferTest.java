package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest { // java -jar artifacts/app-ibank-build-for-testers.jar

    private String fromCard;
    private String toCard;

    private DashboardPage loginAndVerify(int idOfCardSender, int idOfCardRecipient) {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        var dashboard = verificationPage.validVerify(verificationCode);
        fromCard = DataHelper.getCardNumbersFor(authInfo).getCardNumbers().get(idOfCardSender - 1);
        toCard = DataHelper.getCardNumbersFor(authInfo).getCardNumbers().get(idOfCardRecipient - 1);
        return dashboard;
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x800";
    }

    @ParameterizedTest
    @CsvSource({
            "If valid amount, 100",
            "If zero amount, 0",
            "If negative amount, -100"
    })
    public void shouldTransferMoneyBetweenOwnCards(String testName, int amount) {
        var dashboard = loginAndVerify(1 , 2);
        var expectedBalanceOfCardFrom = dashboard.getCardBalance(fromCard) - Math.abs(amount);
        var expectedBalanceOfCardTo = dashboard.getCardBalance(toCard) + Math.abs(amount);

        dashboard.moneyTransfer(fromCard, toCard, Integer.toString(amount));
        assertEquals(expectedBalanceOfCardFrom, dashboard.getCardBalance(fromCard));
        assertEquals(expectedBalanceOfCardTo, dashboard.getCardBalance(toCard));
    }

    @Test
    public void shouldTransferMoneyBetweenOwnCardsIfAmountWithKopecks() {
        var amount = 10.10;
        var dashboard = loginAndVerify(1 , 2);
        var expectedBalanceOfCardFrom = dashboard.getCardBalance(fromCard) - amount;
        var expectedBalanceOfCardTo = dashboard.getCardBalance(toCard) + amount;

        dashboard.moneyTransfer(fromCard, toCard, Double.toString(amount));
        assertEquals(expectedBalanceOfCardFrom, dashboard.getCardBalance(fromCard));
        assertEquals(expectedBalanceOfCardTo, dashboard.getCardBalance(toCard));
    }

    @Test
    public void shouldTransferMoneyBetweenOwnCardsWithMaxAmount() {
        var dashboard = loginAndVerify(1 , 2);
        var amount = dashboard.getCardBalance(fromCard);
        double expectedBalanceOfCardFrom = 0;
        double expectedBalanceOfCardTo = dashboard.getCardBalance(toCard) + amount;

        dashboard.moneyTransfer(fromCard, toCard, Integer.toString(amount));
        assertEquals(expectedBalanceOfCardFrom, dashboard.getCardBalance(fromCard));
        assertEquals(expectedBalanceOfCardTo, dashboard.getCardBalance(toCard));
    }

    @Test
    public void shouldNotTransferIfNotEnoughMoney() {
        var dashboard = loginAndVerify(2 , 1);
        var amount = dashboard.getCardBalance(fromCard) + 10;

        dashboard.moneyTransfer(fromCard, toCard, Integer.toString(amount));
        dashboard.getErrorNotification().should(appear).shouldHave(text("Произошла ошибка"));
    }

    @ParameterizedTest
    @CsvSource({
            "If invalid card number, 5559 0000 0000 000",
            "If same card number, 5559 0000 0000 0002",
            "If other card number, 9999 9999 9999 9999",
            "If null card number,",
    })
    public void shouldNotTransferMoneyAndShowError(String testName, String fromCard) {
        var dashboard = loginAndVerify(1,2);

        dashboard.moneyTransfer(fromCard, toCard, "100");
        dashboard.getErrorNotification().should(appear).shouldHave(text("Произошла ошибка"));
    }
}