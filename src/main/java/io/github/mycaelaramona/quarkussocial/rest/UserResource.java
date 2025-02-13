package io.github.mycaelaramona.quarkussocial.rest;

import io.github.mycaelaramona.quarkussocial.domain.model.User;
import io.github.mycaelaramona.quarkussocial.domain.repository.UserRepository;
import io.github.mycaelaramona.quarkussocial.rest.dto.CreateUserRequest;
import io.github.mycaelaramona.quarkussocial.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository repository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository userRepository, Validator validator){

        this.repository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest){

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);

        if(!violations.isEmpty()){


            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

            //Removido após a criação do ResponseError.
            //ConstraintViolation<CreateUserRequest> erro = violations.stream().findAny().get();
            //tring errorMessage = erro.getMessage();

            //Ep.23 7:20  Uso do response error:

            //Retirado no Ep.24 5:13
            //ResponseError responseError = ResponseError.createFromValidation(violations);
            //return Response.status(400).entity(responseError).build();

        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());

        repository.persist(user); //se salva no BD

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public  Response deleteUser(@PathParam("id") long id){

        User user = repository.findById(id);

        if(user != null) {
            repository.delete(user);
            return Response.noContent().build();
        };
        return  Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") long id, CreateUserRequest userData){

        User user = repository.findById(id);

        if(user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());

            return  Response.noContent().build();
        }

        return  Response.status(Response.Status.NOT_FOUND).build();
    }
}
