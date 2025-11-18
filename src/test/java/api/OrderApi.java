package api;

import io.restassured.response.ValidatableResponse;

import java.util.List;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private static final String ORDER_CREATION_API = "/api/orders";

    @Step("Создание заказа без авторизации" + ORDER_CREATION_API)
    public ValidatableResponse createOrderWithoutAuth (List<String> ingredients) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body("{\"ingredients\": " + toJsonArray(ingredients) + "}")
                .post(ORDER_CREATION_API)
                .then()
                .log().all();
    }
    @Step ("Создание заказа с авторизацией" + ORDER_CREATION_API)
    public ValidatableResponse createOrderWithAuth(String userToken, List<String> ingredients) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)  // Добавляем токен
                .contentType("application/json")
                .body("{\"ingredients\": " + toJsonArray(ingredients) + "}")
                .post(ORDER_CREATION_API)
                .then()
                .log().all();
    }
    @Step ("Создание заказа без ингредиентов" + ORDER_CREATION_API)
    public ValidatableResponse createOrderWithEmptyIngredients(String userToken) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)  // Добавляем токен
                .contentType("application/json")
                .body("{\"ingredients\": []}")  // Пустой массив ингредиентов
                .post(ORDER_CREATION_API)
                .then()
                .log().all();
    }
    @Step ("Создание заказа неправильным хэшем ингредиентов" + ORDER_CREATION_API)
    public ValidatableResponse createOrderWithWrongIngredients(String userToken, List<String> ingredients) {
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .contentType("application/json")
                .body("{\"ingredients\": " + toJsonArray(ingredients) + "}")  // Неверные ингредиенты
                .post(ORDER_CREATION_API)
                .then()
                .log().all();
    }

    private String toJsonArray(List<String> ingredients) {
        StringBuilder jsonArray = new StringBuilder("[");
        for (int i = 0; i < ingredients.size(); i++) {
            jsonArray.append("\"").append(ingredients.get(i)).append("\"");
            if (i < ingredients.size() - 1) {
                jsonArray.append(",");
            }
        }
        jsonArray.append("]");
        return jsonArray.toString();
    }

}
