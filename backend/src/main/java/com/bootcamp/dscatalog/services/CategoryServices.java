package com.bootcamp.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bootcamp.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServices {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		//USANDO STREAM E MAP PARA DTO
		List<Category>  list = categoryRepository.findAll();
		return list.stream().map(cat -> new CategoryDTO(cat)).collect(Collectors.toList());
	
		//USANDO FOREACH PARA DTO
		//List<CategoryDTO>  dto = new ArrayList<>();
//		for (Category category : categoryRepository.findAll()) {
//			dto.add(new CategoryDTO(category));
//		}
//		return dto;
	}
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(()-> new EntityNotFoundException("Objeto nao encontrado"));
		return entityParaDTO(entity);
		
	}

	public CategoryDTO save(CategoryDTO categoryDTO){
		Category obj = new Category();
		obj.setName(categoryDTO.getName());
		return entityParaDTO(categoryRepository.save(obj));
	}

	public CategoryDTO entityParaDTO(Category category){
		return new CategoryDTO(category.getId(), category.getName());
	}
	
	

}
