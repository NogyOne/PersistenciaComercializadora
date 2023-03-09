package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Pedidosproductos;
import modelo.Remisionesproductos;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Productos.class)
public class Productos_ { 

    public static volatile CollectionAttribute<Productos, Pedidosproductos> pedidosproductosCollection;
    public static volatile SingularAttribute<Productos, Integer> precio;
    public static volatile CollectionAttribute<Productos, Remisionesproductos> remisionesproductosCollection;
    public static volatile SingularAttribute<Productos, Integer> idProducto;
    public static volatile SingularAttribute<Productos, Integer> cantidad;

}