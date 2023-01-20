package org.viamatica.school.Service;

import org.viamatica.school.Repository.UserRepository;
import org.viamatica.school.model.Token;
import org.viamatica.school.model.User;

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
    @Transactional
    public List<User> findAll() {
        return userRepository.listAll();
    }
    @Transactional
    public User getUserById(Integer id) {
        return userRepository.findById(id);
    }

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
            int idRol = Integer.valueOf(_user.substring(_user.lastIndexOf(",")+1, _user.indexOf("]")));

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
