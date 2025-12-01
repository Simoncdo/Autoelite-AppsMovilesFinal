package com.example.autoelite.viewmodel

import com.example.autoelite.data.model.Auto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CarritoViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CarritoViewModel

    @Before
    fun setUp() {
        viewModel = CarritoViewModel()
    }
    private val auto1 = Auto(id = 1L, marca = "Toyota", modelo = "Corolla", precio = 20000L)
    private val auto2 = Auto(id = 2L, marca = "Honda", modelo = "Civic", precio = 22000L)

    @Test
    fun `estado inicial - el carrito está vacío y la moneda es CLP`() {
        assertTrue(viewModel.items.value.isEmpty())
        assertEquals(0.0, viewModel.total, 0.0)
        assertEquals("CLP", viewModel.monedaSeleccionada.value)
        assertEquals(1.0, viewModel.factorConversion.value, 0.0)
    }

    @Test
    fun `agregarAlCarrito - cuando se agrega un auto nuevo, el item se añade a la lista`() {
        viewModel.agregarAlCarrito(auto1)
        val items = viewModel.items.value
        assertEquals(1, items.size)
        assertEquals(1L, items[0].auto.id)
        assertEquals(1, items[0].cantidad)
    }

    @Test
    fun `agregarAlCarrito - cuando se agrega un auto existente, la cantidad aumenta`() {
        viewModel.agregarAlCarrito(auto1)
        viewModel.agregarAlCarrito(auto1)
        val items = viewModel.items.value
        assertEquals(1, items.size)
        assertEquals(2, items[0].cantidad)
    }

    @Test
    fun `removerDelCarrito - cuando se elimina un item, la lista se actualiza`() {
        viewModel.agregarAlCarrito(auto1)
        val item = viewModel.items.value.first()
        viewModel.removerDelCarrito(item)
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `actualizarCantidad - cuando se cambia la cantidad, el valor se actualiza`() {
        viewModel.agregarAlCarrito(auto1)
        val item = viewModel.items.value.first()
        viewModel.actualizarCantidad(item, 5)
        assertEquals(5, viewModel.items.value.first().cantidad)
    }

    @Test
    fun `actualizarCantidad - cuando la cantidad es cero, el item se elimina`() {
        viewModel.agregarAlCarrito(auto1)
        val item = viewModel.items.value.first()
        viewModel.actualizarCantidad(item, 0)
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `vaciarCarrito - cuando se llama, la lista de items queda vacía`() {
        viewModel.agregarAlCarrito(auto1)
        viewModel.agregarAlCarrito(auto2)
        viewModel.vaciarCarrito()
        assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `total - cuando el carrito tiene items, calcula el total correctamente`() {
        viewModel.agregarAlCarrito(auto1)
        viewModel.agregarAlCarrito(auto2)
        viewModel.agregarAlCarrito(auto1)
        assertEquals(62000.0, viewModel.total, 0.0)
    }

    @Test
    fun `total - cuando el carrito está vacío, el total es cero`() {
        assertEquals(0.0, viewModel.total, 0.0)
    }

    @Test
    fun `cambiarMoneda - cuando se cambia a CLP, el factor de conversion es uno`() = runTest {
        viewModel.cambiarMoneda("USD")
        advanceUntilIdle()
        viewModel.cambiarMoneda("CLP")
        advanceUntilIdle()

        assertEquals("CLP", viewModel.monedaSeleccionada.value)
        assertEquals(1.0, viewModel.factorConversion.value, 0.0)
    }
}
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
