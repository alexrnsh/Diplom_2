import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import model.UserModel;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static data.Constants.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class TestUserCannotLoginWithWrongDataParameterized extends BaseTest{

    private final String email;
    private final String password;

    private static String userToken;

    public TestUserCannotLoginWithWrongDataParameterized(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @BeforeClass
    public static void createUserGetToken() {
        UserModel user = new UserModel (EMAIL, PASSWORD, NAME);
        ValidatableResponse response = userApi.createUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        userToken = response.extract().path("accessToken");
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] testData() {
        return new Object[][]{
                {null, PASSWORD},
                {EMAIL, null},
        };
    }

    @Test
    @DisplayName("Невозможно сделать логин без емэйл или пароля")
    @Description("Параметризованный тест на получение ошибки при попытке создания курьера без емэйл или пароля")
    public void testLoginWithoutRequiredDataReturns400() {

        UserModel user = new UserModel(email, password);
        userApi.loginUser(user, userToken)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", CoreMatchers.equalTo(false))
                .body("message", CoreMatchers.equalTo("email or password are incorrect"));
    }

    @AfterClass
    public static void testUserDeletion(){

        if (userToken != null) {
            userApi.deleteUser(userToken)
                    .statusCode(SC_ACCEPTED)  // Ожидаемый статус 200
                    .body("success", equalTo(true))  // Ожидаем, что success будет true
                    .body("message", equalTo("User successfully removed"));
        }
    }
}
