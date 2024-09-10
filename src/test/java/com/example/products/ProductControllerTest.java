package com.example.products;

import com.example.products.controller.ProductController;
import com.example.products.model.Product;
import com.example.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepository;

	@InjectMocks
	private ProductController productController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
	}

	@Test
	void testGetAllProducts() throws Exception {
		Product product1 = new Product(1L, "Product1", "Description1", 100.0);
		Product product2 = new Product(2L, "Product2", "Description2", 200.0);
		when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

		mockMvc.perform(get("/products")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Product1"))
				.andExpect(jsonPath("$[1].name").value("Product2"));
	}

	@Test
	void testGetProductById() throws Exception {
		Product product = new Product(1L, "Product1", "Description1", 100.0);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

		mockMvc.perform(get("/products/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Product1"));
	}

	@Test
	void testCreateProduct() throws Exception {
		Product product = new Product(1L, "Product1", "Description1", 100.0);
		when(productRepository.save(any(Product.class))).thenReturn(product);

		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"Product1\",\"description\":\"Description1\",\"price\":100.0}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Product1"));
	}

	@Test
	void testUpdateProduct() throws Exception {
		Product existingProduct = new Product(1L, "Product1", "Description1", 100.0);
		Product updatedProduct = new Product(1L, "ProductUpdated", "DescriptionUpdated", 150.0);
		when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
		when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

		mockMvc.perform(put("/products/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"ProductUpdated\",\"description\":\"DescriptionUpdated\",\"price\":150.0}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("ProductUpdated"));
	}

	@Test
	void testDeleteProduct() throws Exception {
		doNothing().when(productRepository).deleteById(anyLong());

		mockMvc.perform(delete("/products/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
