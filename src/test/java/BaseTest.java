import api.UserApi;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static data.Constants.BASE_URL;

public class BaseTest {
    protected static UserApi userApi = new UserApi();
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
}
