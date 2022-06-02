package specbuilder;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import oauth.OAuth.Utils;
import pojo.AddPlace;
import pojo.Location;

public class RequestResponseSpecBuilder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
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
		
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri(Utils.URL).addQueryParam("key", "qaclick123")
		                         .setContentType(ContentType.JSON).build();
		
		ResponseSpecification res = new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();
		
		RestAssured.baseURI= Utils.URL;
		
		RequestSpecification request = given().log().all().spec(req).body(ap);
		
		String response = request.when().post("/maps/api/place/add/json")
		.then().spec(res).assertThat().extract().response().asString();
		
		System.out.println("***THE RESPONSE IS*** "  + response);
		
		

	}

}
