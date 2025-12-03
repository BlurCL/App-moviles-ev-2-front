package com.example.app_catalogo_front

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.app_catalogo_front.data.Producto
import com.example.app_catalogo_front.network.ApiService
import com.example.app_catalogo_front.network.IndicadoresApi
import com.example.app_catalogo_front.network.RetrofitIndicadores
import com.example.app_catalogo_front.network.RetrofitInstance
import com.example.app_catalogo_front.viewmodel.CatalogoViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private val apiServiceMock = mockk<ApiService>(relaxed = true) // relaxed=true evita errores si olvidamos mockear algo
    private val indicadoresApiMock = mockk<IndicadoresApi>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mockeamos los Singletons (Objetos globales)
        mockkObject(RetrofitInstance)
        every { RetrofitInstance.api } returns apiServiceMock

        mockkObject(RetrofitIndicadores)
        every { RetrofitIndicadores.api } returns indicadoresApiMock
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `agregarProducto deberia actualizar la lista`() = runTest {
        // 1. DATO DE PRUEBA
        val productoNuevo = Producto(1, "Test", "Pastel Prueba", BigDecimal("5000"), null)
        val listaConProducto = listOf(productoNuevo)

        // 2. CONFIGURAR MOCKS (Simulamos la API)
        // Cuando pidan productos, devolvemos lista vacía primero
        coEvery { apiServiceMock.getProductos() } returns emptyList()
        // Cuando creen producto, devolvemos el mismo producto
        coEvery { apiServiceMock.crearProducto(any()) } returns productoNuevo
        // Cuando pidan productos DE NUEVO (al recargar), devolvemos la lista con el producto
        coEvery { apiServiceMock.getProductos() } returnsMany listOf(emptyList(), listaConProducto)

        // 3. EJECUTAR
        val viewModel = CatalogoViewModel()
        advanceUntilIdle() // Esperar carga inicial

        viewModel.agregarProducto(productoNuevo) { }
        advanceUntilIdle() // Esperar a que se agregue

        // 4. VERIFICAR
        // Verificamos que la lista en el ViewModel ahora tiene 1 elemento
        assertEquals(1, viewModel.productos.value.size)
        // Verificamos que sea el producto correcto
        assertEquals("Pastel Prueba", viewModel.productos.value[0].nombre)
    }
}