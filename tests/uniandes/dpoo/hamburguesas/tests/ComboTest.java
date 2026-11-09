package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


class ComboTest {

	private Combo combo;
	private ArrayList<ProductoMenu> itemsCombo;
	private ProductoMenu corral;
	private ProductoMenu papasMedianas;
	private ProductoMenu gaseosa;
	
	@BeforeEach
	void setUp() {
		corral = new ProductoMenu("corral", 14000);
		papasMedianas = new ProductoMenu("papas medianas", 5500);
		gaseosa = new ProductoMenu("gaseosa", 5000);
		
		itemsCombo = new ArrayList<>();
		itemsCombo.add(corral);
		itemsCombo.add(papasMedianas);
		itemsCombo.add(gaseosa);
		
		combo = new Combo("corral", 0.1, itemsCombo);
	}
	
	@Test
	@DisplayName("Validar Nombre")
	void validarNombre() {
		assertEquals("corral", combo.getNombre(), "El nombre del combo no es el esperado");
	}
	
	@Test
	@DisplayName("Validar precio")
	void validarPrecio() {
		assertEquals(22050, combo.getPrecio(), "El precio del combo no es el esperado");
	}
	
	@Test
	@DisplayName("Validar factura")
	void validarFactura() {
		String esperado = "Combo corral\n"
						+ " Descuento: 0.1\n"
						+ "            22050\n";
		
		assertEquals(esperado, combo.generarTextoFactura(), "El texto de la factura no es el esperado");
	}
	
		
		
}


