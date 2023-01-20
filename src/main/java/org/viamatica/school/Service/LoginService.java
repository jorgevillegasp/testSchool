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




    @Transactional
    public String login(String user, String pass ) throws SQLException {

        String output = "";

        StoredProcedureQuery query = em.createStoredProcedureQuery("spLogin");
        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);

        query.setParameter(1,user );
        query.setParameter(2, pass);

        if(query.execute())
        {
            List o= query.getResultList();
           if(o.size()>0)
           {
               Gson gson= new Gson();
               String token = gson.toJson(o.get(0));
               output = tokenJwt.generateToken(token);
           }
        }
        return output;
    }
}
