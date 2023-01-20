package org.viamatica.school.Controller;

import org.apache.http.HttpStatus;
import org.viamatica.school.Service.LoginService;
import org.viamatica.school.model.dtoLogin;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/login")
public class LoginController {


    @Inject
    LoginService loginService;

    @Path("")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(dtoLogin user) throws SQLException {

        String validation = loginService.login(user.getUserName(), user.getUserPassword());

        if (!validation.equals("")) {
            return Response.status(HttpStatus.SC_OK).entity(validation).build();
        } else {
            return Response.status(HttpStatus.SC_CONFLICT).entity("Usuario y contraseña incorrecto").build();
        }
    }

}
