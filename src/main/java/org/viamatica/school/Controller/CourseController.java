package org.viamatica.school.Controller;

import org.apache.http.HttpStatus;

import org.viamatica.school.Service.CourseService;
import org.viamatica.school.model.Course;
import org.viamatica.school.utility.MessageResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1")
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
    @Path("course")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getAllCourse() {
        return courseService.findAll();
    }

    //Obtener por ID

    /**
     * Obtenemos el Curso por su id
     * @param id
     * @return Course
     */
    @GET
    @Path("/course/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getCourseById(@PathParam("id") Integer id) {
        return courseService.getCourseById(id);
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
    public Response createCourseById(Course course,@HeaderParam("key") String token) {

        if(course.getTitle().length() >= 100){
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("El titulo es muy largo")).build();
        }
        if(course.getTitle().length() >= 250){
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("La descripcion es muy larga")).build();
        }

        String res =  courseService.saveCourse(course,token);

        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Guardado con exito")).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("No se pudo guardar el curso")).build();
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
    @Path("/course/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCourseById(Course course,String token) {

        if(course.getTitle().length() >= 100){
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("El titulo es muy largo")).build();
        }
        if(course.getTitle().length() >= 250){
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse("La descripcion es muy larga")).build();
        }

        String res =  courseService.updateCourse(course,token);
        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_CREATED).entity(new MessageResponse("Se actualizo con exito")).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res)).build();
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
    public Response deleteCourseById(@PathParam("id") Integer id, @HeaderParam("key") String token) {

        String res =  courseService.deleteCourse(id, token);

        if(res.equals("ok")){
            return Response.status(HttpStatus.SC_OK).entity(new MessageResponse("Se elimino con exito")).build();
        }else{
            return Response.status(HttpStatus.SC_CONFLICT).entity(new MessageResponse(res)).build();
        }


    }
}