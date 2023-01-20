package org.viamatica.school.Repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.viamatica.school.model.Course;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CourseRepository implements PanacheRepositoryBase<Course,Integer> {
}
