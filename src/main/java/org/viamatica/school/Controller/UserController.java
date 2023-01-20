package org.viamatica.school.Controller;

import net.bytebuddy.asm.Advice;
import org.apache.http.HttpStatus;
import org.viamatica.school.Service.UserService;
import org.viamatica.school.model.User;
import org.viamatica.school.utility.MessageResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/user")
public class UserController {

    @Inject
    UserService userService;

    /**
     * Obtenemos todos los registro
     * @return List<User>
     */
    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUser(  @HeaderParam("token") String token ) {
        List<User> users = userService.findAll(token);
        if(users == null){
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("Consulta fallida",null)).build();
        }
        return Response.status(HttpStatus.SC_OK).entity(new MessageResponse("Consulta exitosa",users)).build();
    }

    /**
     * Buscamos un recurso por su id
     * @param id
     * @return User
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseById(@PathParam("id") Integer id, @HeaderParam("token") String token) {
        User user =  userService.getUserById(id);
        return  Response.status(HttpStatus.SC_OK).entity(new MessageResponse("Consulta exitosa",user)).build();
    }


    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User user,@HeaderParam("token") String token) {

        String res =  userService.saveUser(user,token);

        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Se guardo con exito",null)).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res,null)).build();
        }
    }




}
