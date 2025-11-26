package alfarius.goida.web4.repository;

import alfarius.goida.web4.exceptions.NoSuchLoginException;
import alfarius.goida.web4.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@ApplicationScoped
public class UserRepository {


    @PersistenceContext(unitName = "main")
    private EntityManager em;

    public User findUserByLogin(String name) throws NoSuchLoginException {
        System.out.println(name);

        String jpql = "SELECT u FROM User u WHERE u.login = :name";

        try {

            TypedQuery<User> query = em.createQuery(jpql, User.class);


            query.setParameter("name", name);


            return query.getSingleResult();

        } catch (NoResultException e) {

            throw new NoSuchLoginException();
        }
    }
}