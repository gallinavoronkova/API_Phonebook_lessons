package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiBase {
    final String BASE_URI = "http://phonebook.telran-edu.de:8080/";
    final String API_KEY = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InRlc3RAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MjEwNjk3ODI5Nn0.GM1wsoRV2QoAsD6wKmIk7N49DDpuCejK4BC9H9YItJvesH5vft8HO2uqTPnGQJwJ5oXKS2OILqP1yoanMnIMkA";

    RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON) //с каим форматом передачи данных будем работать
            .addHeader("Access-Token", API_KEY) //что должны добавлять в заголовок
            .build();

    public Response getRequest(String endPoint, Integer responseCode) {
        Response response = RestAssured.given()
                .spec(spec)
                .when()
                .log().all()
                .get(endPoint) //get method до нашего uri добавляем ендпоинт
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
        }

    public Response getRequestWithParam(String endPoint, Integer responseCode, String paramName, int id) {
        Response response = RestAssured.given()
                .spec(spec)
                .when()
                .pathParam(paramName, id)
                .log().all()
                .get(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public Response postRequest(String endPoint, Integer responseCode, Object body) {
        Response response = RestAssured.given()
                .spec(spec)
                .body(body)
                .when()
                .log().all()
                .post(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }

    public Response putRequest(String endPoint, Integer responseCode, Object body) {
        Response response = RestAssured.given()
                .spec(spec)
                .body(body)
                .when()
                .log().all()
                .put(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }
    public Response deleteRequest(String endPoint, Integer responseCode, int id) {
        Response response = RestAssured.given()
                .spec(spec)
                .when()
                .pathParam("id", id)
                .log().all()
                .delete(endPoint)
                .then().log().all()
                .extract().response();
        response.then().assertThat().statusCode(responseCode);
        return response;
    }
}
