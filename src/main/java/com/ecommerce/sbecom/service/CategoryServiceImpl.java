package com.ecommerce.sbecom.service;

import com.ecommerce.sbecom.exceptions.APIException;
import com.ecommerce.sbecom.exceptions.ResourceNotFoundException;
import com.ecommerce.sbecom.model.Category;
import com.ecommerce.sbecom.payload.CategoryRequestDTO;
import com.ecommerce.sbecom.payload.CategoryResponseDTO;
import com.ecommerce.sbecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No categories created till now.");
        }
        List<CategoryRequestDTO> categoriesDTO = categories.stream()
                .map(category -> modelMapper.map(category, CategoryRequestDTO.class)).toList();
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setCategoryResponse(categoriesDTO);
        return categoryResponseDTO;
    }

    @Override
    public CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category categoryEntity = modelMapper.map(categoryRequestDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName());
        if (categoryFromDB != null) {
            throw new APIException("Category with name " + categoryRequestDTO.getCategoryName() + " already exists !!");
        }
        Category savedCategory = categoryRepository.save(categoryEntity);
        return modelMapper.map(savedCategory,  CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category","categoryId",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryRequestDTO.class);
    }

    @Override
    public CategoryRequestDTO updateCategory(CategoryRequestDTO categoryRequestDTO, Long categoryId) {
        Category updatedCategory = categoryRepository.findByCategoryName(categoryRequestDTO.getCategoryName());
        if (updatedCategory != null) {
            throw new APIException("Category with name " + categoryRequestDTO.getCategoryName() + " already exists !!");
        }
        categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));
        Category savedCategory = modelMapper.map(categoryRequestDTO, Category.class);
        savedCategory.setCategoryId(categoryId);
        categoryRepository.save(savedCategory);
        return modelMapper.map(savedCategory,  CategoryRequestDTO.class);
    }
}
