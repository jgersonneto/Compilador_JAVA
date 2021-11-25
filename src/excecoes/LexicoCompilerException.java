package excecoes;

import lexico.Token;

public class LexicoCompilerException extends RuntimeException{
	public LexicoCompilerException(String msg, Token token) {		
		super(msg+" -- Erro na linha: " + token.getLine() + " e coluna: " + token.getColumn());
	}

}
