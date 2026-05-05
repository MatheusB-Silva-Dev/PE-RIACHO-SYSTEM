package com.riachoaluminio.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Orcamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O campo cliente é obrigatório.")
    private Cliente cliente;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ItemOrcamento> itens;

    @Column(nullable = false)
    private LocalDate dataCriacao;

    private Integer prazoEntrega;
    private String vendedor;
    private String formaPagamento;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    // Getters e Setters manuais
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemOrcamento> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrcamento> itens) {
        this.itens = itens;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(Integer prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (itens != null) {
            for (ItemOrcamento item : itens) {
                total = total.add(item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            }
        }
        this.valorTotal = total;
    }

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDate.now();
        }
        calcularValorTotal();
    }

    @PreUpdate
    public void preUpdate() {
        calcularValorTotal();
    }
}
