package com.mari.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mari.minhasfinancas.exception.RegraNegocioException;
import com.mari.minhasfinancas.exception.ErroAutenticacao;
import com.mari.minhasfinancas.model.entity.Usuario;
import com.mari.minhasfinancas.model.repository.UsuarioRepository;
import com.mari.minhasfinancas.service.impl.UsuarioServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTeste {
	
	//@Autowired
	@SpyBean
	UsuarioServiceImpl service;
	
	//@Autowired
	@MockBean
	UsuarioRepository repository;
	
	//@BeforeEach
	//public void setUp() {
		//service = Mockito.spy(UsuarioServiceImpl.class);
		//repository = Mockito.mock(UsuarioRepository.class); -> Comentado primeiro
		//service = new UsuarioServiceImpl(repository); -> Comentado depois
	//}
	
	@Test
	public void deveSalvarUmUsuario() {
		//Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@email.com").senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//Ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//Cenario
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//Ação
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));
		
		
		//Verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//Cenario 
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Ação
		Usuario result = service.autenticar(email, senha);
		
		//Verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){
		//Cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//Ação
		//org.junit.jupiter.api.Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "senha"));
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//Cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//Ação
		//org.junit.jupiter.api.Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "123"));
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
		
	}
	
	@Test
	public void deveValidarEmail() {
		//Cenário
		//repository.deleteAll(); -> Não é ideal ficar usando repository para fazer testes em Service
		//Usar mock para criar cenário fake
		//UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//Ação
		service.validarEmail("email@email.com");
	}
	
	@Test //OBS.: Espera-se que seja lançada a exceção
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//Cenário
		//Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		//repository.save(usuario);
		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		//Acao
		org.junit.jupiter.api.Assertions.assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
	}
}
