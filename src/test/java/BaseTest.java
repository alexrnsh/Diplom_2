import api.OrderApi;
import api.UserApi;
import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static data.Constants.BASE_URL;

public class BaseTest {

    protected static final UserApi userApi = new UserApi();
    protected static final OrderApi orderApi = new OrderApi();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

}
