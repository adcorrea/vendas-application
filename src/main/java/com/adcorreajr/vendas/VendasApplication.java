package com.adcorreajr.vendas;

import com.adcorreajr.vendas.domain.entity.Cliente;
import com.adcorreajr.vendas.domain.entity.ItemPedido;
import com.adcorreajr.vendas.domain.entity.Pedido;
import com.adcorreajr.vendas.domain.entity.Produto;
import com.adcorreajr.vendas.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class VendasApplication {


	public static void main(String[] args) {
		SpringApplication.run(VendasApplication.class, args);
	}

}
