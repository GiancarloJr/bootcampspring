package com.bootcamp.dscatalog.services;

import com.bootcamp.dscatalog.dto.CategoryDTO;
import com.bootcamp.dscatalog.dto.RoleDTO;
import com.bootcamp.dscatalog.dto.UserDTO;
import com.bootcamp.dscatalog.entities.Category;
import com.bootcamp.dscatalog.entities.Role;
import com.bootcamp.dscatalog.entities.User;
import com.bootcamp.dscatalog.repository.CategoryRepository;
import com.bootcamp.dscatalog.repository.RoleRepository;
import com.bootcamp.dscatalog.repository.UserRepository;
import com.bootcamp.dscatalog.services.exceptions.DataBaseException;
import com.bootcamp.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class UserServices {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable){
		//USANDO STREAM E MAP PARA DTO
		Page<User>  list = userRepository.findAll(pageable);
		return list.map(cat -> new UserDTO(cat));
	
		//USANDO FOREACH PARA DTO
		//List<UserDTO>  dto = new ArrayList<>();
//		for (User User : UserRepository.findAll()) {
//			dto.add(new UserDTO(User));
//		}
//		return dto;
	}
	@Transactional(readOnly = true)
	public UserDTO findById(Long id){
		Optional<User> obj = userRepository.findById(id);
		User entity = obj.orElseThrow(()-> new ResourceNotFoundException("Objeto nao encontrado"));
		return new UserDTO(entity);
	}
	@Transactional
	public UserDTO save(UserDTO userDTO){
		User obj = new User();
		copyDtoToEntity(userDTO, obj);
		return new UserDTO(userRepository.save(obj));
	}

	private void copyDtoToEntity(UserDTO userDTO,User entity){
		entity.setFirstName(userDTO.getFirstName());
		entity.setLastName(userDTO.getLastName());
		entity.setEmail(userDTO.getEmail());

		entity.getRoles().clear();
		for(RoleDTO roleDTO: userDTO.getRoles()){
			Role role = roleRepository.getReferenceById(roleDTO.getId());
			entity.getRoles().add(role);
		}

	}
	@Transactional
	public UserDTO update(UserDTO UserDTO){
		try {
			Optional<User> obj = userRepository.findById(UserDTO.getId());
			copyDtoToEntity(UserDTO, obj.get());
			return new UserDTO(userRepository.save(obj.get()));
		} catch (NoSuchElementException e){
			throw new ResourceNotFoundException("Entity not found");
		}
	}
	public void delete(Long id){
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Entity not found");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integraty violation");
		}
	}

//	public UserDTO entityParaDTO(User User){
//		return new UserDTO(User);
//	}


}
