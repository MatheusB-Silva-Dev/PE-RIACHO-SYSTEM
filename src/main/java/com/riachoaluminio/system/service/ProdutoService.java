package com.riachoaluminio.system.service;

import com.riachoaluminio.system.entity.Produto;
import com.riachoaluminio.system.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id).map(existingProduto -> {
            existingProduto.setModelo(produtoAtualizado.getModelo());
            existingProduto.setMaterial(produtoAtualizado.getMaterial());
            existingProduto.setImagemUrl(produtoAtualizado.getImagemUrl());
            return produtoRepository.save(existingProduto);
        }).orElseThrow(() -> new RuntimeException("Produto não encontrado para o id: " + id));
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado para o id: " + id));
    }

}
