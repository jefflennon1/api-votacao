package br.com.jefferson.exception;

public class PautaNaoEncontradaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3502342272168181257L;

	public PautaNaoEncontradaException(Long id) {
        super("Pauta não encontrada com ID: " + id);
    }
}