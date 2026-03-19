package br.com.jefferson.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jefferson.model.SessaoVotacao;

@Repository 
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

	 Optional<SessaoVotacao> findByPautaId(Long pautaId);
}
