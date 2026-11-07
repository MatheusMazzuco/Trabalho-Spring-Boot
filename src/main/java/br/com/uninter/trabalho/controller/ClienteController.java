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

import br.com.uninter.trabalho.model.Cliente;
import br.com.uninter.trabalho.repository.ClienteRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping({ "/clientes" })
public class ClienteController {
    private ClienteRepository clienteRepository;

    @PostMapping
    public Cliente create(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @GetMapping(path = { "/{id}" })
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        Cliente record = clienteRepository.findById(id).orElse(null);

        if (record == null) {
            return ResponseEntity.status(404).body("Cliente não existe");
        }

        return ResponseEntity.ok().body(record);
    }

    @GetMapping
    public List<?> findAll() {
        return clienteRepository.findAll();
    }

    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean clienteExiste = clienteRepository.existsById(id);

        if (!clienteExiste) {
            return ResponseEntity.status(404).body("Cliente não existe");
        }

        clienteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Cliente cliente) {
        Cliente record = clienteRepository.findById(id).orElse(null);

        if (record == null) {
            return ResponseEntity.status(404).body("Cliente não existe");
        }

        if (cliente.getNome() != null) {
            record.setNome(cliente.getNome());
        }

        Cliente updated = clienteRepository.save(record);
        return ResponseEntity.ok().body(updated);
    }
}
