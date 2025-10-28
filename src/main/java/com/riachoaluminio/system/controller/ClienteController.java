package com.riachoaluminio.system.controller;

import com.riachoaluminio.system.entity.Cliente;
import com.riachoaluminio.system.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService; // Bean injetado: ClienteService, usado para gerenciar a lógica de negócios dos clientes

    // Construtor para injeção de dependencia via construtor
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService; // Atribuindo a dependência injetada ao atributo da classe
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        // Define o id recebido via URL no objeto para garantir que é uma atualização.
        cliente.setId(id);
        return clienteService.atualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
