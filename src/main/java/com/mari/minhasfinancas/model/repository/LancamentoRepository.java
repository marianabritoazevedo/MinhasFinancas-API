package com.mari.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mari.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
