package ru.netology.web.data;

import lombok.Value;

import java.util.List;

public class DataHelper {
    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class CardNumbers {
        private List<String> cardNumbers;
    }

    public static CardNumbers getCardNumbersFor(AuthInfo authInfo) {
        return new CardNumbers(List.of("5559 0000 0000 0001", "5559 0000 0000 0002"));
    }
}