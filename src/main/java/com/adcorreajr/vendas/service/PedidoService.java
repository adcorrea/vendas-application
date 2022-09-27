package com.adcorreajr.vendas.service;

import com.adcorreajr.vendas.domain.entity.Pedido;
import com.adcorreajr.vendas.domain.enums.StatusPedido;
import com.adcorreajr.vendas.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO pedidoDTO);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
