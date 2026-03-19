package br.com.jefferson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoRequest {

	@NotBlank(message = "O ID do associado é obrigatório")
	private String associadoId;

	@NotNull(message = "O voto é obrigatório")
	private Boolean voto; // true = Sim, false = Não
}