package main;

import excecoes.LexicoCompilerException;
import excecoes.NullSintaticoCompilerException;
import excecoes.SemanticoCompilerException;
import excecoes.SintaticoCompilerException;
import lexico.ScannerCompiler;
import lexico.Token;
import sintatico.parserCompiler;

public class PrincipalCompiler {

	public static void main(String[] args) {
		
		try {
			ScannerCompiler sc = new ScannerCompiler("C:\\Users\\Neto\\eclipse-workspace\\Compilador\\src\\compilerTxt.net");
			parserCompiler pa = new parserCompiler(sc);
			Token token = null;
			
			/*
			 * 
			 * do {
				token = sc.nextToken();
				if(token != null) {
					System.out.println(token);
				}
			}while(token != null);*/
			
			pa.PROG();
			
			System.out.println();
			System.out.println();
			System.out.println("COMPILADO COM SUCESSO!!!!!!");	
			
			System.out.println();
			pa.exibeComando();
			pa.GerarCodigo();
		}
		catch(LexicoCompilerException ex) {
			System.out.println("ERRO LEXICO "+ex.getMessage());
		}
		catch(SintaticoCompilerException ex) {
			System.out.println("ERRO SINTATICO "+ex.getMessage());
		}
		catch(NullSintaticoCompilerException ex) {
			System.out.println("ERRO SINTATICO "+ex.getMessage());
		}
		catch(SemanticoCompilerException ex) {
			System.out.println("ERRO SEMÂNTICO "+ex.getMessage());
		}
		catch(Exception ex) {
			System.out.println("GENERICO ERROR");
		}
		
	}

}
