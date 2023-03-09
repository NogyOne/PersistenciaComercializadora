/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Entregas;
import modelo.Ventas;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author germa
 */
public class EntregasJpaController implements Serializable {

    public EntregasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntregasJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entregas entregas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas idVenta = entregas.getIdVenta();
            if (idVenta != null) {
                idVenta = em.getReference(idVenta.getClass(), idVenta.getIdVenta());
                entregas.setIdVenta(idVenta);
            }
            em.persist(entregas);
            if (idVenta != null) {
                idVenta.getEntregasCollection().add(entregas);
                idVenta = em.merge(idVenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entregas entregas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entregas persistentEntregas = em.find(Entregas.class, entregas.getIdEntrega());
            Ventas idVentaOld = persistentEntregas.getIdVenta();
            Ventas idVentaNew = entregas.getIdVenta();
            if (idVentaNew != null) {
                idVentaNew = em.getReference(idVentaNew.getClass(), idVentaNew.getIdVenta());
                entregas.setIdVenta(idVentaNew);
            }
            entregas = em.merge(entregas);
            if (idVentaOld != null && !idVentaOld.equals(idVentaNew)) {
                idVentaOld.getEntregasCollection().remove(entregas);
                idVentaOld = em.merge(idVentaOld);
            }
            if (idVentaNew != null && !idVentaNew.equals(idVentaOld)) {
                idVentaNew.getEntregasCollection().add(entregas);
                idVentaNew = em.merge(idVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entregas.getIdEntrega();
                if (findEntregas(id) == null) {
                    throw new NonexistentEntityException("The entregas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entregas entregas;
            try {
                entregas = em.getReference(Entregas.class, id);
                entregas.getIdEntrega();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entregas with id " + id + " no longer exists.", enfe);
            }
            Ventas idVenta = entregas.getIdVenta();
            if (idVenta != null) {
                idVenta.getEntregasCollection().remove(entregas);
                idVenta = em.merge(idVenta);
            }
            em.remove(entregas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entregas> findEntregasEntities() {
        return findEntregasEntities(true, -1, -1);
    }

    public List<Entregas> findEntregasEntities(int maxResults, int firstResult) {
        return findEntregasEntities(false, maxResults, firstResult);
    }

    private List<Entregas> findEntregasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entregas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Entregas findEntregas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entregas.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entregas> rt = cq.from(Entregas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
