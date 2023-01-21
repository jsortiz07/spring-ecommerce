package com.johanapp.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.johanapp.ecommerce.model.DetalleOrden;
import com.johanapp.ecommerce.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService {

	@Autowired
	private IDetalleOrdenRepository detalleordenrepository;
	
	@Override
	public DetalleOrden save(DetalleOrden detalleOrden) {
		return detalleordenrepository.save(detalleOrden);
	}

}
