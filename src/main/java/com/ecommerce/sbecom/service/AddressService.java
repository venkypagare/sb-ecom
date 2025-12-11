package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.model.User;
import com.ecommerce.sbecom.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO saveUserAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddress();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddress(User user);

    AddressDTO updateAddress(AddressDTO addressDTO, Long addressId);

    String deleteAddressById(Long addressId);
}
