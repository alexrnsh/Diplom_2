package constants;

import com.github.javafaker.Faker;

public class Constants {
    public static final String BASE_URL ="https://stellarburgers.education-services.ru/";
    final static Faker user = new Faker();
    public static final String EMAIL = user.internet().emailAddress();
    public static final String PASSWORD = user.internet().password();
    public static final String NAME = user.name().firstName();
}
