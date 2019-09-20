package rest;

import dto.PersonDTO;
import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.Strategy;

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, Strategy.DROP_AND_CREATE);

        httpServer = startServer();

        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;

        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        httpServer.shutdownNow();
    }

    private Person person1;
    private Person person2;
    private Person person3;
    private List<Person> people;

    @BeforeEach
    public void setUp() {
        people = new ArrayList<>();
        person1 = new Person("At", "Law", "10101010");
        person2 = new Person("B", "LT", "20202020");
        person3 = new Person("K", "GB", "30303030");

        people.add(person1);
        people.add(person2);
        people.add(person3);

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Query query = em.createNativeQuery("truncate table flow2week1day3_test.PERSON;");
            query.executeUpdate();
            em.getTransaction().commit();

            for (Person p : people) {
                em.getTransaction().begin();
                em.persist(p);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void getAllPersonsTest() {
        given()
                .contentType("application/json").when()
                .get("/person").then().log().body().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("all[0].id", equalTo(1))
                .body("all[0].firstname", equalTo("At"))
                .body("all[0].lastname", equalTo("Law"))
                .body("all[0].phone", equalTo("10101010"))
                .body("all[1].id", equalTo(2))
                .body("all[1].firstname", equalTo("B"))
                .body("all[1].lastname", equalTo("LT"))
                .body("all[1].phone", equalTo("20202020"))
                .body("all[2].id", equalTo(3))
                .body("all[2].firstname", equalTo("K"))
                .body("all[2].lastname", equalTo("GB"))
                .body("all[2].phone", equalTo("30303030"))
                .body("all.size()", is(3));
    }

    @Test
    public void getAllPersonsTestNew() {
        List<PersonDTO> persons;
        persons = given()
                .contentType("application/json")
                .when()
                .get("person")
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);

        assertThat(persons, containsInAnyOrder(new PersonDTO(person1), new PersonDTO(person2), new PersonDTO(person3)));
    }

    @Test
    public void testgetPersonById() {
        given()
                .contentType("application/json").when()
                .get("/person/{id}", 1).then().log().body().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(1))
                .body("firstname", equalTo("At"))
                .body("lname", equalTo("Law"))
                .body("phone", equalTo("10101010"));
    }

    @Test
    public void testgetPersonByWrongID() {
        given()
                .contentType("application/json").when()
                .get("/person/5").then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("No person with provided id found"));
    }

    @Test
    public void deletePersonByIdTest() {
        given()
                .contentType("application/json").when()
                .delete("/person/1").then().log().body().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("status", equalTo("removed"));
    }

    @Test
    public void deletePersonByIdTestWrong() {
        given()
                .contentType("application/json").when()
                .delete("/person/8").then().assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("Could not delete, provided id does not exist"));
    }

    @Test
    public void putPersonTest() {
        person2.setFirstName("Rick");
        person2.setLastName("Morty");

        given()
                .contentType("application/json")
                .body(new PersonDTO(person2))
                .when()
                .put("person")
                .then()
                .body("fname", equalTo("Rick"))
                .body("lname", equalTo("Morty"))
                .body("phone", equalTo("20202020"))
                .body("id", equalTo(2));
    }

    @Test
    public void postPersonTest() {
        given()
                .contentType("application/json")
                .body(new PersonDTO("On", "Post", "007007"))
                .when()
                .post("person")
                .then()
                .body("fname", equalTo("On"))
                .body("lname", equalTo("Post"))
                .body("phone", equalTo("007007"));
    }
}
