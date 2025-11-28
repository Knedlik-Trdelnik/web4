package alfarius.goida.web4.repository;

import alfarius.goida.web4.models.Dot;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

@ApplicationScoped
public class DotsRepository {

    @PersistenceContext(unitName = "main")
    private EntityManager em;

    public List<Dot> getDots() {
        Query query = em.createQuery("SELECT d from Dot d");
        return query.getResultList();
    }
}
