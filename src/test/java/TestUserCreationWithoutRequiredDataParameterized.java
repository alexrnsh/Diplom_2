import model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static data.Constants.*;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class TestUserCreationWithoutRequiredDataParameterized extends BaseTest {

    private final String email;
    private final String password;
    private final String name;

    public TestUserCreationWithoutRequiredDataParameterized(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters(name = "{0} {1} {2}")
    public static Object[][] testData() {
        return new Object[][]{
                {null, PASSWORD, NAME},
                {EMAIL, null, NAME},
                {EMAIL, PASSWORD, null}
        };
    }

    @Test
    public void testCannotCreateUserWithoutRequiredData() {
        UserModel user = new UserModel(email, password, name);
        userApi.createUser(user)
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
