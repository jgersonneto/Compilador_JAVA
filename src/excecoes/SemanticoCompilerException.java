package excecoes;

import lexico.Token;
import semantico.Variavel;

public class SemanticoCompilerException extends RuntimeException{
	
	public SemanticoCompilerException(String msg, Variavel var) {		
		super(msg+" -- Erro na linha: " + var.getLine() + " e coluna: " + var.getColumn());
	}
	
}
