package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private RestauranteService restauranteService;

	@GetMapping
	public List<Restaurante> listar() {
		return this.restauranteRepository.listar();
	}

	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = this.restauranteRepository.buscar(restauranteId);

		if (restaurante != null) {
			return ResponseEntity.ok(restaurante);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Restaurante> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = this.restauranteService.salvar(restaurante);

			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}

	}

	@PutMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> atualizar(@PathVariable Long restauranteId,
			@RequestBody Restaurante restaurante) {
		Restaurante restauranteAtual = this.restauranteRepository.buscar(restauranteId);

		try {
			if (restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
				restauranteAtual = this.restauranteService.salvar(restauranteAtual);

				return ResponseEntity.ok(restauranteAtual);

			}
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.notFound().build();
	}

}
