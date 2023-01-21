package com.johanapp.ecommerce.service;

import java.util.List;

import com.johanapp.ecommerce.model.Orden;

public interface IOrdenService {
	
	List<Orden> findAll();
	Orden save(Orden orden);
}
