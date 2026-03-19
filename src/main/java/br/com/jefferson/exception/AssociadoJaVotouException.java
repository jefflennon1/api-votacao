package br.com.jefferson.exception;

public class AssociadoJaVotouException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2197187831139857859L;

	public AssociadoJaVotouException(String associadoId) {
        super("Associado " + associadoId + " já votou nesta pauta");
    }
}