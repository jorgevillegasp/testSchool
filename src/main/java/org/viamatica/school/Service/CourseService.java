package org.viamatica.school.Service;

import org.viamatica.school.Repository.CourseRepository;
import org.viamatica.school.model.Course;
import org.viamatica.school.model.Token;
import org.viamatica.school.utility.Metodos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CourseService {

    @Inject
    CourseRepository courseRepository;
    @Inject
    Token tokenJwt;

    Metodos metodosUtil = new Metodos();


    /**
     * findAll():
     * Funcion que me devuelve todos
     * los registro de la tabal COURSE de la base de datos
     *
     * @param token
     * @return String
     */
    @Transactional
    public List<Course> findAll(String token) {

        try {
            if (tokenJwt.isTokenExpired(token)) {
                return null;
            }

            List<Course> data =  courseRepository.find("status","A").list();

            return data;

        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
            return null;
        }
    }



    /**
     * getCourseById():
     * Funcion que devuelve un registro a traves
     * de su codigo(id)
     *
     * @param id
     * @return Objeto curso
     */
    @Transactional
    public Course getCourseById(Integer id) {
        return courseRepository.find("id = ?1 and status = ?2", id, "A").firstResult();
    }



    /**
     * saveCourse(): Almacena un registro a la base de datos
     * de la tabla COURSE.
     *
     * @param course: resgistro
     * @param token: llave
     * @return Strgin
     */
    @Transactional
    public String saveCourse(Course course, String token) {

        try{
            if(tokenJwt.isTokenExpired(token)){
                return "El token a expirado";
            }
            if(course.getTitle().length() >= 100){
                return "El titulo es muy largo";
            }
            if(course.getTitle().length() >= 250){
                return "La descripcion es muy larga";
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




    /**
     * updateCourse(): Funcion que actualiza un registro
     * de la tabla curso.
     *
     * @param course: Recurso que se desea actualizar
     * @param token
     * @return String
     */
    @Transactional
    public String updateCourse(Course course, String token) {
        try{
            if(tokenJwt.isTokenExpired(token)){
                return "Su token a expirado";
            }
            if(courseRepository.findById(course.getId()) == null){
                return "El curso no existe";
            }
            courseRepository.persist(course);
            return "ok";
        }catch (Exception e){
            return ("Error : "+e);
        }
    }


    /**
     * deleteCourse()
     * Eliminacion de un recurso de mi tabla COURSE
     *
     * @param id: Codigo del curso que se desea eliminar
     * @param token: Clave
     * @return String
     */
    @Transactional
    public String deleteCourse(Integer id, String token){

        try{
            if(tokenJwt.isTokenExpired(token)){
                return "Su token a expirado";
            }

            //mandamos a convertir el token para extraer su idRol
            int idRol = metodosUtil.lastValue(tokenJwt.getUsernameFromToken(token));


            if(idRol == 2){
                return "Usted no tiene permisos de eliminar un curso";
            }

            //Validamos si es admin
            if(idRol == 1){
                Course course = courseRepository.findById(id);

                if(course == null){
                    return "El curso no existe";
                }

                //Validamos si el curso ya esta eliminado (Inactivo)
                if(course.getStatus().equals("I")){
                    return "El curso que desea eliminar no esta en la lista de activos";
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
