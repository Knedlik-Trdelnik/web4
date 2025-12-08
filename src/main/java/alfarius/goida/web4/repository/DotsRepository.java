package alfarius.goida.web4.repository;

import alfarius.goida.web4.models.Dot;
import alfarius.goida.web4.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class  DotsRepository {

    @PersistenceContext(unitName = "main")
    private EntityManager em;

    public List<Dot> getDotsByUser(User user) {
        String jpql = "SELECT d from Dot d where d.userId = :id";
        TypedQuery<Dot> query = em.createQuery(jpql, Dot.class);
        query.setParameter("id", user.getId());
        return query.getResultList();
    }


    public List<Dot> getDotsByPagesAndUser(int page, User user) {

        String jpql = "SELECT d from Dot d where d.userId = :id";
        TypedQuery<Dot> query = em.createQuery(jpql, Dot.class);
        query.setParameter("id", user.getId());
        return query.setFirstResult((page - 1) * 10)
                .setMaxResults(10)
                .getResultList();
    }

    @Transactional
    public void addDot(Dot dot) {
        em.persist(dot);
    }
}
