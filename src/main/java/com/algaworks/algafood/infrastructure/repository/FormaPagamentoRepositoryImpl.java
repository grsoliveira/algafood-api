package com.algaworks.algafood.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Repository
public class FormaPagamentoRepositoryImpl implements FormaPagamentoRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<FormaPagamento> listar() {
		return this.manager.createQuery("FROM FormaPagamento", FormaPagamento.class).getResultList();
	}

	@Override
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return this.manager.merge(formaPagamento);
	}

	@Override
	public FormaPagamento buscar(Long id) {
		return this.manager.find(FormaPagamento.class, id);
	}

	@Override
	@Transactional
	public void remover(FormaPagamento formaPagamento) {
		formaPagamento = this.buscar(formaPagamento.getId());
		this.manager.remove(formaPagamento);
	}

}
