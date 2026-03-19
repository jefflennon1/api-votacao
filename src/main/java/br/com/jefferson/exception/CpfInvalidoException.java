package br.com.jefferson.exception;


public class CpfInvalidoException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6591202495782557769L;

	public CpfInvalidoException(String cpf) {
        super("CPF inválido: " + cpf);
    }
}