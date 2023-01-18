package com.johanapp.ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johanapp.ecommerce.model.Producto;
import com.johanapp.ecommerce.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private ProductoRepository productorepository;
	
	
	@Override
	public Producto save(Producto producto) {
		return productorepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productorepository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		 productorepository.save(producto);
		
	}

	@Override
	public void delete(Integer id) {
		productorepository.deleteById(id);
		
	}

}
