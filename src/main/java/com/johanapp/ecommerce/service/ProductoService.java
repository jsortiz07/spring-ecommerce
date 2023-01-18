package com.johanapp.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.johanapp.ecommerce.model.Producto;

public interface ProductoService{

	public Producto save(Producto producto);
	public Optional<Producto> get(Integer id);// optional nos ayuda a validar si el producto existe en la db
	public void update(Producto producto);
	public void delete(Integer id);
	public List<Producto> findAll();
}
