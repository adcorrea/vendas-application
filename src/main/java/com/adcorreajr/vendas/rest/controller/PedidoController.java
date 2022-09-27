package com.adcorreajr.vendas.rest.controller;

import com.adcorreajr.vendas.domain.entity.ItemPedido;
import com.adcorreajr.vendas.domain.entity.Pedido;
import com.adcorreajr.vendas.domain.entity.Produto;
import com.adcorreajr.vendas.domain.enums.StatusPedido;
import com.adcorreajr.vendas.rest.dto.AtualizacaoStatusDTO;
import com.adcorreajr.vendas.rest.dto.InformacaoItemPedidoDTO;
import com.adcorreajr.vendas.rest.dto.InformacoesPedidoDTO;
import com.adcorreajr.vendas.rest.dto.PedidoDTO;
import com.adcorreajr.vendas.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    @Autowired
    public PedidoController(PedidoService service){
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer salvar(@RequestBody @Valid PedidoDTO pedidoDTO){

        return service.salvar(pedidoDTO).getId();
    }

    @GetMapping("/{id}")
    public InformacoesPedidoDTO getById(@PathVariable Integer id){
        return service.obterPedidoCompleto(id)
                .map( p -> converter(p))
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não entcontrato"));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusDTO dto){
        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidoDTO converter(Pedido pedido){
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .cpf(pedido.getCliente().getCpf())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItens()))
                .build();

    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> items){
        if(CollectionUtils.isEmpty(items)){
            return Collections.emptyList();
        }

        return items.stream().map(item ->
                InformacaoItemPedidoDTO
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
                ).collect(Collectors.toList());
    }
}
