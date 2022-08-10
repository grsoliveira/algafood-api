package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Restaurante> listar() {
		return this.manager.createQuery("FROM Restaurante", Restaurante.class).getResultList();
	}

	@Override
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		return this.manager.merge(restaurante);
	}

	@Override
	public Restaurante buscar(Long id) {
		return this.manager.find(Restaurante.class, id);
	}

	@Override
	@Transactional
	public void remover(Long restauranteId) {
		this.manager.remove(restauranteId);
	}

}
