package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.payload.CategoryRequestDTO;
import com.ecommerce.sbecom.payload.CategoryResponseDTO;

public interface CategoryService {
    CategoryResponseDTO getAllCategories();
    CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryRequestDTO deleteCategory(Long categoryId);
    CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId);
}
