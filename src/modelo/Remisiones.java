/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author germa
 */
@Entity
@Table(name = "remisiones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Remisiones.findAll", query = "SELECT r FROM Remisiones r")
    , @NamedQuery(name = "Remisiones.findByIdRemision", query = "SELECT r FROM Remisiones r WHERE r.idRemision = :idRemision")
    , @NamedQuery(name = "Remisiones.findByCantidad", query = "SELECT r FROM Remisiones r WHERE r.cantidad = :cantidad")
    , @NamedQuery(name = "Remisiones.findByPrecioCompra", query = "SELECT r FROM Remisiones r WHERE r.precioCompra = :precioCompra")
    , @NamedQuery(name = "Remisiones.findByFechaEntrada", query = "SELECT r FROM Remisiones r WHERE r.fechaEntrada = :fechaEntrada")})
public class Remisiones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_remision")
    private Integer idRemision;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "precio_compra")
    private Integer precioCompra;
    @Column(name = "fecha_entrada")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrada;
    @JoinColumn(name = "id_remision", referencedColumnName = "id_remisionProducto", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Remisionesproductos remisionesproductos;

    public Remisiones() {
    }

    public Remisiones(Integer idRemision) {
        this.idRemision = idRemision;
    }

    public Integer getIdRemision() {
        return idRemision;
    }

    public void setIdRemision(Integer idRemision) {
        this.idRemision = idRemision;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Integer precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Remisionesproductos getRemisionesproductos() {
        return remisionesproductos;
    }

    public void setRemisionesproductos(Remisionesproductos remisionesproductos) {
        this.remisionesproductos = remisionesproductos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRemision != null ? idRemision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remisiones)) {
            return false;
        }
        Remisiones other = (Remisiones) object;
        if ((this.idRemision == null && other.idRemision != null) || (this.idRemision != null && !this.idRemision.equals(other.idRemision))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Remisiones[ idRemision=" + idRemision + " ]";
    }
    
}
