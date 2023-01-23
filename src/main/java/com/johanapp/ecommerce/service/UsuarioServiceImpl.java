package com.johanapp.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johanapp.ecommerce.model.Usuario;
import com.johanapp.ecommerce.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

	@Autowired
	private IUsuarioRepository usuariorepository;

	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuariorepository.findById(id);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuariorepository.save(usuario);
	} 
	
	
	
}
