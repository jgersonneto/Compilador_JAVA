package excecoes;

import lexico.Token;

public class SintaticoCompilerException extends RuntimeException{
	
	public SintaticoCompilerException(String msg, Token token) {		
		super(msg+" -- Erro na linha: " + token.getLine() + " e coluna: " + token.getColumn());
	}

}
