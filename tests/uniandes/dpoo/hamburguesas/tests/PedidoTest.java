package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.io.TempDir;

class PedidoTest {

	@TempDir
	File carpetaTemporal;

	private ProductoMenu corral;
	private ProductoMenu papasMedianas;
	private ProductoMenu hawaiana;
	private Pedido pedido;
	private ProductoMenu mexicana;

	@BeforeEach
	void setUp() {
		corral = new ProductoMenu("corral", 14000);
		papasMedianas = new ProductoMenu("papas medianas", 5500);
		hawaiana = new ProductoMenu("hawaiana", 20000);


		pedido = new Pedido("Alejandro", "Cra 15a #150-22");
		pedido.agregarProducto(corral);
		pedido.agregarProducto(papasMedianas);
		pedido.agregarProducto(hawaiana);
	}

	@Test
	@DisplayName("Validar id")
	void validarId() {
		// numeroPedidos es static: lo comparten todas las pruebas, asi que el id exacto depende
		// del orden en que JUnit las corra. Lo que si se puede verificar es que dos pedidos
		// creados seguidos reciban ids consecutivos.
		Pedido otroPedido = new Pedido("Antonia", "Calle 100 #10-20");
		assertEquals(pedido.getIdPedido() + 1, otroPedido.getIdPedido(), "Los ids de pedidos consecutivos deberian ser consecutivos");
	}

	@Test
	@DisplayName("Validar nombre del cliente")
	void validarNombre() {
		assertEquals("Alejandro", pedido.getNombreCliente(), "El nombre del cliente no es el esperado");
	}

	@Test
	@DisplayName("Validar agrear un producto")
	void validarAgregar() {
		int totalAntes = pedido.getPrecioTotalPedido();
		mexicana = new ProductoMenu("mexicana", 22000);
		pedido.agregarProducto(mexicana);

		assertTrue(pedido.getPrecioTotalPedido() > totalAntes, "El precio total deberia aumentar al agregar un producto");
		assertEquals(73185, pedido.getPrecioTotalPedido(), "El precio con el nuevo agregado no es el esperado");
		assertTrue(pedido.generarTextoFactura().contains("mexicana"), "El producto agregado deberia aparecer en la factura");
	}

	@Test
	@DisplayName("Validar precio total")
	void validarTotal() {
		assertEquals(47005, pedido.getPrecioTotalPedido(), "El precio total no es el esperado");
	}

	@Test
	@DisplayName("Validar texto de la factura")
	void validarFactura() {
		String esperado = "Cliente: Alejandro\n"
						+ "Dirección: Cra 15a #150-22\n"
						+ "----------------\n"
						+ "corral\n"
						+ "            14000\n"
						+ "papas medianas\n"
						+ "            5500\n"
						+ "hawaiana\n"
						+ "            20000\n"
						+ "----------------\n"
						+ "Precio Neto:  39500\n"
						+ "IVA:          7505\n"
						+ "Precio Total: 47005\n";

		assertEquals(esperado, pedido.generarTextoFactura(), "La factura del pedido no es la esperada");
	}

	@Test
	@DisplayName("Guardar factura en una ruta invalida lanza excepcion")
	void validarGuardarFacturaRutaInvalida() {
	    File archivoImposible = new File(carpetaTemporal, "carpetaQueNoExiste/factura.txt");

	    assertThrows(FileNotFoundException.class,
	                 () -> pedido.guardarFactura(archivoImposible),
	                 "Deberia lanzar FileNotFoundException si no puede crear el archivo");
	}

	@Test
	@DisplayName("Guardar factura en archivo")
	void validarGuardarFactura() throws IOException {
		File archivo = new File(carpetaTemporal, "factura.txt");

		pedido.guardarFactura(archivo);

		assertTrue(archivo.exists(), "El archivo de la factura no fue creado");
		String contenido = Files.readString(archivo.toPath());
		assertEquals(pedido.generarTextoFactura(), contenido, "El contenido del archivo no es el esperado");
	}

	// Hecho con ayuda de Claude: validarGuardarFactura  y validarGuardarFacturaInvalida
}
