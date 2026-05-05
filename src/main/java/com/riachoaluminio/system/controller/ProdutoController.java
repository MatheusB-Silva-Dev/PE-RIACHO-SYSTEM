package com.riachoaluminio.system.controller;

import com.riachoaluminio.system.entity.Produto;
import com.riachoaluminio.system.service.ProdutoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;
    @Value("${app.upload.dir}")
    private String uploadDir;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;

    }

    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        return produtoService.atualizar(id, produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @PostMapping("/{id}/imagem")
    public ResponseEntity<Produto> uploadImagem(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String nomeArquivo = "produto_" + id + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(nomeArquivo);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imagemUrl = "C:/riacho-uploads/produtos/" + nomeArquivo;
        Produto produto = produtoService.buscarPorId(id);
        produto.setImagemUrl(imagemUrl);
        Produto atualizado = produtoService.atualizar(id, produto);

        return ResponseEntity.ok(atualizado);
    }
}
