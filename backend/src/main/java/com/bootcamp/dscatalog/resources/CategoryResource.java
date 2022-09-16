package com.bootcamp.dscatalog.resources;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.services.CategoryServices;

@RestController
@RequestMapping("/category")
public class CategoryResource {

	@Autowired
	private CategoryServices categoryServices;

	@GetMapping("/")
	public ResponseEntity<List<CategoryDTO>> findAll() {
		return ResponseEntity.ok().body(categoryServices.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(categoryServices.findById(id));
	}
}