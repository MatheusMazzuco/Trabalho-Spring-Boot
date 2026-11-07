package br.com.uninter.trabalho.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.uninter.trabalho.model.Pedido;
import br.com.uninter.trabalho.model.Produto;
import br.com.uninter.trabalho.repository.ClienteRepository;
import br.com.uninter.trabalho.repository.PedidoRepository;
import br.com.uninter.trabalho.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping({ "/pedidos" })
public class PedidoController {
    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Pedido pedido) {
        if (!clienteExiste(pedido.getClienteId())) {
            return ResponseEntity.status(404).body("Cliente não existe");
        }

        if (!produtoExiste(pedido.getProdutoId())) {
            return ResponseEntity.status(404).body("Produto não existe");
        }

        if (!produtoTemEstoque(pedido.getProdutoId())) {
            return ResponseEntity.status(409).body("Produto não tem estoque");
        }

        Pedido saved = pedidoRepository.save(pedido);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping(path = { "/{id}" })
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        Pedido record = pedidoRepository.findById(id).orElse(null);

        if (record == null) {
            return ResponseEntity.status(404).body("Pedido não existe");
        }

        return ResponseEntity.ok().body(record);
    }

    @GetMapping
    public List<?> findAll() {
        return pedidoRepository.findAll();
    }

    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean pedidoExiste = pedidoRepository.existsById(id);

        if (!pedidoExiste) {
            return ResponseEntity.status(404).body("Pedido não existe");
        }

        pedidoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Pedido pedido) {
        Pedido record = pedidoRepository.findById(id).orElse(null);

        if (record == null) {
            return ResponseEntity.status(404).body("Pedido não existe");
        }

        if (pedido.getClienteId() != null) {
            if (!clienteExiste(pedido.getClienteId())) {
                return ResponseEntity.status(404).body("Cliente não existe");
            }
            record.setClienteId(pedido.getClienteId());
        }

        if (pedido.getProdutoId() != null) {
            if (!produtoExiste(pedido.getProdutoId())) {
                return ResponseEntity.status(404).body("Produto não existe");
            }

            if (!produtoTemEstoque(pedido.getProdutoId())) {
                return ResponseEntity.status(409).body("Produto não tem estoque");
            }
            record.setProdutoId(pedido.getProdutoId());
        }

        if (pedido.getQuantidade() != null) {
            record.setQuantidade(pedido.getQuantidade());
        }

        Pedido updated = pedidoRepository.save(record);
        return ResponseEntity.ok().body(updated);
    }

    private boolean clienteExiste(Long clienteId) {
        return clienteRepository.existsById(clienteId);
    }

    private boolean produtoExiste(Long produtoId) {
        return produtoRepository.existsById(produtoId);
    }

    private boolean produtoTemEstoque(Long produtoId) {
        Optional<Produto> produto = produtoRepository.findById(produtoId);

        if (!produto.isPresent()) {
            return false;
        }

        if (!produto.get().getEstoque()) {
            return false;
        }

        return true;
    }
}
