import models.lombok.RegistrationBodyDto;
import models.lombok.RegistrationResponseDto;
import models.lombok.UnsuccessfulRegResponseDto;
import models.lombok.UserDto;
import models.pojo.CreateUserBodyDto;
import models.pojo.CreateUserResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.SpecsReqres.*;

public class UserApiTest {

   @Test
   @DisplayName("Создание пользователя")
   void createUserWithPojoTest(){
       CreateUserBodyDto data = new CreateUserBodyDto();
       data.setName("Mr.Smith");
       data.setJob("security");

       CreateUserResponseDto response = given(requestSpec)
               .body(data)
               .when()
               .post("/users")
               .then()
               .spec(createUserResponseSpec)
               .extract().as(CreateUserResponseDto.class);
       assertThat(response.getName()).isEqualTo("Mr.Smith");
       assertThat(response.getJob()).isEqualTo("security");


   }
    @Test
    @DisplayName("Неуспешная регистрация пользователя")
    void unsuccessfulRegistrationTest() {
        RegistrationBodyDto data = new RegistrationBodyDto();
        data.setEmail("sydney@fife");
        UnsuccessfulRegResponseDto response = given(requestSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(unsuccessfulRegistrationResponseSpec)
                .extract().as(UnsuccessfulRegResponseDto.class);
        assertThat(response.getError()).isEqualTo("Missing password");
    }

    @Test
    @DisplayName("Регистрация пользователя")
    void successfulRegistrationWithLombokTest() {
        RegistrationBodyDto data = new RegistrationBodyDto();
        data.setEmail("eve.holt@reqres.in");
        data.setPassword("pistol");

        RegistrationResponseDto response = given(requestSpec)
                .body(data)
                .when()
                .post("/register")
                .then()
                .spec(registrationResponseSpec)
                .extract().as(RegistrationResponseDto.class);
        Assertions.assertNotNull(response.getToken());
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
        UserDto userResponse = given().spec(requestSpec)
                .when()
                .pathParam("id", "2")
                .get("/users/{id}")
                .then()
                .spec(response)
                .extract().jsonPath().getObject("data", UserDto.class);

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
