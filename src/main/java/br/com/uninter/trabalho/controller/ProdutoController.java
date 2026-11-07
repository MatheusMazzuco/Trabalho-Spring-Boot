package br.com.uninter.trabalho.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uninter.trabalho.model.Produto;
import br.com.uninter.trabalho.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping({ "/produtos" })
public class ProdutoController {
    private ProdutoRepository produtoRepository;

    @PostMapping
    public Produto create(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    @GetMapping(path = { "/{id}" })
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        Produto record = produtoRepository.findById(id).orElse(null);

        if (record == null) {
            return ResponseEntity.status(404).body("Produto não existe");
        }

        return ResponseEntity.ok().body(record);
    }

    @GetMapping
    public List<?> findAll() {
        return produtoRepository.findAll();
    }

    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean produtoExiste = produtoRepository.existsById(id);

        if (!produtoExiste) {
            return ResponseEntity.status(404).body("Produto não existe");
        }

        produtoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Produto produto) {
        Produto record = produtoRepository.findById(id).orElse(null);

        if (record == null) {
            return ResponseEntity.status(404).body("Produto não existe");
        }

        if (produto.getNome() != null) {
            record.setNome(produto.getNome());
        }

        if (produto.getPreco() != null) {
            record.setPreco(produto.getPreco());
        }

        if (produto.getEstoque() != null) {
            record.setEstoque(produto.getEstoque());
        }

        Produto updated = produtoRepository.save(record);
        return ResponseEntity.ok().body(updated);
    }
}
