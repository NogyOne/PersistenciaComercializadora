/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author germa
 */
@Entity
@Table(name = "remisionesproductos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remisionesproductos.findAll", query = "SELECT r FROM Remisionesproductos r")
    , @NamedQuery(name = "Remisionesproductos.findByIdremisionProducto", query = "SELECT r FROM Remisionesproductos r WHERE r.idremisionProducto = :idremisionProducto")
    , @NamedQuery(name = "Remisionesproductos.findByIdRemision", query = "SELECT r FROM Remisionesproductos r WHERE r.idRemision = :idRemision")})
public class Remisionesproductos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_remisionProducto")
    private Integer idremisionProducto;
    @Basic(optional = false)
    @Column(name = "id_remision")
    private int idRemision;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne(cascade = CascadeType.ALL)
    private Productos idProducto;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "remisionesproductos")
    private Remisiones remisiones;

    public Remisionesproductos() {
    }

    public Remisionesproductos(Integer idremisionProducto) {
        this.idremisionProducto = idremisionProducto;
    }

    public Remisionesproductos(Integer idremisionProducto, int idRemision) {
        this.idremisionProducto = idremisionProducto;
        this.idRemision = idRemision;
    }

    public Integer getIdremisionProducto() {
        return idremisionProducto;
    }

    public void setIdremisionProducto(Integer idremisionProducto) {
        this.idremisionProducto = idremisionProducto;
    }

    public int getIdRemision() {
        return idRemision;
    }

    public void setIdRemision(int idRemision) {
        this.idRemision = idRemision;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    public Remisiones getRemisiones() {
        return remisiones;
    }

    public void setRemisiones(Remisiones remisiones) {
        this.remisiones = remisiones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idremisionProducto != null ? idremisionProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remisionesproductos)) {
            return false;
        }
        Remisionesproductos other = (Remisionesproductos) object;
        if ((this.idremisionProducto == null && other.idremisionProducto != null) || (this.idremisionProducto != null && !this.idremisionProducto.equals(other.idremisionProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Remisionesproductos[ idremisionProducto=" + idremisionProducto + " ]";
    }
    
}
