package com.techlearning.User.service;

import com.techlearning.User.dto.UserRequest;
import com.techlearning.User.dto.UserResponse;
import com.techlearning.User.entity.Address;
import com.techlearning.User.dto.AddressDTO;
import com.techlearning.User.entity.User;
import com.techlearning.User.repository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRespository userRespository;
    private Long userCount = 1L;

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public List<UserResponse> getAllUsers(){
        log.info("Fetching all users");
        return userRespository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getUser(Long id) {
        log.info("Fetch user by id");
        return userRespository.findById(id).map(this::mapToUserResponse);
    }

    public void addUser(@RequestBody UserRequest userRequest){
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRespository.save(user);
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest) {

        return userRespository.findById(id)
                .map( existingUser -> {
                    updateUserFromRequest(existingUser,updatedUserRequest);
                    userRespository.save(existingUser);
                    return true;
                        }
                ).orElse(false);
    }

    // This method should have the param user because when we call the update api,
    // we must populate the user that was found earlier else there should be an error for update api scenario
    User updateUserFromRequest(User user, UserRequest userRequest){

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddressDTO() != null){
            Address address = new Address();
            address.setStreet(userRequest.getAddressDTO().getStreet());
            address.setCity(userRequest.getAddressDTO().getCity());
            address.setCountry(userRequest.getAddressDTO().getCountry());
            address.setZipcode(userRequest.getAddressDTO().getZipcode());

            user.setAddress(address);
        }

        return user;
    }

    UserResponse mapToUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        if(user.getAddress()!= null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }

    public void deleteUser(Long id) {
        userRespository.deleteById(id);
    }
}
