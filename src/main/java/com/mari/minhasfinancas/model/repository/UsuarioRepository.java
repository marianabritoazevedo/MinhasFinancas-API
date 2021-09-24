package com.mari.minhasfinancas.model.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mari.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	//Optional <Usuario> findByEmail(String email); //Optional: pode existir ou não um usuário com esse email. É um querymetod, não precisa usar SQL para buscar
	
	boolean existsByEmail(String email); //Se existir, retorna true, se não, retorna false
	
	Optional <Usuario> findByEmail(String email);
}
