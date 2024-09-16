package com.example.products.controller;

import com.example.products.model.Product;
import com.example.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String getAllProducts(Model model) {
        logger.info("Был выполнен запрос на получение списка продуктов");
        model.addAttribute("products", productRepository.findAll());

        return "products";
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        logger.info("Был выполнен запрос на получение продукта с id " + id);
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        model.addAttribute("product", product.get());

        return "product_edit";
    }

    @GetMapping("/create")
    public String createProduct() {
        return "product_create";
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public String createProduct(Product product) {
        Product createdProduct = productRepository.save(product);

        return "redirect:/products/" + createdProduct.getId();
    }

    @PutMapping(path = "/{id}", consumes = "application/x-www-form-urlencoded")
    public String updateProduct(
            @PathVariable Long id,
            Product productDetails
    ) {
        logger.info("Продукт с id " + id + " был обновлен");

        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());

        productRepository.save(product);

        return "redirect:/products/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);

        return "redirect:/products";
    }
}
