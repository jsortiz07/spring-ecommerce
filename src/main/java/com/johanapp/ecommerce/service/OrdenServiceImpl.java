package com.johanapp.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johanapp.ecommerce.model.Orden;
import com.johanapp.ecommerce.model.Usuario;
import com.johanapp.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenrepository;
	
	@Override
	public Orden save(Orden orden) {
		// TODO Auto-generated method stub
		return ordenrepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return ordenrepository.findAll();
	}
	
	@Override
	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado="";
		
		List<Orden> ordenes = findAll();
		List<Integer> numeros = new ArrayList<Integer>();
		
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if (ordenes.isEmpty()) {
			numero = 1;
		}else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if (numero <10 ) {
			numeroConcatenado = "000000000"+String.valueOf(numero);
		}else if (numero < 100) {
			numeroConcatenado = "00000000"+String.valueOf(numero);
		}else if (numero < 1000 ) {
			numeroConcatenado = "0000000"+String.valueOf(numero);
		}else if (numero <10000) {
			numeroConcatenado = "000000"+String.valueOf(numero);

		}
		return numeroConcatenado;
	}

	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenrepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenrepository.findById(id);
	}

}
