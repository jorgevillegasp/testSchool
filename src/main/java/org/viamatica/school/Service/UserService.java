package org.viamatica.school.Service;

import org.viamatica.school.Repository.UserRepository;
import org.viamatica.school.model.Token;
import org.viamatica.school.model.User;
import org.viamatica.school.utility.Metodos;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;
    @Inject
    Token tokenJwt;
    Metodos metodosUtil = new Metodos();

    /**
     * findAll():
     * Funcion que devuelte todos los registros
     * de mi tabla USER
     *
     * @param token
     * @return User
     */
    @Transactional
    public List<User> findAll(String token) {
        try {
            if (tokenJwt.isTokenExpired(token)) {
                return null;
            }

            return userRepository.listAll();

        }catch (Exception e){
            return null;
        }

    }

    /**
     * getUserById():
     * Funcion que devuelve un registro de mi base de datos
     * desde su ID o clave
     *
     * @param id
     * @return
     */
    @Transactional
    public User getUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * saveUser():
     * Funcion que almacena mi almacena un registro
     * en la tabla USER de mi base de datos
     *
     * @param user
     * @param token
     * @return String
     */

    @Transactional
    public String saveUser(User user, String token) {

        try{
            //Valido si el token no a caducado
            boolean isExpireValue = tokenJwt.isTokenExpired(token);

            if(isExpireValue){
                return "El token a expirado";
            }

            String _user = tokenJwt.getUsernameFromToken(token);

            //Extraemos el ultimo resultado (es aquel que me trae el id del rol)
            int idRol = metodosUtil.lastValue(tokenJwt.getUsernameFromToken(token));

            if(idRol == 2) {
                return "Usted no tiene permiso para agregar un usuario";
            }

            //Validamos si es admin
            if(idRol == 1) {
                //Creamos el recurso
                userRepository.persist(user);
                return "ok";
            }

        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
            return "Error : "+e;
        }
        return "No se guardo";
    }
}
