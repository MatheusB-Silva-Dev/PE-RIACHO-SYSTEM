package com.riachoaluminio.system.service;

import com.riachoaluminio.system.entity.Cliente;
import com.riachoaluminio.system.entity.ItemOrcamento;
import com.riachoaluminio.system.entity.Orcamento;
import com.riachoaluminio.system.entity.Produto;
import com.riachoaluminio.system.exception.ResourceNotFoundException;
import com.riachoaluminio.system.repository.ClienteRepository;
import com.riachoaluminio.system.repository.OrcamentoRepository;
import com.riachoaluminio.system.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrcamentoService {
    private final OrcamentoRepository orcamentoRepository; // Bean injetado: OrcamentoRepository, responsável pelo acesso aos dados de orçamentos
    private final ClienteRepository clienteRepository; // Bean injetado: ClienteRepository, responsável pelo acesso aos dados de clientes
    private final ProdutoRepository produtoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository, ClienteRepository clienteRepository, ProdutoRepository produtoRepository) {
        this.orcamentoRepository = orcamentoRepository; // Atribuindo OrcamentoRepository injetado ao atributo da classe
        this.clienteRepository = clienteRepository; // Atribuindo ClienteRepository injetado ao atributo da classe
        this.produtoRepository = produtoRepository;
    }

    public List<Orcamento> listarTodos() {
        return orcamentoRepository.findAll();
    }

    public Orcamento salvar(Orcamento orcamento) {
        if (orcamento.getCliente() == null) {
            throw new IllegalArgumentException("O campo cliente é obrigatório.");
        }

        if (orcamento.getCliente().getId() != null) {
            Cliente clienteCompleto = clienteRepository.findById(orcamento.getCliente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + orcamento.getCliente().getId()));
            orcamento.setCliente(clienteCompleto);
        } else {
            Cliente clienteSalvo = clienteRepository.save(orcamento.getCliente());
            orcamento.setCliente(clienteSalvo);
        }

        return orcamentoRepository.save(orcamento);
    }

    public Orcamento atualizar(Long id, Orcamento orcamentoAtualizado) {
        return orcamentoRepository.findById(id).map(existingOrcamento -> {
            if (orcamentoAtualizado.getCliente() != null && orcamentoAtualizado.getCliente().getId() != null) {
                Cliente clienteCompleto = clienteRepository.findById(orcamentoAtualizado.getCliente().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o id: " + orcamentoAtualizado.getCliente().getId()));
                existingOrcamento.setCliente(clienteCompleto);
            }
            if (orcamentoAtualizado.getDataCriacao() != null) {
                existingOrcamento.setDataCriacao(orcamentoAtualizado.getDataCriacao());
            }
            existingOrcamento.setPrazoEntrega(orcamentoAtualizado.getPrazoEntrega());
            existingOrcamento.setVendedor(orcamentoAtualizado.getVendedor());
            existingOrcamento.setFormaPagamento(orcamentoAtualizado.getFormaPagamento());

            if (orcamentoAtualizado.getItens() != null) {
                List<ItemOrcamento> itensResolvidos = orcamentoAtualizado.getItens().stream()
                        .map(item -> {
                            Produto produto = produtoRepository.findById(item.getProduto().getId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + item.getProduto().getId()));
                            item.setProduto(produto);
                            return item;
                        }).collect(Collectors.toList());
                existingOrcamento.setItens(itensResolvidos);
            }

            return orcamentoRepository.save(existingOrcamento);
        }).orElseThrow(() -> new ResourceNotFoundException("Orçamento não encontrado para o id: " + id));
    }

    public void excluir(Long id) {
        if (!orcamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Orçamento não encontrado para o id: " + id);
        }
        orcamentoRepository.deleteById(id);
    }

    public Orcamento findById(Long id) {
        return orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orçamento não encontrado para o id: " + id));
    }
}
