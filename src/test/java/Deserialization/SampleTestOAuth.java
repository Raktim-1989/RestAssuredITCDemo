package Deserialization;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class SampleTestOAuth {
	
	public static void main(String[] args )
	{
String currentUrl = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWjUxMdlwG5J9leMAxeq0H5TXWveFY95wCnCC0kKE6MYR6kfFauk-som4kXvc4RLnA&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		//4%2F0AX4XfWjvPOoyvAgWV58vDGIQWvh2MY4RU5Rqxd7QJu7MI2Szdz0fzaXVRjU5NrYs1Ye3Kw
		String partialCode = currentUrl.split("code=")[1];
		String actualAuthCode = partialCode.split("&scope")[0];
		
		System.out.println("##ACTUAL AUTHORIZATION CODE IS "  +  actualAuthCode);
		
		//Note when there is a "%" character in actualAuthCode, RestAssured will encode that value, so we will need to tell RestAssured tha don't do any encoding by
         //using the method "urlEncodingEnabled" in the following step
		
		
		//Step-2
		String accessTokenResponse = given().urlEncodingEnabled(false)
				.queryParams("code", actualAuthCode)
		       .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		       .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		       .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		       .queryParams("grant_type", "authorization_code")
		       .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println("##ACCESS-TOKEN IS  " + accessToken);
		
		   
		//Step-3 Actual Request
		GetCourse gc  = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php")
		.as(GetCourse.class);
		
        System.out.println(gc.getLinkedIn());
		
		System.out.println(gc.getCourses().getApi().get(1).getPrice());
		
		
		//get the price of the api whose courseTitle = 'SOAPUI Webservice testing'
          int size = gc.getCourses().getApi().size();
          for(int i = 0; i<size;i++)
          {
        	  
        	  if(gc.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
        	  {
        		  System.out.println(gc.getCourses().getApi().get(i).getPrice());
        		  
        	  }
        	  
          }
          
          //get all the courseTitle of the course 'webAutomation' and compare whether this is expected 
          String[] expectedCourse = {"Selenium Webdriver Java" , "Cypress"  , "Protractor"};
          int sizeweb = gc.getCourses().getWebAutomation().size();
          List<WebAutomation> webcourse = gc.getCourses().getWebAutomation();
          List<String> ls  = new ArrayList<String>();
          for(int i = 0; i< sizeweb;i++)
          {
        	  System.out.println("COURSE TITLE IS : " + webcourse.get(i).getCourseTitle());
        	  
        	  ls.add(webcourse.get(i).getCourseTitle());
        	  
          }
          List<String> expectedList = Arrays.asList(expectedCourse);
          if(ls.equals(expectedList))
          {
        	  System.out.println("courses displayed are correct ");
          }
          else
          {
        	  System.out.println("Thank you !!");
          }
          
          
          
		
	}

}
