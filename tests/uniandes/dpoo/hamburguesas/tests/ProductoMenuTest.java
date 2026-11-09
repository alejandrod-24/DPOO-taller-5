package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


public class ProductoMenuTest
{
    private ProductoMenu producto;

    @BeforeEach
    void setUp( )
    {
        producto = new ProductoMenu( "corral", 14000 );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", producto.getNombre( ), "El nombre del producto no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, producto.getPrecio( ), "El precio del producto no es el esperado." );
    }

    @Test
    void testGenerarTextoFactura( )
    {
        String esperado = "corral\n" + "            14000\n";
        assertEquals( esperado, producto.generarTextoFactura( ), "El texto de la factura no es el esperado." );
    }

    @Test
    void testGenerarTextoFacturaIncluyeNombreYPrecio( )
    {
        ProductoMenu gaseosa = new ProductoMenu( "gaseosa", 5000 );
        String factura = gaseosa.generarTextoFactura( );
        assertTrue( factura.contains( "gaseosa" ), "La factura debe incluir el nombre del producto." );
        assertTrue( factura.contains( "5000" ), "La factura debe incluir el precio del producto." );
    }
}
