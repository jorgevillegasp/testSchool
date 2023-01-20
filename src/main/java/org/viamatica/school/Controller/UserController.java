package org.viamatica.school.Controller;

import org.apache.http.HttpStatus;
import org.viamatica.school.Service.UserService;
import org.viamatica.school.model.User;
import org.viamatica.school.utility.MessageResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/user")
public class UserController {

    @Inject
    UserService userService;

    /**
     * Obtenemos todos los registro
     * @return List<User>
     */
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUser() {
        return userService.findAll();
    }

    /**
     * Buscamos un recurso por su id
     * @param id
     * @return User
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getCourseById(@PathParam("id") Integer id) {
        return userService.getUserById(id);
    }


    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user,@HeaderParam("key") String token) {


        String res =  userService.saveUser(user,token);

        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Se guardo con exito")).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res)).build();
        }
    }




}
