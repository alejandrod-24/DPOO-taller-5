package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

class ProductoAjustadoTest {

	private ProductoMenu producto;
	private ProductoAjustado productoAjustado;
	private Ingrediente tomate;
	private Ingrediente huevo;
	
	@BeforeEach
	void setUp() {
		producto = new ProductoMenu("corral", 14000);
		productoAjustado = new ProductoAjustado(producto);
		tomate = new Ingrediente("tomate", 1000);
		huevo = new Ingrediente("huevo", 2500);
	}
	
	@Test
	@DisplayName("Traer nombre del producto ajustado")
	void validarNombre() {
		assertEquals("corral", productoAjustado.getNombre(), "El nombre del producto ajustado no es el esperado");
	}
	
	@Test
	@DisplayName("Traer precio del producto ajustado")
	void validarPrecio() {
		assertEquals(14000, productoAjustado.getPrecio(), "El precio del producto ajustado no es el esperado");
	}
	
	@Test
	@DisplayName("Validar precio agregados")
	void validarAgregados() {
		productoAjustado.agregarIngrediente(huevo);
		productoAjustado.agregarIngrediente(tomate);
		
		assertEquals(17500, productoAjustado.getPrecio(), "El precio del producto ajustado no es el esperado");
	}
	
	@Test
	@DisplayName("Validar precio eliminados")
	void validarEliminados() {
		productoAjustado.eliminarIngrediente(huevo);
		productoAjustado.eliminarIngrediente(tomate);
		
		assertEquals(14000, productoAjustado.getPrecio(), "El precio del producto ajustado no es el esperado");
	}
	
	@Test
	@DisplayName("Traer texto factura")
	void validarTextoFactura() {
	    productoAjustado.eliminarIngrediente(huevo);
	    productoAjustado.agregarIngrediente(tomate);

	    String esperado = "corral\n"
	                    + "    +tomate                1000\n"
	                    + "    -huevo\n"
	                    + "            15000\n";
	    assertEquals(esperado, productoAjustado.generarTextoFactura(), "El texto de la factura no es el esperado");
	}
}
