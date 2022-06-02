package oauth.OAuth;

import static io.restassured.RestAssured.given;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.path.json.JsonPath;

public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		
		//Step-1 WebUI integration to grab the authorization code  -- Google has now blocked developing any bot for their sign-in. They made some intelligence so that they are now
		//able to identify that there is a bot developed to automate their Sign-in and trying to grab the authorization code
		//That's why we blocked from line 21 - 30
		//we can ask developer to increase the life of the authentication code so that we don't need to perform the task all the time
		
		
		/*WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("wrishi.d.fire@gmail.com");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Utils.password);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(6000);*/
		
		String currentUrl = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWjKtrlkB_9uxglxEO65YdjWJlNq1K0igwxYnY0yFK9qusxSVES9sReFLyALK8QemA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent#";
		
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
		String response  = given().queryParam("access_token", accessToken)
		.when().log().all().get("https://rahulshettyacademy.com/getCourse.php")
		.asString();

		System.out.println("######RESPONSE IS####" + response);
		
		
	}

}
