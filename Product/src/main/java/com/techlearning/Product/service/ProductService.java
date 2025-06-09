package com.techlearning.Product.service;

import com.techlearning.Product.dto.ProductRequest;
import com.techlearning.Product.dto.ProductResponse;
import com.techlearning.Product.entity.Product;
import com.techlearning.Product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> mapToProductResponse(product))
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id){
        return productRepository.findByIdAndActiveTrue(id).map(this::mapToProductResponse);
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setCategory(product.getCategory());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setActive(product.isActive());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setCreatedAt(product.getCreatedAt());
        if(product.getUpdatedAt() !=null){
            productResponse.setUpdatedAt(product.getUpdatedAt());
        }

        return productResponse;
    }

    public Optional<ProductResponse> updateProduct(Long id,ProductRequest productRequest) {
        return productRepository.findById(id)
                .map( product -> {
                    updateProductFromRequest(product,productRequest);
                    Product savedProduct = productRepository.save(product);
                    return mapToProductResponse(savedProduct);
                });

    }

    public boolean deleteProduct(Long id) {
        //1. Delete the product all together
        //productRepository.deleteById(id);

        //2. Just set the active flag as false and throw exception if not found. Return type of method will be void
//        Product product = productRepository.findById(id)
//                .orElseThrow(()-> new RuntimeException("Product not found"));
//        product.setActive(false);
//        productRepository.save(product);

        //3. Make the method return boolean on setting active flag false
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
