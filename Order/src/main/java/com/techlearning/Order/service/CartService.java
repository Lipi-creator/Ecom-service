package com.techlearning.Order.service;

import com.techlearning.Order.clients.ProductServiceClient;
import com.techlearning.Order.clients.UserServiceClient;
import com.techlearning.Order.dto.CartItemRequest;
import com.techlearning.Order.dto.ProductResponse;
import com.techlearning.Order.dto.UserResponse;
import com.techlearning.Order.entity.CartItem;
import com.techlearning.Order.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public boolean addToCart(String userId, CartItemRequest request){

        //Look for product and required quantity
        ProductResponse productDetails = productServiceClient.getProductDetails(request.getProductId());
        if(productDetails ==null || productDetails.getStockQuantity() < request.getStockQuantity()){
            return false;
        }

        // Look for user
        UserResponse userDetails = userServiceClient.getUserDetails(userId);
        if(userDetails == null) return false;
//        Optional<User> userOptional = userRespository.findById(Long.valueOf(userId));
//        if(!userOptional.isPresent())
//            return false;
//        User user = userOptional.get();

        // Logic to add item into cart
        // 1. Item already exists in the cart, so we must update/ to existing quantity
        CartItem existingCartItem = cartRepository.findByUserIdAndProductId(userId,request.getProductId());
        if(existingCartItem != null){
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getStockQuantity());
                // we do not have access to product right now. SO lets put some dummy value
                existingCartItem.setPrice(productDetails.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
//            existingCartItem.setPrice(BigDecimal.valueOf(10000));
            cartRepository.save(existingCartItem);
        }else{
                // 2. Item must be newly added
                CartItem newCartItem = new CartItem();
                newCartItem.setUserId(userId);
                newCartItem.setProductId(request.getProductId());
                newCartItem.setQuantity(request.getStockQuantity());
//                newCartItem.setPrice(BigDecimal.valueOf(10000));
                newCartItem.setPrice(productDetails.getPrice().multiply(BigDecimal.valueOf(request.getStockQuantity())));
                cartRepository.save(newCartItem);
            }

            return true;
    }

    public boolean deleteFromCart(String userId, String productId) {
//        Optional<User> userOptional = userRespository.findById(Long.valueOf(userId));
//        Optional<Product> productOptional = productRepository.findById(productId);

        CartItem cartItem = cartRepository.findByUserIdAndProductId(userId,productId);

//        if(userOptional.isPresent() && productOptional.isPresent()){
        if(cartItem!=null){
//            cartRepository.deleteByUserAndProduct(userOptional.get(),productOptional.get());
            cartRepository.delete(cartItem);
            return true;
        }

        return false;
    }

    public List<CartItem> getCart(String userId) {

//        return userRespository.findById(Long.valueOf(userId))
//                .map(cartRepository::findByUser)
//                .orElseGet(List::of);

        return cartRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
//        userRespository.findById(Long.valueOf(userId))
//                .ifPresent( user -> cartRepository.deleteByUser(user));

        cartRepository.deleteByUserId(userId);
    }
}
