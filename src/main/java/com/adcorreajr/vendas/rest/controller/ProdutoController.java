package com.adcorreajr.vendas.rest.controller;

import com.adcorreajr.vendas.domain.entity.Cliente;
import com.adcorreajr.vendas.domain.entity.Produto;
import com.adcorreajr.vendas.domain.repository.Produtos;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private Produtos produtosRepository;

    @Autowired
    public ProdutoController(Produtos produtosRepository){
        this.produtosRepository = produtosRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto salvar(@RequestBody @Valid Produto produto){

        return produtosRepository.save(produto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Produto getProduto(@PathVariable Integer id){
        return produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody @Valid Produto produto){
        produtosRepository.findById(id)
                .map(produtoEncontrado -> {
                    produto.setId(produtoEncontrado.getId());
                    produtosRepository.save(produto);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        produtosRepository.findById(id)
                .map(produtoEncontrado -> {
                    produtosRepository.delete(produtoEncontrado);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Produto> pesquisarProduto(Produto filtro){
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, exampleMatcher);
        return  produtosRepository.findAll(example);
    }


}
