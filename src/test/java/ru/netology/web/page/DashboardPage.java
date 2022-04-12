package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        SelenideElement card = cards.findBy(text(hiddenNumber(id)));
        String text = card.text().split(":")[1];
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        String value = text.substring(0, text.indexOf("р.")).trim();
        return Integer.parseInt(value);
    }

    private String hiddenNumber(String id) {
        int length = id.length();
        return "**** **** **** " + id.substring(length - 4, length);
    }

    public TransferPage selectTargetCard(String toCard) {
        SelenideElement recipient = cards.findBy(text(hiddenNumber(toCard)));
        recipient.$("button").click();
        return new TransferPage();
    }
}