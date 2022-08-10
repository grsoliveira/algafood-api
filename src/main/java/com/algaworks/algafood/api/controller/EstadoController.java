package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.EstadoService;

@RestController
@RequestMapping(value = "/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EntityController<Estado> {

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private EstadoService estadoService;

	@Override
	@GetMapping
	public List<Estado> listar() {
		return this.estadoRepository.findAll();
	}

	@Override
	@GetMapping("/{estadoId}")
	public ResponseEntity<Estado> buscar(Long estadoId) {
		Optional<Estado> estado = this.estadoRepository.findById(estadoId);
		if (estado.isPresent()) {
			return ResponseEntity.ok(estado.get());
		}
		return ResponseEntity.notFound().build();
	}

	@Override
	@PostMapping
	public ResponseEntity<Estado> adicionar(@RequestBody Estado estado) {
		try {
			estado = this.estadoService.salvar(estado);

			return ResponseEntity.ok(estado);

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@Override
	@PutMapping("/{estadoId}")
	public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
		Optional<Estado> estadoAtual = this.estadoRepository.findById(estado.getId());

		try {
			if (estadoAtual.isPresent()) {
				BeanUtils.copyProperties(estado, estadoAtual.get(), "id");
				Estado estadoSalvo = this.estadoService.salvar(estadoAtual.get());

				return ResponseEntity.ok(estadoSalvo);
			}
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.notFound().build();
	}

	@Override
	@DeleteMapping("/{estadoId}")
	public ResponseEntity<Estado> remover(@PathVariable Long estadoId) {
		try {
			this.estadoService.excluir(estadoId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
