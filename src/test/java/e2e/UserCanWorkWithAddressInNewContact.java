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

public class UserCanWorkWithAddressInNewContact {
    Contact contact;
    Address address;

    @Test
    public void userCanWorkWithAddressInNewContact() throws JSONException {
        contact = new Contact();
        //create new contact TODO: POST
        JsonPath createdContact = contact.createContact(201).jsonPath();
        int contactId = createdContact.getInt("id");        //чтобы вытащить из респонса наш id (value по ключу)

        address = new Address();
        address.createAddress(201, contactId);
        JsonPath createdAddress = address.getAllAddresses(200, contactId).jsonPath();
        int addressId = createdAddress. getInt("[0].id");
        String city = createdAddress. getString("[0].city");
        String country = createdAddress. getString("[0].country");
        String street = createdAddress. getString("[0].street");
        String zip = createdAddress. getString("[0].zip");

        JSONObject actualAddressJson = new JSONObject();
        actualAddressJson.put("id", addressId);
        actualAddressJson.put("city", city);
        actualAddressJson.put("country", country);
        actualAddressJson.put("street", street);
        actualAddressJson.put("zip", zip);
        actualAddressJson.put("contactId", contactId);

        String actualJson = actualAddressJson.toString();
        String expectedJson = "{\n" +
                "        \"id\": "+addressId+",\n" +
                "        \"city\": \"Berlin\",\n" +
                "        \"country\": \"Germany\",\n" +
                "        \"street\": \"Thorner\",\n" +
                "        \"zip\": \"21335\",\n" +
                "        \"contactId\": "+contactId+"\n" +
                "    }";
        JSONAssert.assertEquals(actualJson, expectedJson, JSONCompareMode.STRICT);
    }
}