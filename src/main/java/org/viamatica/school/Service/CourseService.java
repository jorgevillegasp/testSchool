package org.viamatica.school.Service;

import org.apache.http.HttpStatus;
import org.viamatica.school.Repository.CourseRepository;
import org.viamatica.school.model.Course;
import org.viamatica.school.model.Token;
import org.viamatica.school.utility.Conf;
import org.viamatica.school.utility.MessageResponse;
import org.viamatica.school.utility.Metodos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class CourseService {

    @Inject
    CourseRepository courseRepository;
    @Inject
    Token tokenJwt;

    Metodos metodosUtil = new Metodos();

    @Transactional
    public List<Course> findAll() {
        return courseRepository.listAll();
    }
    @Transactional
    public Course getCourseById(Integer id) {
        return courseRepository.findById(id);
    }
    @Transactional
    public String saveCourse(Course course, String token) {

        try{

            if(course.getTitle().length() >= 100){
                return "El titulo es muy largo";
            }
            if(course.getTitle().length() >= 250){
                return "La descripcion es muy larga";
            }
            if(tokenJwt.isTokenExpired(token)){
                return "El token a expirado";
            }
            int idRol = metodosUtil.lastValue(tokenJwt.getUsernameFromToken(token));

            if(idRol == 2) {
                return  "Usted no tiene permiso de agregar un curso";
            }
            //Validamos si es admin
            if(idRol == 1) {
                courseRepository.persist(course);
                return "ok";
            }

        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
            return "Error : "+e;
        }
        return "No se a podido almacenar";
    }
    @Transactional
    public String updateCourse(Course course, String token) {
        try{
            if(courseRepository.findById(course.getId()) == null){
                return "El curso no existe";
            }
            courseRepository.persist(course);
            return "ok";
        }catch (Exception e){
            return ("Error : "+e);
        }
    }
    @Transactional
    public String deleteCourse(Integer id, String token){

        try{
            if(tokenJwt.isTokenExpired(token)){
                return "Su token a expirado";
            }

            //Extraemos el ultimo resultado (es aquel que me trae el id del rol)
            int idRol = metodosUtil.lastValue(tokenJwt.getUsernameFromToken(token));


            if(idRol == 2){
                return "Usted no tiene permisos de ingresar un curso";
            }

            //Validamos si es admin
            if(idRol == 1){
                Course course = courseRepository.findById(id);

                if(course == null){
                    return "El curso no existe";
                }

                //Validamos si el curso ya esta eliminado (Inactivo)
                if(course.getStatus().equals("I")){
                    return "El curso que dea eliminar no esta en la lista de activos";
                }

                course.setStatus("I");
                courseRepository.persist(course);
                courseRepository.flush();
                return "ok";
            }

        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
            return "Error : "+e;
        }

        return "";
    }




}
