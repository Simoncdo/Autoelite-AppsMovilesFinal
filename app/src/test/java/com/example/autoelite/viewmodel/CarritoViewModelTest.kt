package com.example.autoelite.viewmodel

import com.example.autoelite.data.model.Auto
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CarritoViewModelTest {

    private lateinit var viewModel: CarritoViewModel
    private val auto1 = Auto(id = 1L, marca = "Tesla", modelo = "Modelo S", precio = 75000L)
    private val auto2 = Auto(id = 2L, marca = "Ford", modelo = "Mustang", precio = 55000L)

    @Before
    fun setUp() {
        viewModel = CarritoViewModel()
    }

    @Test
    fun `agregar un nuevo auto al carrito aumenta la lista de items`() {
        viewModel.agregarAlCarrito(auto1)
        assertEquals(1, viewModel.items.value.size)
        assertEquals(auto1.id, viewModel.items.value.first().auto.id)
    }

    @Test
    fun `agregar un auto existente solo incrementa su cantidad`() {
        viewModel.agregarAlCarrito(auto1)
        viewModel.agregarAlCarrito(auto1)
        assertEquals(1, viewModel.items.value.size)
        assertEquals(2, viewModel.items.value.first().cantidad)
    }

    @Test
    fun `remover del carrito elimina el item`() {
        viewModel.agregarAlCarrito(auto1)
        val item = viewModel.items.value.first()
        viewModel.removerDelCarrito(item)
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `actualizar cantidad cambia la cantidad del item`() {
        viewModel.agregarAlCarrito(auto1)
        val item = viewModel.items.value.first()
        viewModel.actualizarCantidad(item, 5)
        assertEquals(5, viewModel.items.value.first().cantidad)
    }

    @Test
    fun `actualizar cantidad a cero remueve el item`() {
        viewModel.agregarAlCarrito(auto1)
        val item = viewModel.items.value.first()
        viewModel.actualizarCantidad(item, 0)
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `vaciar carrito elimina todos los items`() {
        viewModel.agregarAlCarrito(auto1)
        viewModel.agregarAlCarrito(auto2)
        viewModel.vaciarCarrito()
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `el total es cero cuando el carrito esta vacio`() {
        assertEquals(0.0, viewModel.total, 0.0)
    }

    @Test
    fun `calcular total con un solo item`() {
        viewModel.agregarAlCarrito(auto1)
        assertEquals(75000.0, viewModel.total, 0.0)
    }

    @Test
    fun `calcular total con multiples items y cantidades`() {
        viewModel.agregarAlCarrito(auto1) // 75000
        viewModel.agregarAlCarrito(auto1) // 75000 * 2
        viewModel.agregarAlCarrito(auto2) // 55000
        val expectedTotal = (75000.0 * 2) + 55000.0
        assertEquals(expectedTotal, viewModel.total, 0.0)
    }

    @Test
    fun `remover un item actualiza el total correctamente`() {
        viewModel.agregarAlCarrito(auto1)
        viewModel.agregarAlCarrito(auto2)
        val item2 = viewModel.items.value.find { it.auto.id == auto2.id }!!
        viewModel.removerDelCarrito(item2)
        assertEquals(75000.0, viewModel.total, 0.0)
    }
}
