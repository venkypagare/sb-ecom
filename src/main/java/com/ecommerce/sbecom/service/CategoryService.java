package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.payload.CategoryRequestDTO;
import com.ecommerce.sbecom.payload.CategoryResponseDTO;

public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pageNumber,  Integer pageSize, String sortBy, String sortOrder);
    CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryRequestDTO deleteCategory(Long categoryId);
    CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId);
}
