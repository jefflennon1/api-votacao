package br.com.jefferson.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaRequest {

	@NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres")
    private String titulo;
	
	@Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;
}