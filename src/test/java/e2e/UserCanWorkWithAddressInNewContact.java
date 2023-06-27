package e2e;

import api.address.Address;
import api.contact.Contact;
import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.Test;
import schemas.AddressDto;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserCanWorkWithAddressInNewContact {
    Contact contact;
    Address address;

    @Test
    public void userCanWorkWithAddressInNewContact() throws JSONException {
        contact = new Contact();
        //create new contact - POST-запрос
        JsonPath createdContact = contact.createContact(201).jsonPath();
        int contactId = createdContact.getInt("id");        //чтобы вытащить из респонса наш id (value по ключу)

        address = new Address();
        address.createAddress(201, contactId);
        JsonPath createdAddress = address.getAllAddresses(200, contactId).jsonPath();
        int addressId = createdAddress.getInt("[0].id");
        String city = createdAddress.getString("[0].city");
        String country = createdAddress.getString("[0].country");
        String street = createdAddress.getString("[0].street");
        String zip = createdAddress.getString("[0].zip");

        JSONObject actualAddressJson = new JSONObject();
        actualAddressJson.put("id", addressId);
        actualAddressJson.put("city", city);
        actualAddressJson.put("country", country);
        actualAddressJson.put("street", street);
        actualAddressJson.put("zip", zip);
        actualAddressJson.put("contactId", contactId);

        String actualJson = actualAddressJson.toString();
        String expectedJson = "{\n" +
                "        \"id\": " + addressId + ",\n" +
                "        \"city\": \"Berlin\",\n" +
                "        \"country\": \"Germany\",\n" +
                "        \"street\": \"Thorner\",\n" +
                "        \"zip\": \"21335\",\n" +
                "        \"contactId\": " + contactId + "\n" +
                "    }";
        JSONAssert.assertEquals(actualJson, expectedJson, JSONCompareMode.STRICT);


        //Add edit address
        address.editAddress(200, addressId, contactId);
        JsonPath editedAddress = address.getAddress(200, addressId).jsonPath();

        LinkedHashMap<String, String> objectEditedData = new LinkedHashMap<>();
        objectEditedData.put(editedAddress.getString("city"), address.dataForEditAddress(addressId, contactId).getCity());
        objectEditedData.put(editedAddress.getString("country"), address.dataForEditAddress(addressId, contactId).getCountry());
        objectEditedData.put(editedAddress.getString("street"), address.dataForEditAddress(addressId, contactId).getStreet());
        objectEditedData.put(editedAddress.getString("zip"), address.dataForEditAddress(addressId, contactId).getZip());

        //Add check that address was edited
        for(Map.Entry<String, String> object: objectEditedData.entrySet()) {
            String actualResult = object.getKey();
            String expectedResult = object.getValue();
            Assert.assertEquals(actualResult, expectedResult, actualResult + "not equals" + expectedResult);
        }

        //Add delete address
        address.deleteAddress(200, addressId);

        //Add check that address was deleted
        JsonPath actualDeletedAddress = address.getAddress(500, addressId).jsonPath();
        Assert.assertEquals(actualDeletedAddress.getString("message"), "Error! This address doesn't exist in our DB");
    }

}