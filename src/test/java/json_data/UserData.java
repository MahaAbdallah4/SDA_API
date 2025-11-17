package json_data;

public class UserData {

    public final static String USER_JSON = """
            {
                "name": "Test User",
                "email": "testuser@example.com",
                "password": "Password.12345"
            }
            """;

    public final static String USER_JSON_DYNAMIC = """
            {
                "name": "%s",
                "email": "%s",
                "password": "%s"
            }
            """;
}
