package com.brunasilva.quarkussocial.rest;

import com.brunasilva.quarkussocial.rest.dto.CreateUserRequest;
import com.brunasilva.quarkussocial.rest.dto.ResponseError;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    @TestHTTPResource("/users")
    URL apiURL;

    @Test
    @DisplayName("Should create an user successfully")
    @Order(1)
    void createUserTest(){

        var user = new CreateUserRequest();
        user.setName("Bruna");
        user.setAge(40);

        //cenário: given, execução: when, então: then
        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                .when()
                        .post(apiURL)
                .then()
                        .extract().response();

        //assertivas
        assertEquals(201, response.statusCode()); //201 CREATED
        assertNotNull(response.jsonPath().getString("id"));
    }
    @Test
    @DisplayName("Should return error when json is not valid")
    @Order(2)
    void createUserValidationErrorTest() {

        var user = new CreateUserRequest();
        user.setAge(null);
        user.setName(null);

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(user)
                .when()
                    .post(apiURL)
                .then()
                    .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        // assertEquals("Validation Error", response.jsonPath().getString("message"));
        List<Map<String, String>> errors = response.jsonPath().getList("errors"); //capturando todos os erros
        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));
        /*
        *testando com a mensagem especifica, esse tipo de teste pode dar erro pois nem sempre as mensagens vem na ordem
        *assertEquals("Age is Required", errors.get(0).get("message"));
        *assertEquals("Name is Required", errors.get(1).get("message"));
        */
    }

    @Test
    @DisplayName("Should list all users")
    @Order(3) //os testes serão executados numa ordem para que o usuário seja criado antes de executar esse teste
    void listAllUsersTest(){

        given()
            .contentType(ContentType.JSON)
       .when()
            .get(apiURL)
       .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}