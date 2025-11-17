// org/example/DAO/AnimalDAOImpl.java
package org.example.DAO;

import org.example.Util.HibernateUtil;
import org.example.entities.Animal;
import org.example.entities.Clasificacion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AnimalDAOImpl implements AnimalDAO {

    @Override
    public List<Animal> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Animal", Animal.class).list();
        }
    }

    @Override
    public Animal findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT a FROM Animal a LEFT JOIN FETCH a.persona WHERE a.id = :id",
                            Animal.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    @Override
    public List<Animal> findByNombre(String nombre) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Animal WHERE nombre = :nombre", Animal.class)
                    .setParameter("nombre", nombre)
                    .list();
        }
    }

    @Override
    public List<Animal> findByEspecie(String especie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT a FROM Animal a LEFT JOIN FETCH a.persona WHERE a.especie = :especie",
                            Animal.class)
                    .setParameter("especie", especie)
                    .list();
        }
    }

    /*
    ✅ JOIN FETCH le dice a Hibernate:
     "carga también la Persona en la misma consulta".
    Ahora, cuando accedas a a.getPersona().getNombre(),
    la Persona ya estará cargada, aunque la sesión esté cerrada.
    */

    @Override
    public Animal create(Animal animal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            // ❌ session.persist(animal);
            // ✅ Usa merge para manejar entidades transient Y detached
            animal = session.merge(animal);
            tx.commit();
            return animal;
        }
    }

    /*
    * Tercer animal: OVNIVORO + MAMIFERO → ¡ya existe! → tu código
    * la busca y la reutiliza → pero esa Clasificacion
    * está detached → al usar persist(animal), Hibernate
    * intenta persistir la Clasificacion de nuevo → error.
    * */

    @Override
    public Animal update(Animal animal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(animal);
            tx.commit();
            return animal;
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Animal animal = session.get(Animal.class, id);
            if (animal != null) {
                session.remove(animal);
                tx.commit();
                return true;
            }
            return false;
        }
    }
}