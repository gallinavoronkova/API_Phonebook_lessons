package e2e;

import api.address.Address;
import api.contact.Contact;
import api.phone.Phone;
import io.restassured.path.json.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserCanWorkWithPhoneInNewContact {
    Contact contact;
    Phone phone;

    @Test
    public void userCanWorkWithPhoneInNewContact() throws JSONException {
        contact = new Contact();
        //create new contact - POST-запрос
        JsonPath createdContact = contact.createContact(201).jsonPath();
        int contactId = createdContact.getInt("id");        //чтобы вытащить из респонса наш id (value по ключу)

        phone = new Phone();
        phone.createPhone(201, contactId);
        JsonPath createdPhone = phone.getAllPhones(200, contactId).jsonPath();
        int phoneId = createdPhone.getInt("[0].id");
        String countryCode = createdPhone.getString("[0].countryCode");
        String phoneNumber = createdPhone.getString("[0].phoneNumber");

        JSONObject actualPhoneJson = new JSONObject();
        actualPhoneJson.put("id", phoneId);
        actualPhoneJson.put("countryCode", countryCode);
        actualPhoneJson.put("phoneNumber", phoneNumber);
        actualPhoneJson.put("contactId", contactId);

        String actualJson = actualPhoneJson.toString();
        String expectedJson = "{\n" +
                "        \"id\": " + phoneId + ",\n" +
                "        \"countryCode\": \"+49\",\n" +
                "        \"phoneNumber\": \"017677788890\",\n" +
                "        \"contactId\": " + contactId + "\n" +
                "    }";
        JSONAssert.assertEquals(actualJson, expectedJson, JSONCompareMode.STRICT);


        //edit address
        phone.editPhone(200, phoneId, contactId);
        JsonPath editedPhone = phone.getPhone(200, phoneId).jsonPath();

        LinkedHashMap<String, String> objectEditedData = new LinkedHashMap<>();
        objectEditedData.put(editedPhone.getString("countryCode"), phone.dataForEditPhone(phoneId, contactId).getCountryCode());
        objectEditedData.put(editedPhone.getString("phoneNumber"), phone.dataForEditPhone(phoneId, contactId).getPhoneNumber());

        // check that address was edited
        for(Map.Entry<String, String> object: objectEditedData.entrySet()) {
            String actualResult = object.getKey();
            String expectedResult = object.getValue();
            Assert.assertEquals(actualResult, expectedResult, actualResult + "not equals" + expectedResult);
        }

        // delete address
        phone.deletePhone(200, phoneId);

        // check that address was deleted
        JsonPath actualDeletedAddress = phone.getPhone(500, phoneId).jsonPath();
        Assert.assertEquals(actualDeletedAddress.getString("message"), "Error! This phone number doesn't exist in our DB");

        // delete contact
        contact.deleteContact(200, contactId);
        // get error message (not existing in DB)
        JsonPath actualDeletedContact = contact.getContact(500, contactId).jsonPath();
        Assert.assertEquals(actualDeletedContact.getString("message"), "Error! This contact doesn't exist in our DB");
    }
}
