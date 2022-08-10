package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Component
public class CidadeRepositoryImpl implements CidadeRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Cidade> listar() {
		return this.manager.createQuery("FROM Cidade", Cidade.class).getResultList();
	}

	@Override
	@Transactional
	public Cidade salvar(Cidade cidade) {
		return this.manager.merge(cidade);
	}

	@Override
	public Cidade buscar(Long id) {
		return this.manager.find(Cidade.class, id);
	}

	@Override
	@Transactional
	public void remover(Long cidadeId) {
		Cidade cidade = this.buscar(cidadeId);
		this.manager.remove(cidade);
	}

}
