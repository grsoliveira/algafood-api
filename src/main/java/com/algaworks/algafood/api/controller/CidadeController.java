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
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CidadeService;

@RestController
@RequestMapping(value = "{/cidades}", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements EntityController<Cidade> {

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Override
	@GetMapping
	public List<Cidade> listar() {
		return this.cidadeRepository.findAll();
	}

	@Override
	@GetMapping("/{cidadeId}")
	public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
		Optional<Cidade> cidade = this.cidadeRepository.findById(cidadeId);
		if (cidade.isPresent()) {
			return ResponseEntity.ok(cidade.get());
		}
		return ResponseEntity.notFound().build();
	}

	@Override
	@PostMapping
	public ResponseEntity<Cidade> adicionar(@RequestBody Cidade cidade) {
		cidade = this.cidadeService.salvar(cidade);
		return ResponseEntity.ok(cidade);
	}

	@Override
	@PutMapping("/{cidadeId}")
	public ResponseEntity<Cidade> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
		Optional<Cidade> cidadeAtual = this.cidadeRepository.findById(cidadeId);
		try {
			if (cidadeAtual.isPresent()) {
				BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");
				Cidade cidadeSalva = this.cidadeService.salvar(cidadeAtual.get());

				return ResponseEntity.ok(cidadeSalva);
			}
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.notFound().build();
	}

	@Override
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<Cidade> remover(@PathVariable Long cidadeId) {
		try {
			this.cidadeService.excluir(cidadeId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
