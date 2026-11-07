package br.com.uninter.trabalho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.uninter.trabalho.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
