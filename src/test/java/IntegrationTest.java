/**
 * Created by mirco on 20.05.17.
 */

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

public class IntegrationTest {

    @BeforeClass
    public static void init() {
        RestAssured.rootPath = "http://localhost";
        RestAssured.port = 9001;
    }

    @Test
    public void smoke() {
        given().when().get("/").then().statusCode(200);
    }

    @Test
    public void uploadPDF() {
        InputStream resource = IntegrationTest.class.getResourceAsStream("vr_44_loesung.pdf");
        given().multiPart("doc", "vr_44_loesung.pdf", resource).expect().statusCode(201).when().post("/document/import");

    }


}
