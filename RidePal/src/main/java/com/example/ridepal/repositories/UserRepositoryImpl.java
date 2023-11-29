package com.example.ridepal.repositories;

import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl extends AbstractCrudRepository<User> implements UserRepository {
    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory, Class<User> clazz) {
        super(sessionFactory, clazz);
    }

    @Override
    public List<User> getAllByFilterOptions(UserFilterOptions userFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            userFilterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            userFilterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", String.format("%%%s%%", value));
            });

            userFilterOptions.getFirstName().ifPresent(value -> {
                filters.add("firstName like :first_name");
                params.put("first_name", String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from User");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }
}
