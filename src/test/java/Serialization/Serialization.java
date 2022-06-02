package Serialization;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import oauth.OAuth.Utils;
import pojo.AddPlace;
import pojo.Location;

public class Serialization {
	
	public static void main(String[] args) {
		
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setAddress("29, side layout, cohen 09");
		ap.setLanguage("French-IN");
		ap.setWebsite("http://google.com");
		ap.setPhone_number("8777244144");
		ap.setName("Frontline house");
		
		List<String> types = new ArrayList<String>();
		types.add("shoe park");
		types.add("shop");
		ap.setTypes(types);
		
		Location lc = new Location();
		lc.setLat(-38.383494);
		lc.setLng(33.427362);
		ap.setLocation(lc);
		
		RestAssured.baseURI= Utils.URL;
		
		String response = given().log().all().queryParam("key", "qaclick123").body(ap)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("***THE RESPONSE IS*** "  + response);
		
		
		
	}

}
