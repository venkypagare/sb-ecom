package com.ecommerce.sbecom.controller;

import com.ecommerce.sbecom.model.User;
import com.ecommerce.sbecom.payload.AddressDTO;
import com.ecommerce.sbecom.service.AddressService;
import com.ecommerce.sbecom.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    private final AddressService addressService;
    private final AuthUtil authUtil;

    public AddressController(AddressService addressService, AuthUtil authUtil) {
        this.addressService = addressService;
        this.authUtil = authUtil;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> saveUserAddresses(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO address = addressService.saveUserAddress(addressDTO, user);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressList = addressService.getAddress();
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressList = addressService.getUserAddress(user);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@RequestBody AddressDTO addressDTO,
                                                          @PathVariable Long addressId) {
        AddressDTO updatedAddress = addressService.updateAddress(addressDTO, addressId);
        return new ResponseEntity<>(updatedAddress, HttpStatus.CREATED);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressByUserId(@PathVariable Long addressId) {
        String status = addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
