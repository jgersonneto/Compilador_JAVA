package excecoes;

import lexico.ScannerCompiler;
import lexico.Token;

public class NullSintaticoCompilerException extends RuntimeException{
	public NullSintaticoCompilerException(String msg, ScannerCompiler scan) {		
		super(msg+" -- Erro na linha: " + scan.getLine() + " e coluna: " + scan.getColumn());
	}

}
