package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Repository
public class CozinhaRepositoryImpl implements CozinhaRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Cozinha> listar() {
		return this.manager.createQuery("FROM Cozinha", Cozinha.class).getResultList();
	}

	@Override
	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return this.manager.merge(cozinha);
	}

	@Override
	public Cozinha buscar(Long id) {
		return this.manager.find(Cozinha.class, id);
	}

	@Override
	@Transactional
	public void remover(Long id) {
		Cozinha cozinha = this.buscar(id);

		if (cozinha == null) {
			throw new EmptyResultDataAccessException(1);
		}

		this.manager.remove(cozinha);
	}

	@Override
	public List<Cozinha> consultarPorNome(String nome) {
		return this.manager.createQuery("FROM Cozinha WHERE nome = :nome", Cozinha.class)
				.setParameter("nome", "%" + nome + "%").getResultList();

	}

}
