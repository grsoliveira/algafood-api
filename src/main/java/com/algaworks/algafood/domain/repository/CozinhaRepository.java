package com.algaworks.algafood.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

<<<<<<< HEAD
=======
//	List<Cozinha> nome(String nome); //Busca por nome exato
	
>>>>>>> branch 'main' of https://github.com/grsoliveira/algafood-api.git
	List<Cozinha> findByNome(String nome);

}
