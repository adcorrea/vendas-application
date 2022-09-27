package com.adcorreajr.vendas.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_pedido")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "quantidade")
    private Integer quantidade;


    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", produto=" + produto.getDescricao() +
                ", preco= " + produto.getPreco().toString() +
                ", quantidade=" + quantidade +
                '}';
    }
}
