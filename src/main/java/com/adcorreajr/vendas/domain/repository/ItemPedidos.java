package com.adcorreajr.vendas.domain.repository;

import com.adcorreajr.vendas.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidos extends JpaRepository<ItemPedido, Integer> {
}
