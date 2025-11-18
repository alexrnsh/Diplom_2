import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static data.Constants.*;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class TestUserCreation extends BaseTest {

    private final UserModel user = new UserModel(EMAIL,PASSWORD,NAME);
    private String userToken;

    @Test
    @DisplayName("Создание пользователя")
    @Description("Отправляет запрос на создание пользователя и проверяет что возвращается статус 200")
    public void testUserCanBeCreated(){

        ValidatableResponse response = userApi.createUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        userToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых пользователей")
    @Description("Отправляет 2 запроса на создание одинаковых пользователей и проверяет что возвращается ошибка 403")
    public void testCannotCreateDuplicateUser(){
        UserModel duplicateUser = new UserModel(EMAIL, PASSWORD, NAME);

        ValidatableResponse response = userApi.createUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        userToken = response.extract().path("accessToken");

        userApi.createUser(duplicateUser)
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Логин пользователя")
    @Description("Отправляет запрос на логин пользователя и проверяет что возвращается статус 200")
    public void testUserCanLogin(){
        ValidatableResponse response = userApi.createUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        userToken = response.extract().path("accessToken");

        userApi.loginUser(user, userToken)
                .statusCode(SC_OK)
                .body("success", equalTo(true));

    }

    @After
    public void testUserDeletion(){
        if (userToken != null) {
            userApi.deleteUser(userToken)
                    .statusCode(SC_ACCEPTED)  // Ожидаемый статус 200
                    .body("success", equalTo(true))  // Ожидаем, что success будет true
                    .body("message", equalTo("User successfully removed"));
        }
    }
}
