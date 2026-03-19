package br.com.jefferson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.jefferson.model.Pauta;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

}
