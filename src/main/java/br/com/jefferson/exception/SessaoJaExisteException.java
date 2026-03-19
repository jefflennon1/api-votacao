package br.com.jefferson.exception;

public class SessaoJaExisteException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1461803661644210845L;

	public SessaoJaExisteException(Long pautaId) {
        super("Já existe uma sessão de votação para a pauta com ID: " + pautaId);
    }
}