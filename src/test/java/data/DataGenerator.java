package data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void createUser(DataGenerator.RegistrationDto user) {
        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String generateLogin() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static class Registration {

        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(generateLogin(), generatePassword(), status);
        }

        public static RegistrationDto getRegistrationUser(String status) {
            var user = getUser(status);
            createUser(user);
            return user;
        }

        public static RegistrationDto getBlockedUser(RegistrationDto originalUser) {
            var blockedUser = new RegistrationDto(
                    originalUser.getLogin(),
                    originalUser.getPassword(),
                    "blocked"
            );
            createUser(blockedUser);
            return blockedUser;
        }

        public static RegistrationDto getActiveUser(RegistrationDto originalUser) {
            var activeUser = new RegistrationDto(
                    originalUser.getLogin(),
                    originalUser.getPassword(),
                    "active"
            );
            createUser(activeUser);
            return activeUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}