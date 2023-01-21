package com.johanapp.ecommerce.service;

import java.util.Optional;

import com.johanapp.ecommerce.model.Usuario;

public interface IUsuarioService {
	
	Optional<Usuario> findById(Integer id);
		
	
}
