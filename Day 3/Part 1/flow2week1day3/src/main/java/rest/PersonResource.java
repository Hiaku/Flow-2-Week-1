package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonDTOwithoutID;
import dto.PersonsDTO;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;
import facades.PersonFacade;
import facades.IPersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final IPersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsDTO() {
        PersonsDTO persons = new PersonsDTO(FACADE.getAllPersons());
//        Map all = new HashMap();
//        all.put("all", persons);
        return GSON.toJson(persons);
    }

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonDTObyID(@PathParam("id") int id) throws PersonNotFoundException {
        try {
            PersonDTO person = new PersonDTO(FACADE.getPerson(id));
            return GSON.toJson(person);
        } catch (PersonNotFoundException ex) {
            throw new PersonNotFoundException(ex.getMessage());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePerson(String p) throws MissingInputException {
        try {
            PersonDTO personDTO = GSON.fromJson(p, PersonDTO.class);
            Person person = FACADE.addPerson(personDTO.getFname(), personDTO.getLname(), personDTO.getPhone());
            PersonDTOwithoutID responseDTO = new PersonDTOwithoutID(person);
            return Response.ok(responseDTO).build();
        } catch (MissingInputException ex) {
            throw new MissingInputException(ex.getMessage());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPerson(String p) throws PersonNotFoundException {
        try {
            PersonDTO personDTO = GSON.fromJson(p, PersonDTO.class);
            Person person = FACADE.getPerson(personDTO.getId());
            person.editPerson(personDTO);
            PersonDTO responseDTO = new PersonDTO(FACADE.editPerson(person));
            return Response.ok(responseDTO).build();
        } catch (MissingInputException ex) {
            return Response.notModified(ex.getMessage()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        try {
            FACADE.deletePerson(id);
            return "{\"status\": \"removed\"}";
        } catch (PersonNotFoundException ex) {
            throw new PersonNotFoundException(ex.getMessage());
        }
    }
}
