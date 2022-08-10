package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface EntityController<T> {

	public List<T> listar();

	public ResponseEntity<T> buscar(Long entityId);

	public ResponseEntity<T> adicionar(T entity);

	public ResponseEntity<T> atualizar(Long entityId, T entity);

	public ResponseEntity<T> remover(Long entityId);

}
