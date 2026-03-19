package br.com.jefferson.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jefferson.model.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

	boolean existsByPautaIdAndAssociadoId(Long pautaId, String associadoId);

    List<Voto> findByPautaId(Long pautaId);
}
