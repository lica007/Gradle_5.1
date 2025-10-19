package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static data.DataGenerator.Registration.getUser;

public class CreateUserTest {

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @Test
    public void shouldOpenPersonalAccountOfARegisteredUser() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("active");
        $("[data-test-id='login'] input").setValue(registrationUser.getLogin());
        $("[data-test-id='password'] input").setValue(registrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2")
                .shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldAppearErrorMessage() {
        var notRegistrationUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegistrationUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegistrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'].notification")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldAppearWhenTheBlockedUserLogsInMessageAboutTheBlockedUser() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("blocked");
        $("[data-test-id='login'] input").setValue(registrationUser.getLogin());
        $("[data-test-id='password'] input").setValue(registrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'].notification")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldAppearErrorMessageEnteringAnIncorrectLogin() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("active");
        var invalidLogin = DataGenerator.generateLogin();
        $("[data-test-id='login'] input").setValue(invalidLogin);
        $("[data-test-id='password'] input").setValue(registrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'].notification")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldAppearErrorMessageEnteringAnIncorrectPassword() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("active");
        var invalidPassword = DataGenerator.generatePassword();
        $("[data-test-id='login'] input").setValue(registrationUser.getLogin());
        $("[data-test-id='password'] input").setValue(invalidPassword);
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'].notification")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldBeFilledInPasswordField() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("active");
        $("[data-test-id='login'] input").setValue(registrationUser.getLogin());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='password'] .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldBeFilledInLoginField() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("active");
        $("[data-test-id='password'] input").setValue(registrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldBeFilledInAllField() {
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
        $("[data-test-id='password'] .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldBeBlockedActiveUser() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("active");

        $("[data-test-id='login'] input").setValue(registrationUser.getLogin());
        $("[data-test-id='password'] input").setValue(registrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2")
                .shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);

        closeWebDriver();
        Selenide.open("http://localhost:9999");
        var blockUser = DataGenerator.Registration.getBlockedUser(registrationUser);

        $("[data-test-id='login'] input").setValue(blockUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'].notification")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldBeActivatedBlockedUser() {
        var registrationUser = DataGenerator.Registration.getRegistrationUser("blocked");

        $("[data-test-id='login'] input").setValue(registrationUser.getLogin());
        $("[data-test-id='password'] input").setValue(registrationUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='error-notification'].notification")
                .shouldBe(Condition.visible, Duration.ofSeconds(5))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));

        closeWebDriver();
        Selenide.open("http://localhost:9999");
        var activeUser = DataGenerator.Registration.getActiveUser(registrationUser);

        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("h2")
                .shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);
    }
}
