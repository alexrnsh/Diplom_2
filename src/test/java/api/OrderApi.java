package api;

import io.restassured.response.ValidatableResponse;

import java.util.List;
import io.qameta.allure.Step;
import model.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private static final String ORDER_CREATION_API = "/api/orders";

    @Step("Создание заказа без авторизации" + ORDER_CREATION_API)
    public ValidatableResponse createOrderWithoutAuth (List<String> ingredients) {
        OrderRequest request = new OrderRequest(ingredients);
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .body(request)
                .post(ORDER_CREATION_API)
                .then()
                .log().all();
    }

    @Step ("Создание заказа с авторизацией" + ORDER_CREATION_API)
    public ValidatableResponse createOrderWithAuth(String userToken, List<String> ingredients) {
        OrderRequest request = new OrderRequest(ingredients);

        return given()
                .log().all()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)  // Добавляем токен
                .contentType("application/json")
                .body(request)
                .post(ORDER_CREATION_API)
                .then()
                .log().all();
    }
}
