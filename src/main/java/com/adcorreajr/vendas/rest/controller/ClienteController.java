package com.adcorreajr.vendas.rest.controller;

import com.adcorreajr.vendas.domain.entity.Cliente;
import com.adcorreajr.vendas.domain.repository.Clientes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private Clientes clientes;

    @Autowired
    public ClienteController(Clientes clientes){
        this.clientes = clientes;
    }

    @RequestMapping(
            value = {"/hello/{nome}", "/hello"},
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"}
    )
    @ResponseBody
    public void helloCliente (@PathVariable("nome") String nomeCliente, @RequestBody Cliente cliente){
         String.format("hello " + nomeCliente);
    }


    @GetMapping("/{id}")
    public ResponseEntity getCliente(@PathVariable Integer id){
         Optional<Cliente> cliente = clientes.findById(id);

         if(cliente.isPresent()){
             return ResponseEntity.ok(cliente.get());
         }

         return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity save (@RequestBody @Valid Cliente cliente)
    {
        return ResponseEntity.ok(clientes.save(cliente));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente){
        return clientes.findById(id)
                .map(clienteExistente ->{
                    cliente.setId(clienteExistente.getId());
                    return ResponseEntity.ok(clientes.save(cliente));
                })
                .orElseGet(() -> {return ResponseEntity.notFound().build();});
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable Integer id){
        Optional<Cliente> cliente = clientes.findById(id);

        if(cliente.isPresent()){
            clientes.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity find(Cliente filtro){
        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, exampleMatcher);
        List<Cliente> lista = clientes.findAll(example);
        return ResponseEntity.ok(lista);
    }
}
