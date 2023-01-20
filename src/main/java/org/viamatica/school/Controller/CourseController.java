package org.viamatica.school.Controller;

import org.apache.http.HttpStatus;

import org.viamatica.school.Service.CourseService;
import org.viamatica.school.model.Course;
import org.viamatica.school.utility.MessageResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/course")
public class CourseController {

    @Inject
    CourseService courseService;

    /**
     *  getAllCourse()
     *  Obtiene todo los cursos de la base de datos
     *
     * @return Course
     */
    //Obtener todos los registros
    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCourse( @HeaderParam("token") String token) {
        List<Course> courses =  courseService.findAll(token);
        if(courses == null){
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("Consulta fallida",null)).build();
        }
        return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Consulta exitosa", courses)).build();

    }

    //Obtener por ID

    /**
     * Obtenemos el Curso por su id
     * @param id
     * @return Course
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCourseById(@PathParam("id") Integer id, @HeaderParam("token") String token) {

        Course course = courseService.getCourseById(id);
        if(course == null){
            return  Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("No se encontro el curso",null)).build();
        }
        return Response.status(HttpStatus.SC_OK).entity(new MessageResponse("OK",course)).build();

    }

    //Create

    /**
     * Creamos un curso nuevo
     * @param course
     * @param token
     * @return Mensaje
     */
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCourseById(Course course,@HeaderParam("token") String token) {

        String res =  courseService.saveCourse(course,token);

        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Guardado con exito",null)).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res,null)).build();
        }
    }


    /**
     *
     * Acrtualizamos el recurso
     * @param course
     * @param token
     * @return mensaje
     */
    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourseById(Course course,@HeaderParam("token") String token) {

        String res =  courseService.updateCourse(course,token);
        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Se actualizo con exito",null)).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res,null)).build();
        }
    }

    /**
     * Elimacion lopgica de un recurso
     * @param id
     * @param token
     * @return mensaje
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCourseById(@PathParam("id") Integer id, @HeaderParam("token") String token) {

        String res =  courseService.deleteCourse(id, token);

        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_OK).entity(new MessageResponse("Se elimino con exito",null)).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res,null)).build();
        }


    }
}