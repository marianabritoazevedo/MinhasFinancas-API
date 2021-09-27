package com.mari.minhasfinancas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "usuario", schema = "financas")
@Builder
@Data //Equivale a getter, setter, tostring, equals e hashcode
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
	//Criação das colunas
	
	@Id
	@Column(name = "id") //Não é necessário, pois o nome é o mesmo na base de dados
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome;

	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	@JsonIgnore
	private String senha;

}
