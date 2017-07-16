package test; /**
 * Created by mirco on 20.05.17.
 */

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

//import static io.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;
@Ignore
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
        given().multiPart("doc", "vr_44_loesung.pdf", resource).expect().statusCode(201).header("Location", notNullValue()).when().post("/document/import");
    }

    @Test
    public void bulkExport() {
        given().when().post("/document/export");
    }

    @Test
    public void bulkUpload() {
        InputStream resource = IntegrationTest.class.getResourceAsStream("vr_44_loesung.pdf");
        InputStream resource2 = IntegrationTest.class.getResourceAsStream("zr_71_loesung.pdf");
        given().multiPart("doc", "vr_44_loesung.pdf", resource).multiPart("doc", "zr_71_loesung.pdf", resource2).expect().statusCode(201).header("Location", notNullValue()).when().post("/document/import");
    }


}
