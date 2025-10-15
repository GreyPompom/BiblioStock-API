// package com.BiblioStock.BiblioStock_API.service;

// import com.BiblioStock.BiblioStock_API.model.Product;
// import com.BiblioStock.BiblioStock_API.repository.ProductRepository;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// public class ProductService {

//     private final ProductRepository repository;

//     public ProductService(ProductRepository repository) {
//         this.repository = repository;
//     }

//     public List<Product> getAllProducts() {
//         return repository.findAll();
//     }

//     public Optional<Product> getProductById(Long id) {
//         return repository.findById(id);
//     }

//     public Product createProduct(Product product) {
//         product.setCreatedAt(java.time.LocalDateTime.now());
//         return repository.save(product);
//     }

//     public Product updateProduct(Long id, Product productDetails) {
//         Product product = repository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Produto não encontrado com id " + id));

//         product.setName(productDetails.getName());
//         product.setIsbn(productDetails.getIsbn());
//         product.setPrice(productDetails.getPrice());
//         product.setStockQty(productDetails.getStockQty());
//         product.setMinQty(productDetails.getMinQty());
//         product.setMaxQty(productDetails.getMaxQty());
//         product.setUnit(productDetails.getUnit());
//         product.setPublisher(productDetails.getPublisher());
//         product.setCategory(productDetails.getCategory());

//         return repository.save(product);
//     }

//     public void deleteProduct(Long id) {
//         Product product = repository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Produto não encontrado com id " + id));

//         // Aqui você poderia verificar se existem movimentações vinculadas
//         repository.delete(product);
//     }
// }
