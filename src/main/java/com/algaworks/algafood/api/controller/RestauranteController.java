package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.RestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements EntityController<Restaurante> {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private RestauranteService restauranteService;

	@GetMapping
	public List<Restaurante> listar() {
		return this.restauranteRepository.findAll();
	}

	@Override
	@GetMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> buscar(@PathVariable Long restauranteId) {
		Optional<Restaurante> restaurante = this.restauranteRepository.findById(restauranteId);

		if (restaurante.isPresent()) {
			return ResponseEntity.ok(restaurante.get());
		}

		return ResponseEntity.notFound().build();
	}

	@Override
	@PostMapping
	public ResponseEntity<Restaurante> adicionar(@RequestBody Restaurante restaurante) {
		try {
			restaurante = this.restauranteService.salvar(restaurante);

			return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}

	}

	@Override
	@PutMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> atualizar(@PathVariable Long restauranteId,
			@RequestBody Restaurante restaurante) {
		Optional<Restaurante> restauranteAtual = this.restauranteRepository.findById(restauranteId);

		try {
			if (restauranteAtual != null) {
				BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id");
				Restaurante restauranteSalvo = this.restauranteService.salvar(restauranteAtual.get());

				return ResponseEntity.ok(restauranteSalvo);

			}
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.notFound().build();
	}

	@PatchMapping("/{restauranteId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long restauranteId,
			@RequestBody Map<String, Object> campos) {

		Optional<Restaurante> restauranteAtual = this.restauranteRepository.findById(restauranteId);
		if (restauranteAtual.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		this.mesclar(campos, restauranteAtual.get());

		return this.atualizar(restauranteId, restauranteAtual.get());
	}

	private void mesclar(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);

			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}

	@Override
	@DeleteMapping("/{restauranteId}")
	public ResponseEntity<Restaurante> remover(@PathVariable Long restauranteId) {
		try {
			this.restauranteService.excluir(restauranteId);
			return ResponseEntity.noContent().build();

		} catch (EntidadeEmUsoException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();

		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
