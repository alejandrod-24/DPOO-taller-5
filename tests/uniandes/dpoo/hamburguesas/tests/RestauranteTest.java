package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class RestauranteTest
{
    private Restaurante restaurante;

    @BeforeEach
    void setUp()
    {
        restaurante = new Restaurante();
    }

    private void cargarDatosValidos() throws Exception {
        restaurante.cargarInformacionRestaurante(
            new File("data/ingredientes.txt"),
            new File("data/menu.txt"),
            new File("data/combos.txt"));
    }

    @Test
    @DisplayName("Un restaurante nuevo esta vacio")
    void testRestauranteVacio()
    {
    	assertAll("Restaurante vacio",
    			() -> assertTrue(restaurante.getIngredientes().isEmpty(), "Un restaurante nuevo no deberia tener ingredientes"),
    			() -> assertTrue(restaurante.getMenuBase().isEmpty(), "Un restaurante nuevo no deberia tener productos en el menu"),
    			() -> assertTrue(restaurante.getMenuCombos().isEmpty(), "Un restaurante nuevo no deberia tener combos"),
    			() -> assertTrue(restaurante.getPedidos().isEmpty(), "Un restaurante nuevo no deberia tener pedidos"),
    			() -> assertNull(restaurante.getPedidoEnCurso(), "Un restaurante nuevo no deberia tener un pedido en curso"));
    }

    @Test
    @DisplayName("Cargar archivos validos llena las tres listas")
    void testCargarInformacionValida() throws Exception {
        cargarDatosValidos();

        assertAll("Carga de datos validos",
            () -> assertEquals(15, restaurante.getIngredientes().size(), "El numero de ingredientes cargados no es el esperado"),
            () -> assertEquals(22, restaurante.getMenuBase().size(), "El numero de productos del menu no es el esperado"),
            () -> assertEquals(4, restaurante.getMenuCombos().size(), "El numero de combos no es el esperado"));
    }

    @Test
    @DisplayName("Los ingredientes se cargan con nombre y costo correctos")
    void testCargarIngredientes() throws Exception {
        cargarDatosValidos();

        Ingrediente primero = restaurante.getIngredientes().get(0);
        Ingrediente ultimo = restaurante.getIngredientes().get(14);

        assertAll("Datos de los ingredientes",
            () -> assertEquals("lechuga", primero.getNombre(), "El nombre del primer ingrediente no es el esperado"),
            () -> assertEquals(1000, primero.getCostoAdicional(), "El costo del primer ingrediente no es el esperado"),
            () -> assertEquals("piña", ultimo.getNombre(), "El nombre del ultimo ingrediente no es el esperado"),
            () -> assertEquals(2500, ultimo.getCostoAdicional(), "El costo del ultimo ingrediente no es el esperado"));
    }

    @Test
    @DisplayName("Los productos del menu se cargan con nombre y precio correctos")
    void testCargarMenu() throws Exception {
        cargarDatosValidos();

        ProductoMenu primero = restaurante.getMenuBase().get(0);
        ProductoMenu ultimo = restaurante.getMenuBase().get(21);

        assertAll("Datos del menu",
            () -> assertEquals("corral", primero.getNombre(), "El nombre del primer producto no es el esperado"),
            () -> assertEquals(14000, primero.getPrecio(), "El precio del primer producto no es el esperado"),
            () -> assertEquals("gaseosa", ultimo.getNombre(), "El nombre del ultimo producto no es el esperado"),
            () -> assertEquals(5000, ultimo.getPrecio(), "El precio del ultimo producto no es el esperado"));
    }

    @Test
    @DisplayName("Los combos se cargan con nombre y precio con descuento correctos")
    void testCargarCombos() throws Exception {
    	cargarDatosValidos();
    	
    	Combo primero = restaurante.getMenuCombos().get(0);
    	Combo ultimo = restaurante.getMenuCombos().get(3);
    	
    	assertAll("Datos del combo",
    			() -> assertEquals("combo corral", primero.getNombre(), "El nombre del primer combo no es el esperado"),
    			() -> assertEquals(22050, primero.getPrecio(), "El precio del primer combo no es el esperado"),
    			() -> assertEquals("combo especial", ultimo.getNombre(), "El nombre del ultimo combo no es el esperado"),
    			() -> assertEquals(31222, ultimo.getPrecio(), "El precio del ultimo combo no es el esperado"));
    }

    @Test
    @DisplayName("Las lineas vacias de los archivos se ignoran")
    void testCargarConLineasVacias() throws Exception {
        restaurante.cargarInformacionRestaurante(
            new File("datosPrueba/ingredientesConVacias.txt"),
            new File("datosPrueba/menuConVacias.txt"),
            new File("datosPrueba/combosConVacias.txt"));

        assertAll("Carga ignorando lineas vacias",
            () -> assertEquals(2, restaurante.getIngredientes().size(), "Las lineas vacias no deberian contarse como ingredientes"),
            () -> assertEquals(3, restaurante.getMenuBase().size(), "Las lineas vacias no deberian contarse como productos"),
            () -> assertEquals(1, restaurante.getMenuCombos().size(), "Las lineas vacias no deberian contarse como combos"));
    }

    @Test
    @DisplayName("Un ingrediente repetido lanza IngredienteRepetidoException")
    void testIngredienteRepetido() {
        assertThrows(IngredienteRepetidoException.class, () -> restaurante.cargarInformacionRestaurante(
																			                             new File("datosPrueba/ingredientesRepetidos.txt"),
																			                             new File("data/menu.txt"),
																			                             new File("data/combos.txt")),
                     																					"Un ingrediente repetido deberia lanzar IngredienteRepetidoException");
    }

    @Test
    @DisplayName("Un producto repetido en el menu lanza ProductoRepetidoException")
    void testProductoRepetidoMenu()
    {
        assertThrows(ProductoRepetidoException.class, () -> restaurante.cargarInformacionRestaurante(
        																							  new File("data/ingredientes.txt"),
        																							  new File("datosPrueba/menuRepetidos.txt"),
        																							  new File("data/combos.txt")),
        																							 "Un producto repetido en el menu deberia lanzar ProductoRepetidoException");
    }

    @Test
    @DisplayName("Un combo repetido lanza ProductoRepetidoException")
    void testComboRepetido()
    {
        assertThrows(ProductoRepetidoException.class, () -> restaurante.cargarInformacionRestaurante(
        																							  new File("data/ingredientes.txt"),
        																							  new File("data/menu.txt"),
        																							  new File("datosPrueba/combosRepetidos.txt")),
        																							 "Un combo repetido deberia lanzar ProductoRepetidoException");
    }

    @Test
    @DisplayName("Un combo con un producto que no esta en el menu lanza ProductoFaltanteException")
    void testProductoFaltanteEnCombo()
    {
        assertThrows(ProductoFaltanteException.class, () -> restaurante.cargarInformacionRestaurante(
                new File("data/ingredientes.txt"),
                new File("data/menu.txt"),
                new File("datosPrueba/combosProductoFaltante.txt")),
                "Un combo con un producto que no esta en el menu deberia lanzar ProductoFaltanteException");
    }

    @Test
    @DisplayName("Cargar un archivo que no existe lanza FileNotFoundException")
    void testArchivoNoExiste()
    {
        assertThrows(FileNotFoundException.class, () -> restaurante.cargarInformacionRestaurante(
                new File("datosPrueba/archivoQueNoExiste.txt"),
                new File("data/menu.txt"),
                new File("data/combos.txt")),
                "Cargar un archivo que no existe deberia lanzar FileNotFoundException");
    }

    @Test
    @DisplayName("Iniciar un pedido lo deja como pedido en curso")
    void testIniciarPedido() throws Exception {
        restaurante.iniciarPedido("Alejandro", "Cra 15a #150-22");

        assertAll("Pedido en curso",
            () -> assertNotNull(restaurante.getPedidoEnCurso(), "Despues de iniciar un pedido deberia haber un pedido en curso"),
            () -> assertEquals("Alejandro", restaurante.getPedidoEnCurso().getNombreCliente(), "El pedido en curso no es del cliente esperado"));
    }
    
    @Test
    @DisplayName("Iniciar un pedido cuando ya hay uno en curso lanza YaHayUnPedidoEnCursoException")
    void testIniciarPedidoConPedidoEnCurso() throws Exception {
        restaurante.iniciarPedido("Alejandro", "Cra 15a #150-22");

        assertThrows(YaHayUnPedidoEnCursoException.class, () -> restaurante.iniciarPedido("Antonia", "Calle 100 #10-20"), "Iniciar un pedido con otro en curso deberia lanzar YaHayUnPedidoEnCursoException");
        assertEquals("Alejandro", restaurante.getPedidoEnCurso().getNombreCliente(), "El pedido en curso no deberia cambiar al fallar el segundo inicio");
    }

    @Test
    @DisplayName("Cerrar sin pedido en curso lanza NoHayPedidoEnCursoException")
    void testCerrarSinPedido() {
        assertThrows(NoHayPedidoEnCursoException.class,
                     () -> restaurante.cerrarYGuardarPedido(), "Cerrar sin un pedido en curso deberia lanzar NoHayPedidoEnCursoException");
    }

    @Test
    @DisplayName("Cerrar un pedido guarda la factura y lo pasa al historico")
    void testCerrarYGuardarPedido() throws Exception
    {
        cargarDatosValidos();
        restaurante.iniciarPedido("Alejandro", "Cra 15a #150-22");
        Pedido pedido = restaurante.getPedidoEnCurso();
        pedido.agregarProducto(restaurante.getMenuBase().get(0));
        File factura = new File("./facturas/factura_" + pedido.getIdPedido() + ".txt");

        try
        {
            restaurante.cerrarYGuardarPedido();

            assertAll("Cerrar y guardar pedido",
                () -> assertNull(restaurante.getPedidoEnCurso(), "Despues de cerrar no deberia haber pedido en curso"),
                () -> assertTrue(factura.exists(), "No se creo el archivo de la factura"),
                () -> assertTrue(restaurante.getPedidos().contains(pedido), "El pedido cerrado deberia quedar en el historico de pedidos"));
        }
        finally
        {
            factura.delete();
        }
    }
    //testCerrarYGuardarPedido fue hecho con la ayuda de claude
}


