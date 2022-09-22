import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.openqa.selenium.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void setUpTest() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    void shouldTestTheRightForm() {

        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванов-Петрова Анна Мария");
        $("[data-test-id=\"phone\"] input").setValue("+79012345678");
        $x("//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").click();
        $(".button__content").click();
        $x("//div[text()= 'Успешно!']").should(Condition.visible, Duration.ofSeconds(15));
        $x("//div[contains(text(), 'Встреча успешно забронирована')]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldGetCardIfCityNotValid() {
        $("[placeholder=\"Город\"]").setValue("Чехов");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванова-Петрова Анна Мария");
        $("[data-test-id=\"phone\"] input").setValue("+79012345678");
        $x("//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").click();
        $(".button__content").click();
        $x("//span[text()=\"Доставка в выбранный город недоступна\"]").should(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldGetCardEarlierThan3Days() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванова-Петрова Анна Мария");
        $("[data-test-id=\"phone\"] input").setValue("+79012345678");
        $x("//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").click();
        $(".button__content").click();
        $("[data-test-id=\"date\"] .input__sub")
                .shouldHave(Condition.text("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldGetCardIfEnglishName() {

        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Smith John");
        $("[data-test-id=\"phone\"] input").setValue("+79012345678");
        $x("//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").click();
        $(".button__content").click();
        $x("//span[text()=\"Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.\"]").should(Condition.visible, Duration.ofSeconds(15));

    }

    @Test
    void shouldGetCardIfPhoneWithoutPlus() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванова-Петрова Анна Мария");
        $("[data-test-id=\"phone\"] input").setValue("89012345678");
        $x("//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"] .input__sub")
                .shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldGetCardIfPhoneContains12Numbers() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванова-Петрова Анна Мария");
        $("[data-test-id=\"phone\"] input").setValue("+790123456789");
        $x("//span[contains(text(), 'Я соглашаюсь с условиями обработки')]").click();
        $(".button__content").click();
        $("[data-test-id=\"phone\"] .input__sub")
                .shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678"), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldGetCardIfNoAgreement() {
        $("[placeholder=\"Город\"]").setValue("Москва");
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        String planningDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyy"));
        $("[placeholder=\"Дата встречи\"]").setValue(planningDate);
        $("[data-test-id=\"name\"] input").setValue("Иванова-Петрова Анна Мария");
        $("[data-test-id=\"phone\"] input").setValue("+79012345678");
        $(".button__content").click();
        $("[data-test-id=\"agreement\"].input_invalid").should(Condition.visible, Duration.ofSeconds(15));
    }

}
