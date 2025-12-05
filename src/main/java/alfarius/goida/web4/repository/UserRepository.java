package alfarius.goida.web4.repository;

import alfarius.goida.web4.exceptions.NameIsAlreadyExistException;
import alfarius.goida.web4.exceptions.NoSuchLoginException;
import alfarius.goida.web4.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "main")
    private EntityManager em;

    public User findUserByLogin(String name) throws NoSuchLoginException {
        System.out.println("Ищем человека по имени");
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

    @Transactional
    public void addUser(User user) throws NameIsAlreadyExistException {
        try {
            em.persist(user);
        } catch (PersistenceException e) {
            throw new NameIsAlreadyExistException();
        }

    }
}