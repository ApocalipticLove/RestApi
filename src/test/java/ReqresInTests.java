import models.lombok.RegistrationBodyLombokModel;
import models.lombok.RegistrationResponseLombokModel;
import models.lombok.UnsuccessfulRegResponseLombokModel;
import models.lombok.User;
import models.pojo.CreateUserBodyPojoModel;
import models.pojo.CreateUserResponsePojoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static specs.SpecsReqres.*;


public class ReqresInTests {

   @Test
   @DisplayName("Создание пользователя")
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
    @DisplayName("Неуспешная регистрация пользователя")
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
    @DisplayName("Регистрация пользователя")
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
    @DisplayName("Проверка значений в списке с помощью Groovy")
    void listWithGroovyTest() {
        given()
                .spec(requestSpec)
                .when()
                .get("/unknown")
                .then()
                .spec(response)
                .log().body()
                .body("data.findAll{it.id == 3}.name", hasItem("true red"))
                .body("data.findAll{it.id == 3}.color", hasItem("#BF1932"));
    }

    @Test
    @DisplayName("Проверка id и email пользователя")
    void checkUserIdAndEmail() {
        User userResponse = given().spec(requestSpec)
                .when()
                .pathParam("id", "2")
                .get("/users/{id}")
                .then()
                .spec(response)
                .extract().jsonPath().getObject("data", User.class);

        assertEquals(2, userResponse.getId());
        assertThat(userResponse.getEmail()).isEqualTo("janet.weaver@reqres.in");
    }

    @Test
    @DisplayName("Проверка наличия пользователя в базе")
    void getSingleUserNotFound() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUserTest() {

        given(requestSpec)
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }
}
