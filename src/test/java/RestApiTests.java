import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;


public class RestApiTests {
    String baseUrl = "https://reqres.in/";

    @Test
    void singleUserTest() {
        given()
                .log().uri()
                .baseUri(baseUrl)
                .contentType(JSON)
                .when()
                .get("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

   @Test
   void createUserTest(){
       String body = "{ \"name\": \"Mr.Smith\", \"job\": \"security\"}";

       given()
               .log().uri()
               .contentType(JSON)
               .body(body)
               .when()
               .post("https://reqres.in/api/users")
               .then()
               .log().status()
               .log().body()
               .statusCode(201)
               .body("name", is("Mr.Smith"))
               .body("job", is("security"));

   }
    @Test
    void unsuccessfulRegistrationTest() {

        given()
                .log().uri()
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .statusCode(415);
    }

    @Test
    void successfulRegistrationTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
    @Test
    void deleteUserTest() {

        given()
                .log().uri()
                .when()
                .delete(baseUrl + "/users/2")
                .then()
                .statusCode(204);
    }
}
