package com.adcorreajr.vendas.domain.repository;

import com.adcorreajr.vendas.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Produtos extends JpaRepository<Produto, Integer> {
}
