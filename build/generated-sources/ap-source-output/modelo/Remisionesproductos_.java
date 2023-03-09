package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Productos;
import modelo.Remisiones;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2023-03-08T23:08:16")
@StaticMetamodel(Remisionesproductos.class)
public class Remisionesproductos_ { 

    public static volatile SingularAttribute<Remisionesproductos, Integer> idRemision;
    public static volatile SingularAttribute<Remisionesproductos, Remisiones> remisiones;
    public static volatile SingularAttribute<Remisionesproductos, Productos> idProducto;
    public static volatile SingularAttribute<Remisionesproductos, Integer> idremisionProducto;

}