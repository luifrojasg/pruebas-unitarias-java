package co.edu.notas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * ENTREGA DEL TALLER
 * Nombre completo: Luisa Fernanda Rojas Gonzalez
 * 1. Bug encontrado (método y línea de CarritoCompras.java):
 *    agregarProducto, línea 24. Decía "cantidad < 0" y dejaba agregar productos con cantidad 0.
 *    Lo cambié por "cantidad <= 0" porque R1 dice que la cantidad debe ser mayor a 0.
 * 2. Prueba que lo detectó (nombre del método):
 *    agregarProducto_cantidadCero_lanzaExcepcion
 * 3. Sugerencia de la IA que rechacé o corregí, y por qué
 *    (si todas eran correctas: cómo verifiqué una de ellas con las reglas):
 *    Las pruebas que propuso la IA eran correctas. Verifiqué a mano la del total con SENA10
 *    usando R3 y R6: Monitor de 500.000, descuento del 10 % = 50.000, (500.000 - 50.000) x 1,19
 *    = 535.500. Como es mayor o igual a 150.000 el envío es gratis, entonces el total es 535.500.
 *    También revisé que la prueba de cantidad 0 esperara una excepción como dice R1 y no lo que
 *    hacía el código, que dejaba agregar el producto.
 */
class CarritoComprasTest {

    private CarritoCompras carrito;

    @BeforeEach
    void setUp() { carrito = new CarritoCompras(); }

    @Test
    @DisplayName("Subtotal = precio × cantidad de cada producto")
    void calcularSubtotal_variosProductos_sumaCorrecta() {
        // Arrange
        carrito.agregarProducto("Cuaderno", 8_000, 3);
        carrito.agregarProducto("Esfero", 2_000, 5);
        // Act
        double subtotal = carrito.calcularSubtotal();
        // Assert
        assertEquals(34_000, subtotal, 0.001);
    }

    @Test
    @DisplayName("Agregar un producto válido aumenta los items")
    void agregarProducto_productoValido_aumentaItems() {
        carrito.agregarProducto("Cuaderno", 8_000, 1);
        assertEquals(1, carrito.cantidadDeItems());
    }

    @Test
    @DisplayName("Carrito vacío tiene subtotal 0")
    void calcularSubtotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularSubtotal(), 0.001);
    }

    @Test
    @DisplayName("Carrito vacío tiene total 0")
    void calcularTotal_carritoVacio_retornaCero() {
        assertEquals(0, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Nombre vacío lanza excepción")
    void agregarProducto_nombreVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("", 8_000, 1));
    }

    @Test
    @DisplayName("Nombre nulo lanza excepción")
    void agregarProducto_nombreNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto(null, 8_000, 1));
    }

    @Test
    @DisplayName("Precio 0 lanza excepción")
    void agregarProducto_precioCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 0, 1));
    }

    @Test
    @DisplayName("Cantidad 0 lanza excepción")
    void agregarProducto_cantidadCero_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 8_000, 0));
    }

    @Test
    @DisplayName("Cantidad negativa lanza excepción")
    void agregarProducto_cantidadNegativa_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.agregarProducto("Cuaderno", 8_000, -1));
    }

    @Test
    @DisplayName("SENA10 descuenta el 10 % del subtotal")
    void calcularDescuento_cuponSena10_descuentaDiezPorCiento() {
        carrito.agregarProducto("Libro", 100_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(10_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO con subtotal de 50.000 descuenta 5.000")
    void calcularDescuento_bienvenidoCon50000_descuenta5000() {
        carrito.agregarProducto("Mochila", 50_000, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(5_000, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("BIENVENIDO con subtotal de 49.999 no descuenta")
    void calcularDescuento_bienvenidoCon49999_noDescuenta() {
        carrito.agregarProducto("Mochila", 49_999, 1);
        carrito.aplicarCupon("BIENVENIDO");
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Sin cupón no hay descuento")
    void calcularDescuento_sinCupon_retornaCero() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(0, carrito.calcularDescuento(), 0.001);
    }

    @Test
    @DisplayName("Cupón inválido lanza excepción")
    void aplicarCupon_cuponInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> carrito.aplicarCupon("GRATIS"));
    }

    @Test
    @DisplayName("Total menor a 150.000 con IVA paga envío")
    void calcularTotal_menorA150000_pagaEnvio() {
        carrito.agregarProducto("Libro", 100_000, 1);
        assertEquals(131_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total de 150.000 o más con IVA tiene envío gratis")
    void calcularTotal_mayorA150000_envioGratis() {
        carrito.agregarProducto("Calculadora", 200_000, 1);
        assertEquals(238_000, carrito.calcularTotal(), 0.001);
    }

    @Test
    @DisplayName("Total con cupón SENA10")
    void calcularTotal_conCuponSena10_aplicaDescuentoEIva() {
        carrito.agregarProducto("Monitor", 500_000, 1);
        carrito.aplicarCupon("SENA10");
        assertEquals(535_500, carrito.calcularTotal(), 0.001);
    }
}
