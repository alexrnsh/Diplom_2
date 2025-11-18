package api;

import io.qameta.allure.Step;
import model.UserModel;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserApi {

    private final static String USER_CREATION_API = "/api/auth/register";
    private final static String USER_LOGIN_API = "/api/auth/login";
    private final static String USER_DELETION_API = "/api/auth/user";

    @Step("Создание пользователя через POST " + USER_CREATION_API)
    public ValidatableResponse createUser (UserModel userModel){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(userModel)
                .post(USER_CREATION_API)
                .then()
                .log().all();

    }
    @Step("Логин пользователя через POST " + USER_LOGIN_API)
    public ValidatableResponse loginUser (UserModel userModel, String userToken) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(userModel)
                .post(USER_LOGIN_API)
                .then()
                .log().all();
    }
    @Step("Удаление пользователя через DELETE " + USER_DELETION_API)
    public ValidatableResponse deleteUser(String userToken){
        return given()
                .log().all()
                .header("Authorization", userToken) // Добавляем токен в заголовок
                .when()
                .delete(USER_DELETION_API)  // Путь запроса на удаление пользователя
                .then()
                .log().all();
    }

}
