package modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Entregas;
import modelo.Pedidos;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Ventas.class)
public class Ventas_ { 

    public static volatile SingularAttribute<Ventas, Integer> precioVenta;
    public static volatile CollectionAttribute<Ventas, Entregas> entregasCollection;
    public static volatile SingularAttribute<Ventas, Pedidos> idPedido;
    public static volatile SingularAttribute<Ventas, Integer> idVenta;
    public static volatile SingularAttribute<Ventas, Date> fechaVenta;

}