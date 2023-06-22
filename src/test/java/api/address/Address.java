package api.address;

import api.ApiBase;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import schemas.AddressDto;
import schemas.ContactDto;

public class Address extends ApiBase {
    Response response;
    AddressDto dto;
    Faker faker = new Faker();

    public AddressDto randomDataForCreateAddress(int contactId){ // зависимость ламбок для того, чтобы сп класс как сеттегр геттер и давать ему аннотации для работы с параментрами
        dto = new AddressDto();
        dto.setCity("Berlin");
        dto.setCountry("Germany");
        dto.setStreet("Thorner");
        dto.setZip("21335");
        dto.setContactId(contactId);
        return dto;
    }

    public AddressDto dataForEditAddress(int id, int contactId){ // зависимость ламбок для того, чтобы сп класс как сеттегр геттер и давать ему аннотации для работы с параментрами
        dto = new AddressDto();
        dto.setId(id);
        dto.setCity("Concor");
        dto.setCountry("Albania");
        dto.setStreet("Sommer");
        dto.setZip("33333");
        dto.setContactId(contactId);
        return dto;
    }

    public Response createAddress(Integer code, int id){
        String endpoint = "/api/address";
        response = postRequest(endpoint, code, randomDataForCreateAddress(id));
        return response;
    }
    public void editAddress(Integer code, int id, int contactId){
        String endPoint = "/api/address";
        putRequest(endPoint, code, dataForEditAddress(id, contactId));
    }

    public Response deleteAddress(Integer code, int id){
        String endPoint = "/api/address/{id}";
        response = deleteRequest(endPoint, code, id);
        return response;
    }

    public Response getAddress(Integer code, Integer id){ //передаем параметры
        String endPoint = "/api/address/{id}"; //get запрос с параметром {id}
        response = getRequestWithParam(endPoint, code, "id", id); //тут в скобках - это аргумнты
        return response;
    }

    public Response getAllAddresses (Integer code, Integer id){ //метод для того, чтобы вытащить потом id самого адреса
        String endPoint = "/api/address/{contactId}/all";
        response = getRequestWithParam(endPoint, code, "contactId", id);
        return response;
    }

}
