package com.adcorreajr.vendas.service.impl;

import com.adcorreajr.vendas.domain.entity.Cliente;
import com.adcorreajr.vendas.domain.entity.ItemPedido;
import com.adcorreajr.vendas.domain.entity.Pedido;
import com.adcorreajr.vendas.domain.entity.Produto;
import com.adcorreajr.vendas.domain.enums.StatusPedido;
import com.adcorreajr.vendas.domain.repository.Clientes;
import com.adcorreajr.vendas.domain.repository.ItemPedidos;
import com.adcorreajr.vendas.domain.repository.Pedidos;
import com.adcorreajr.vendas.domain.repository.Produtos;
import com.adcorreajr.vendas.exception.PedidoNaoEncontradoException;
import com.adcorreajr.vendas.exception.RegraNegocioException;
import com.adcorreajr.vendas.rest.dto.ItemPedidoDTO;
import com.adcorreajr.vendas.rest.dto.PedidoDTO;
import com.adcorreajr.vendas.service.PedidoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemPedidos itemRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatus(StatusPedido.REALIZADO);

        Cliente cliente = clientesRepository.findById(pedidoDTO.getCliente())
                .orElseThrow(()->new RegraNegocioException("Codigo de Cliente inválido!"));

        pedido.setCliente(cliente);

        List<ItemPedido> listaItemPedido = converterItens(pedido, pedidoDTO.getItems());
        pedido = repository.save(pedido);
        listaItemPedido = itemRepository.saveAll(listaItemPedido);
        pedido.setItens(listaItemPedido);

        return  pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository.findById(id)
                .map( p -> {
                    p.setStatus(statusPedido);
                    return repository.save(p);
                })
                .orElseThrow(() -> new PedidoNaoEncontradoException());
    }


    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> items){

        if(items.isEmpty()){
            throw new RegraNegocioException("Lista de items está vazia!");
        }

        return items
                .stream()
                .map( dto -> {
                    Produto produto = produtosRepository.findById(dto.getProduto())
                            .orElseThrow(() -> new RegraNegocioException("Codigo de Produto invalido!"));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setPedido(pedido);
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setProduto(produto);

                    return itemPedido;
                }).collect(Collectors.toList());

    }
}
