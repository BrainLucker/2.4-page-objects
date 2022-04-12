package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement heading = $("[data-test-id=dashboard] ~ h1");
    private final SelenideElement amount = $("[data-test-id=amount] input");
    private final SelenideElement from = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification] .notification__content");

    public SelenideElement getErrorNotification() {
        return errorNotification;
    }

    public TransferPage() {
        heading.shouldHave(text("Пополнение карты")).shouldBe(visible);
    }

    public void moneyTransfer(String fromCard, String sum) {
        amount.val(sum);
        from.val(fromCard);
        transferButton.click();
    }
}