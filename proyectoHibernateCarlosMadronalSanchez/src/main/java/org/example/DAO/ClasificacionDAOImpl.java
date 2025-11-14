package org.example.DAO;
import org.example.Util.HibernateUtil;
import org.example.entities.Animal;
import org.example.entities.Clasificacion;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;


public class ClasificacionDAOImpl {

    public List<Clasificacion> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Clasificacion", Clasificacion.class).list();
        }
    }


    public Clasificacion findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Clasificacion.class, id);
        }
    }

    public Clasificacion create(Clasificacion clasificacion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(clasificacion);
            tx.commit();
            return clasificacion;
        }
    }

    public Clasificacion update(Clasificacion clasificacion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(clasificacion);
            tx.commit();
            return clasificacion;
        }
    }

    public void delete(Clasificacion clasificacion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(clasificacion);
            tx.commit();
        }
    }


}
