import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static constants.Constants.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderTest extends BaseTest {

    private static String userToken;

    @BeforeClass
    public static void createUserGetToken() {
        UserModel user = new UserModel (EMAIL, PASSWORD, NAME);
        ValidatableResponse response = userApi.createUser(user)
                .statusCode(SC_OK)
                .body("success", Matchers.equalTo(true));

        userToken = response.extract().path("accessToken");

    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Отправляет запрос на создание заказа и проверяет что возвращается статус 200")
    public void testCreateOrderWithoutAuth() {

        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72");

        orderApi.createOrderWithoutAuth(ingredients)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Отправляет запрос на создание заказа и проверяет что возвращается статус 200")
    public void testCreateOrderWithAuth() {

        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72");

        orderApi.createOrderWithAuth(userToken, ingredients)
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.owner", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Отправляет запрос на создание заказа и проверяет что возвращается статус 400")
    public void testCreateOrderWithEmptyIngredients() {
        orderApi.createOrderWithAuth(userToken, List.of())
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    @Description("Отправляет запрос на создание заказа и проверяет что возвращается статус 500")
    public void testCreateOrderWithWrongIngredients() {

        List<String> ingredients = Arrays.asList("invalidIngredient1", "invalidIngredient2");

        orderApi.createOrderWithAuth(userToken, ingredients)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @AfterClass
    public static void testUserDeletion(){
        if (userToken != null) {
            userApi.deleteUser(userToken)
                    .statusCode(SC_ACCEPTED)
                    .body("success", Matchers.equalTo(true))
                    .body("message", Matchers.equalTo("User successfully removed"));
        }
    }

}

