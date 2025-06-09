package com.techlearning.Order.controller;

import com.techlearning.Order.dto.CartItemRequest;
import com.techlearning.Order.entity.CartItem;
import com.techlearning.Order.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request){

        if(!cartService.addToCart(userId,request)){
            return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
        }else{
//            return ResponseEntity.status(HttpStatus.CREATED).build();
            return ResponseEntity.ok("Item added to cart");
        }
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable String productId){

        boolean deleted = cartService.deleteFromCart(userId,productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    //Get cart items for a given user
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
