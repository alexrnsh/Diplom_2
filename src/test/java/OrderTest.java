import model.UserModel;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static data.Constants.*;
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
    public void testCreateOrderWithoutAuth() {

        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72");

        orderApi.createOrderWithoutAuth(ingredients)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    public void testCreateOrderWithAuth() {

        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa71", "61c0c5a71d1f82001bdaaa72");

        orderApi.createOrderWithAuth(userToken, ingredients)
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.owner", notNullValue());
    }

    @Test
    public void testCreateOrderWithEmptyIngredients() {
        orderApi.createOrderWithEmptyIngredients(userToken)
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void testCreateOrderWithWrongIngredients() {

        List<String> ingredients = Arrays.asList("invalidIngredient1", "invalidIngredient2");

        orderApi.createOrderWithWrongIngredients(userToken, ingredients)
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

