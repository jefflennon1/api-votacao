package br.com.jefferson.exception;

public class SessaoEncerradaException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3119577032478837267L;

	public SessaoEncerradaException(Long pautaId) {
        super("Sessão de votação encerrada ou não iniciada para a pauta com ID: " + pautaId);
    }
}