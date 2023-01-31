import models.lombok.RegistrationBodyLombokModel;
import models.lombok.RegistrationResponseLombokModel;
import models.lombok.UnsuccessfulRegResponseLombokModel;
import models.pojo.CreateUserBodyPojoModel;
import models.pojo.CreateUserResponsePojoModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.RegistrationSpecs.*;


public class RestApiTests {

   @Test
   void createUserWithPojoTest(){
       CreateUserBodyPojoModel data = new CreateUserBodyPojoModel();
       data.setName("Mr.Smith");
       data.setJob("security");

       CreateUserResponsePojoModel response = given(requestSpec)
               .body(data)
               .when()
               .post("/users")
               .then()
               .spec(createUserResponseSpec)
               .extract().as(CreateUserResponsePojoModel.class);
       assertThat(response.getName()).isEqualTo("Mr.Smith");
       assertThat(response.getJob()).isEqualTo("security");


   }
    @Test
    void unsuccessfulRegistrationTest() {
        RegistrationBodyLombokModel data = new RegistrationBodyLombokModel();
        data.setEmail("sydney@fife");
        UnsuccessfulRegResponseLombokModel response = given(requestSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(unsuccessfulRegistrationResponseSpec)
                .extract().as(UnsuccessfulRegResponseLombokModel.class);
        assertThat(response.getError()).isEqualTo("Missing password");
    }

    @Test
    void successfulRegistrationWithLombokTest() {
        RegistrationBodyLombokModel data = new RegistrationBodyLombokModel();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("pistol");

        RegistrationResponseLombokModel response = given(requestSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(registrationResponseSpec)
                .extract().as(RegistrationResponseLombokModel.class);
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        assertThat(response.getId()).isEqualTo("4");
    }
    @Test
    void deleteUserTest() {

        given(requestSpec)
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }
}
