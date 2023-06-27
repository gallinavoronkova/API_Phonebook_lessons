package api.contact;

import api.ApiBase;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import schemas.ContactDto;

public class Contact extends ApiBase {
    Response response;
    ContactDto dto;
    Faker faker = new Faker();

    public ContactDto randomDataForCreteContact() { // зависимость ламбок для того, чтобы сп класс как сеттегр геттер и давать ему аннотации для работы с параментрами
        dto = new ContactDto();
        dto.setFirstName(faker.internet().uuid());
        dto.setLastName(faker.internet().uuid());
        dto.setDescription(faker.internet().uuid());
        return dto;
    }

    public ContactDto dataForEditContact(int id) { 
        dto = new ContactDto();
        dto.setId(id);
        dto.setFirstName("Galina");
        dto.setLastName("Voronkova");
        dto.setDescription("I am a student");
        return dto;
    }

    public Response createContact(Integer code) {
        String endpoint = "/api/contact";
        response = postRequest(endpoint, code, randomDataForCreteContact());
        response.as(ContactDto.class); // типизация будет такая же, как в ContactDto.class //сравниваем респонс, который пришел с тем, который задали в ContactDto
        return response;
    }

    public void editContact(Integer code, int id) {
        String endPoint = "/api/contact";
        putRequest(endPoint, code, dataForEditContact(id));
    }

    public Response deleteContact(Integer code, int id) {
        String endPoint = "/api/contact/{id}";
        response = deleteRequest(endPoint, code, id);
        return response;
    }

    public Response getContact(Integer code, Integer id) { //передаем параметры
        String endPoint = "/api/contact/{id}"; //get запрос с параметром {id}
        response = getRequestWithParam(endPoint, code, "id", id); //тут в скобках - это аргумнты
        return response;
    }
}
