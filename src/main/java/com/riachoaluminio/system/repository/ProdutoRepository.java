package com.riachoaluminio.system.repository;

import com.riachoaluminio.system.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository <Produto, Long> {

}
