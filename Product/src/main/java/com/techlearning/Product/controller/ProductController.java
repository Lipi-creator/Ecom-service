package com.techlearning.Product.controller;

import com.techlearning.Product.dto.ProductRequest;
import com.techlearning.Product.dto.ProductResponse;
import com.techlearning.Product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id){
       Optional<ProductResponse> productResponse = productService.getProductById(Long.valueOf(id));
       if(productResponse.isPresent()){
           return ResponseEntity.ok(productResponse.get());
       }else{
           return ResponseEntity.notFound().build();
       }
    }

    @PostMapping("/add")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
        return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> createProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest productRequest){
        return productService.updateProduct(id,productRequest)
                .map(productResponse -> ResponseEntity.ok(productResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    //*** SEARCH Feature***
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){
        return  ResponseEntity.ok(productService.searchProducts(keyword));
    }

}
