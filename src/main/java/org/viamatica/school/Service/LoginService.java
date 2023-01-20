package org.viamatica.school.Service;

import com.google.gson.Gson;
import org.viamatica.school.Repository.UserRepository;
import org.viamatica.school.model.Course;
import org.viamatica.school.model.Token;
import org.viamatica.school.model.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequestScoped
public class LoginService {

    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager em;

    @Inject
    Token tokenJwt;


    /**
     * login():
     * Funcion de validacion de mi usuario
     *
     * @param user
     * @param pass
     * @return
     * @throws String: token
     */

    @Transactional
    public String login(String user, String pass ) throws SQLException {
        String output = "";
        User user123 = userRepository.find("userName = ?1 and userPassword = ?2", user, pass).firstResult();

        if(user123 == null){
            return output;
        }

        Gson gson= new Gson();
        String token = gson.toJson(user123);
        output = tokenJwt.generateToken(token);

        return output;
    }
}
