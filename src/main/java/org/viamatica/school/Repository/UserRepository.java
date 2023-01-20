package org.viamatica.school.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.viamatica.school.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User,Integer> {

    /*public User findByUserName(String _user){
        return find(_user).firstResult();
    }

     */
}
