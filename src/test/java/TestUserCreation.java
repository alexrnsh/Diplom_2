import Model.UserModel;

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
    public void testUserCanBeCreated(){

        ValidatableResponse response = userApi.createUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        userToken = response.extract().path("accessToken");
    }

    @Test
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
