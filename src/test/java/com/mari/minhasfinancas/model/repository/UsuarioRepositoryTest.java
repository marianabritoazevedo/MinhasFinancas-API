package com.mari.minhasfinancas.model.repository;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mari.minhasfinancas.model.entity.Usuario;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) //Não vai sobrescrever configurações
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//3 elementos para o teste: cenário, ação/execução e verificação
		
		//Cenário
		Usuario usuario = criarUsuario();
		//repository.save(usuario);
		entityManager.persist(usuario);
		
		//Ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//Verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//Cenário
		//repository.deleteAll();
		
		//Ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//Verificacao
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//Cenário
		Usuario usuario = criarUsuario();
		//Ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		//Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//Cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Verificacao
		Optional <Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		//Cenario - Não crio nenhum usuário, banco de dados vazio
		
		
		//Verificacao
		Optional <Usuario> result = repository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
	
}
