
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Clientes;
import modelo.Pedidos;
import modelo.Pedidosproductos;
import modelo.Productos;
import persistencia.ClientesJpaController;
import persistencia.PedidosJpaController;
import persistencia.PedidosproductosJpaController;
import persistencia.ProductosJpaController;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;


public class CamaronsizaInsaneTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ClientesJpaController clientesJpa = new ClientesJpaController();
        PedidosJpaController pedidosJpa = new PedidosJpaController();
        ProductosJpaController productosJpa = new ProductosJpaController();
        PedidosproductosJpaController pedidosProd = new PedidosproductosJpaController();
        
        ArrayList<Pedidosproductos> pedsProdsLista = new ArrayList();
        
        Clientes cliente = new Clientes(1,"Erick", "Bernal");
        //clientesJpa.create(cliente);

        Productos camaron = new Productos(1, "Camaron", 2, 60);
        Productos pulpo = new Productos(2, "Pulpo", 3, 30);
        Productos cayo = new Productos(3, "Cayo", 4, 70);
        Productos mojarra = new Productos(4, "Mojarra", 5, 80);
        Productos ostion = new Productos(5, "Ostion", 6, 90);
        
//        productosJpa.create(camaron);
//        productosJpa.create(pulpo);
//        productosJpa.create(cayo);
//        productosJpa.create(mojarra);
//        productosJpa.create(ostion);
        
        Pedidos pedido = new Pedidos();
        pedido.setPrecioTotal(2000);
        pedido.setIdCliente(cliente);
//        System.out.println(pedido.toString());
//        pedidosJpa.create(pedido);     
//        

       
        pedido.setIdPedido(9);
        try {
            pedidosJpa.destroy(pedido.getIdPedido());
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(CamaronsizaInsaneTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CamaronsizaInsaneTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Pedidosproductos pedidosProductos = new Pedidosproductos();
        pedidosProductos.setIdpedidoProducto(13);
//        Pedidosproductos pedidosProductos2 = new Pedidosproductos();
//        Pedidosproductos pedidosProductos3 = new Pedidosproductos();
//        Pedidosproductos pedidosProductos4 = new Pedidosproductos();
//        pedsProdsLista.add(pedidosProductos1);
//        pedsProdsLista.add(pedidosProductos2);
//        pedsProdsLista.add(pedidosProductos3);
//        pedsProdsLista.add(pedidosProductos4);
//        try {
            //        pedidosProductos.setIdPedido(pedido);
//        pedidosProductos.setIdProducto(camaron);
//        pedidosProductos.setCantidad(2);
//        pedidosProd.create(pedidosProductos);
//        
//        pedidosProductos.setIdProducto(cayo);
//        pedidosProductos.setCantidad(2);
//        pedidosProd.create(pedidosProductos);
//        
//        pedidosProductos.setIdProducto(mojarra);
//        pedidosProductos.setCantidad(5);
//        pedidosProd.create(pedidosProductos);
//        
//        pedidosProductos.setIdProducto(pulpo);
//        pedidosProductos.setCantidad(6);

         //pedidosProd.destroy(pedidosProductos.getIdpedidoProducto());


//pedidosJpa.edit(pedido);
//        } catch (NonexistentEntityException ex) {
//            Logger.getLogger(CamaronsizaInsaneTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
}
