package com.johanapp.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johanapp.ecommerce.model.Usuario;

@Repository
public interface IUsuarioRepository extends  JpaRepository<Usuario, Integer>{

}
