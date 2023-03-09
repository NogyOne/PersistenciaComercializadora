/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Remisionesproductos;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Remisiones;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author germa
 */
public class RemisionesJpaController implements Serializable {

    public RemisionesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public RemisionesJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Remisiones remisiones) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Remisionesproductos remisionesproductosOrphanCheck = remisiones.getRemisionesproductos();
        if (remisionesproductosOrphanCheck != null) {
            Remisiones oldRemisionesOfRemisionesproductos = remisionesproductosOrphanCheck.getRemisiones();
            if (oldRemisionesOfRemisionesproductos != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Remisionesproductos " + remisionesproductosOrphanCheck + " already has an item of type Remisiones whose remisionesproductos column cannot be null. Please make another selection for the remisionesproductos field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remisionesproductos remisionesproductos = remisiones.getRemisionesproductos();
            if (remisionesproductos != null) {
                remisionesproductos = em.getReference(remisionesproductos.getClass(), remisionesproductos.getIdremisionProducto());
                remisiones.setRemisionesproductos(remisionesproductos);
            }
            em.persist(remisiones);
            if (remisionesproductos != null) {
                remisionesproductos.setRemisiones(remisiones);
                remisionesproductos = em.merge(remisionesproductos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRemisiones(remisiones.getIdRemision()) != null) {
                throw new PreexistingEntityException("Remisiones " + remisiones + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Remisiones remisiones) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remisiones persistentRemisiones = em.find(Remisiones.class, remisiones.getIdRemision());
            Remisionesproductos remisionesproductosOld = persistentRemisiones.getRemisionesproductos();
            Remisionesproductos remisionesproductosNew = remisiones.getRemisionesproductos();
            List<String> illegalOrphanMessages = null;
            if (remisionesproductosNew != null && !remisionesproductosNew.equals(remisionesproductosOld)) {
                Remisiones oldRemisionesOfRemisionesproductos = remisionesproductosNew.getRemisiones();
                if (oldRemisionesOfRemisionesproductos != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Remisionesproductos " + remisionesproductosNew + " already has an item of type Remisiones whose remisionesproductos column cannot be null. Please make another selection for the remisionesproductos field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (remisionesproductosNew != null) {
                remisionesproductosNew = em.getReference(remisionesproductosNew.getClass(), remisionesproductosNew.getIdremisionProducto());
                remisiones.setRemisionesproductos(remisionesproductosNew);
            }
            remisiones = em.merge(remisiones);
            if (remisionesproductosOld != null && !remisionesproductosOld.equals(remisionesproductosNew)) {
                remisionesproductosOld.setRemisiones(null);
                remisionesproductosOld = em.merge(remisionesproductosOld);
            }
            if (remisionesproductosNew != null && !remisionesproductosNew.equals(remisionesproductosOld)) {
                remisionesproductosNew.setRemisiones(remisiones);
                remisionesproductosNew = em.merge(remisionesproductosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = remisiones.getIdRemision();
                if (findRemisiones(id) == null) {
                    throw new NonexistentEntityException("The remisiones with id " + id + " no longer exists.");
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
            Remisiones remisiones;
            try {
                remisiones = em.getReference(Remisiones.class, id);
                remisiones.getIdRemision();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The remisiones with id " + id + " no longer exists.", enfe);
            }
            Remisionesproductos remisionesproductos = remisiones.getRemisionesproductos();
            if (remisionesproductos != null) {
                remisionesproductos.setRemisiones(null);
                remisionesproductos = em.merge(remisionesproductos);
            }
            em.remove(remisiones);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Remisiones> findRemisionesEntities() {
        return findRemisionesEntities(true, -1, -1);
    }

    public List<Remisiones> findRemisionesEntities(int maxResults, int firstResult) {
        return findRemisionesEntities(false, maxResults, firstResult);
    }

    private List<Remisiones> findRemisionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Remisiones.class));
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

    public Remisiones findRemisiones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Remisiones.class, id);
        } finally {
            em.close();
        }
    }

    public int getRemisionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Remisiones> rt = cq.from(Remisiones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
