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
import modelo.Productos;
import modelo.Remisiones;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Remisionesproductos;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author germa
 */
public class RemisionesproductosJpaController implements Serializable {

    public RemisionesproductosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public RemisionesproductosJpaController() {
        emf = Persistence.createEntityManagerFactory("DatosCamaroncizaPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Remisionesproductos remisionesproductos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Productos idProducto = remisionesproductos.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                remisionesproductos.setIdProducto(idProducto);
            }
            Remisiones remisiones = remisionesproductos.getRemisiones();
            if (remisiones != null) {
                remisiones = em.getReference(remisiones.getClass(), remisiones.getIdRemision());
                remisionesproductos.setRemisiones(remisiones);
            }
            em.persist(remisionesproductos);
            if (idProducto != null) {
                idProducto.getRemisionesproductosCollection().add(remisionesproductos);
                idProducto = em.merge(idProducto);
            }
            if (remisiones != null) {
                Remisionesproductos oldRemisionesproductosOfRemisiones = remisiones.getRemisionesproductos();
                if (oldRemisionesproductosOfRemisiones != null) {
                    oldRemisionesproductosOfRemisiones.setRemisiones(null);
                    oldRemisionesproductosOfRemisiones = em.merge(oldRemisionesproductosOfRemisiones);
                }
                remisiones.setRemisionesproductos(remisionesproductos);
                remisiones = em.merge(remisiones);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRemisionesproductos(remisionesproductos.getIdremisionProducto()) != null) {
                throw new PreexistingEntityException("Remisionesproductos " + remisionesproductos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Remisionesproductos remisionesproductos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remisionesproductos persistentRemisionesproductos = em.find(Remisionesproductos.class, remisionesproductos.getIdremisionProducto());
            Productos idProductoOld = persistentRemisionesproductos.getIdProducto();
            Productos idProductoNew = remisionesproductos.getIdProducto();
            Remisiones remisionesOld = persistentRemisionesproductos.getRemisiones();
            Remisiones remisionesNew = remisionesproductos.getRemisiones();
            List<String> illegalOrphanMessages = null;
            if (remisionesOld != null && !remisionesOld.equals(remisionesNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Remisiones " + remisionesOld + " since its remisionesproductos field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                remisionesproductos.setIdProducto(idProductoNew);
            }
            if (remisionesNew != null) {
                remisionesNew = em.getReference(remisionesNew.getClass(), remisionesNew.getIdRemision());
                remisionesproductos.setRemisiones(remisionesNew);
            }
            remisionesproductos = em.merge(remisionesproductos);
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getRemisionesproductosCollection().remove(remisionesproductos);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getRemisionesproductosCollection().add(remisionesproductos);
                idProductoNew = em.merge(idProductoNew);
            }
            if (remisionesNew != null && !remisionesNew.equals(remisionesOld)) {
                Remisionesproductos oldRemisionesproductosOfRemisiones = remisionesNew.getRemisionesproductos();
                if (oldRemisionesproductosOfRemisiones != null) {
                    oldRemisionesproductosOfRemisiones.setRemisiones(null);
                    oldRemisionesproductosOfRemisiones = em.merge(oldRemisionesproductosOfRemisiones);
                }
                remisionesNew.setRemisionesproductos(remisionesproductos);
                remisionesNew = em.merge(remisionesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = remisionesproductos.getIdremisionProducto();
                if (findRemisionesproductos(id) == null) {
                    throw new NonexistentEntityException("The remisionesproductos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Remisionesproductos remisionesproductos;
            try {
                remisionesproductos = em.getReference(Remisionesproductos.class, id);
                remisionesproductos.getIdremisionProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The remisionesproductos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Remisiones remisionesOrphanCheck = remisionesproductos.getRemisiones();
            if (remisionesOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Remisionesproductos (" + remisionesproductos + ") cannot be destroyed since the Remisiones " + remisionesOrphanCheck + " in its remisiones field has a non-nullable remisionesproductos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Productos idProducto = remisionesproductos.getIdProducto();
            if (idProducto != null) {
                idProducto.getRemisionesproductosCollection().remove(remisionesproductos);
                idProducto = em.merge(idProducto);
            }
            em.remove(remisionesproductos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Remisionesproductos> findRemisionesproductosEntities() {
        return findRemisionesproductosEntities(true, -1, -1);
    }

    public List<Remisionesproductos> findRemisionesproductosEntities(int maxResults, int firstResult) {
        return findRemisionesproductosEntities(false, maxResults, firstResult);
    }

    private List<Remisionesproductos> findRemisionesproductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Remisionesproductos.class));
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

    public Remisionesproductos findRemisionesproductos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Remisionesproductos.class, id);
        } finally {
            em.close();
        }
    }

    public int getRemisionesproductosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Remisionesproductos> rt = cq.from(Remisionesproductos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
