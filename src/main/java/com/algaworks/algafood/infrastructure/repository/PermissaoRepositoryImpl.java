package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@Repository
public class PermissaoRepositoryImpl implements PermissaoRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Permissao> listar() {
		return this.manager.createQuery("FROM Permissao", Permissao.class).getResultList();
	}

	@Override
	@Transactional
	public Permissao salvar(Permissao permissao) {
		return this.manager.merge(permissao);
	}

	@Override
	public Permissao buscar(Long id) {
		return this.manager.find(Permissao.class, id);
	}

	@Override
	@Transactional
	public void remover(Permissao permissao) {
		permissao = this.buscar(permissao.getId());
		this.manager.remove(permissao);
	}

}
