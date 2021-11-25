package sintatico;

import java.lang.invoke.StringConcatFactory;
import java.util.ArrayList;

import arvoreGeraCodigo.AbstractComando;
import arvoreGeraCodigo.ComandoATR;
import arvoreGeraCodigo.ComandoConvertFloat;
import arvoreGeraCodigo.ComandoDV;
import arvoreGeraCodigo.ComandoEA;
import arvoreGeraCodigo.ComandoIF;
import arvoreGeraCodigo.ComandoJUMPER;
import arvoreGeraCodigo.ComandoOPR;
import arvoreGeraCodigo.ComandoWHILE;
import arvoreGeraCodigo.Label;
import arvoreGeraCodigo.ProgGeradorCod;
import excecoes.NullSintaticoCompilerException;
import excecoes.SemanticoCompilerException;
import excecoes.SintaticoCompilerException;
import lexico.ScannerCompiler;
import lexico.Token;
import semantico.Simbolo;
import semantico.TabelaSimbolo;
import semantico.Variavel;

public class parserCompiler {
	
	private ScannerCompiler scan;
	private Token token;
	private int OPR;
	private int chave;
	private int atrib;
	
	//Manipulação das variaveis
	private String tipoVar;
	private String nomeVar;
	private String valorVar;
	private TabelaSimbolo tabelaSimbolo = new TabelaSimbolo();
	private Simbolo simbolo;
	private int bloco;
	
	//Manipulação para Gerar Código Intermediario
	private ProgGeradorCod programa = new ProgGeradorCod();
	private ArrayList<AbstractComando> curThread = new ArrayList<AbstractComando>();	
	private String idEA;
	private String id1;
	private int cont1;
	private int cont2;
		
	public parserCompiler (ScannerCompiler scan) {
		this.scan = scan;		
	}
	
	/*
	 * -----------------------------------------------------------
	 * PROGRAMA
	 * Tudo que se refere a PROGRAMA esta nesse bloco
	 * -----------------------------------------------------------
	 */
	
	public void PROG() {
		
		PRINT();
		PRMAIN();
		token = scan.nextToken();
		aParentese();
		token = scan.nextToken();
		fParentese();
		token = scan.nextToken();
		BL();
		
		if(!scan.isFim()) {
			token = scan.nextToken();
			throw new SintaticoCompilerException(" - Existe um ABRE CHAVE faltando OU tem um FECHA CHAVE a mais", token);
		}
		programa.setComandos(curThread);
	}	
		
	public void PRINT() {
		token = scan.nextToken();
		if (token.getType() != Token.TK_RESERVADO 
			|| !token.getText().equals("int")) {
			
			throw new SintaticoCompilerException(" - TIPO NÃO COMPATÍVEL", token);
				
		}		
	}
	
	public void PRMAIN() {
		token = scan.nextToken();
		if (token.getType() != Token.TK_RESERVADO 
			|| !token.getText().equals("main")) {
			
			throw new SintaticoCompilerException(" - Palavra Reservada NÃO COMPATÍVEL", token);
				
		}		
	}
	
	public void aParentese() {
		if (token != null) {
			if (token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals("(")) {				
				
				throw new SintaticoCompilerException(" - Falta do Caracter Especial ABRE PARENTESE", token);
				
			}	
		}
		else {
			throw new NullSintaticoCompilerException(" - Falta do Caracter Especial ABRE PARENTESE", scan);
		}
			
	}
	
	public void fParentese() {
		if (token != null) {
			if (token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals(")")) {
				throw new SintaticoCompilerException(" - SINAL FECHA PARENTESE", token);
			}			
		}
		else {
			throw new NullSintaticoCompilerException(" - SINAL FECHA PARENTESE", scan);
		}		
	}
	
	public void BL() {
		this.bloco++;	
		aChave();
		token = scan.nextToken();
		
		while(token.getType() == Token.TK_RESERVADO && (token.getText().equals("int") || token.getText().equals("float") || token.getText().equals("char"))) {
			DV();
			token = scan.nextToken();
		}
		
		while(token.getText().equals("if") || token.getText().equals("{") || token.getText().equals("while") || token.getType() == Token.TK_IDENTIFICADOR) {
			COMD();			
		}
		fChave();
		this.bloco--;
	}
	
	private void aChave() {		
		if (token != null) {
			if (token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals("{")) {				
				
				throw new SintaticoCompilerException(" - Falta do Caracter Especial ABRE CHAVE", token);
				
			}
			chave++;
		}
		else {
			throw new NullSintaticoCompilerException(" - Falta do Caracter Especial ABRE CHAVE", scan);
		}
	}
	
	private void fChave() {
		if (token != null) {
			if (token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals("}")) {				
				
				throw new SintaticoCompilerException(" - Falta do Caracter Especial FECHA CHAVE", token);
				
			}
			if(chave > 0) {
				chave--;
			}
			else {
				throw new SintaticoCompilerException(" - Tentando FECHA CHAVE sem a presença de um ABRE CHAVE", token);
			}
			
		}
		else {
			throw new NullSintaticoCompilerException(" - Falta do Caracter Especial FECHA CHAVE", scan);
		}
	}
	
	private void COMD() {
		String op;
		op = token.getText();
		switch(op) {
		case "if":
			IF();			
			break;
		case "while":
			IT();			
			break;	
		case "{":
			BL();
			token = scan.nextToken();
			break;
		default:
			AT();
			token = scan.nextToken();
			break;
		}
	}	
	
	private void IF() {
		String label1 = "L"+(cont2++);
		Label lab = new Label(label1);
		
		token = scan.nextToken();
		aParentese();
		ER();
		fParentese();
		ComandoIF IF = new ComandoIF(this.idEA, label1);
		curThread.add(IF);
		this.idEA = null;
		token = scan.nextToken();
		COMD();
		curThread.add(lab);
		if(token.getText().equals("else")) {			
			token = scan.nextToken();
			COMD();
		}
		
		
	}
	
	private void IT() {
		String label1 = "L"+(cont2++);
		Label lab1 = new Label(label1);
		String label2 = "L"+(cont2++);
		Label lab2 = new Label(label2);
		curThread.add(lab1);
		token = scan.nextToken();
		aParentese();
		ER();
		fParentese();
		ComandoWHILE WHILE = new ComandoWHILE(this.idEA, label2);
		curThread.add(WHILE);
		this.idEA = null;
		token = scan.nextToken();
		COMD();
		ComandoJUMPER J = new ComandoJUMPER(label1);
		curThread.add(J);
		curThread.add(lab2);
		this.idEA = null;
	}

	/*
	 * -----------------------------------------------------------
	 * FIM do bloco do PROGRAMA
	 * -----------------------------------------------------------
	 */
	
	/*
	 * -----------------------------------------------------------
	 * Declaração de Variável
	 * Tudo que se refere a Declaração de Variável esta nesse bloco
	 * -----------------------------------------------------------
	 */
	
	public void DV() {
		TIPDV();
		this.tipoVar = token.getText();
		
		token = scan.nextToken();
		ID();
		this.nomeVar = token.getText();
		this.valorVar = null;
		simbolo = new Variavel(this.nomeVar, this.tipoVar, 
				this.valorVar, this.bloco, token.getLine(), token.getColumn());	
		if(!tabelaSimbolo.existe(nomeVar) || (tabelaSimbolo.existe(nomeVar) && 
				((Variavel)tabelaSimbolo.getSimbolo(nomeVar)).getBloco()!= this.bloco)) {
			tabelaSimbolo.add(simbolo);
		}
		else {
			throw new SemanticoCompilerException(" - Não pode declarar uma variável que já está declarada", (Variavel) simbolo);
		}
		ComandoDV cmd1 = new ComandoDV(this.tipoVar, this.nomeVar);
		curThread.add(cmd1);
		
		token = scan.nextToken();
		CEPV();
		
	}
		
	public void TIPDV() {
		
		if (token.getType() != Token.TK_RESERVADO 
			|| (!token.getText().equals("int") 
			&& !token.getText().equals("float") 
			&& !token.getText().equals("char"))) {			
							
			throw new SintaticoCompilerException(" - TIPO VARIÀVEL INCORRETA", token);
				
		}		
	}
	
	public void CEPVDV() {
		
		if (token == null || token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals(";")) {				
				
				throw new NullSintaticoCompilerException(" - FALTA DO CARACTER ESPECIAL PONTO VÍRGULA", scan);
				
			}		
	}
	
	
	/*
	 * -----------------------------------------------------------
	 * FIM do bloco da Declaração de Variável 
	 * -----------------------------------------------------------
	 */
	
	/*
	 * -----------------------------------------------------------
	 * Atribuição
	 * Tudo que se refere a Atribuição esta nesse bloco
	 * -----------------------------------------------------------
	 */
	
	public void AT() {
		String EA1;
		this.atrib++;
		ID();
		this.nomeVar = token.getText();		
		simbolo = new Variavel(this.nomeVar, this.tipoVar, 
				this.valorVar, this.bloco, token.getLine(), token.getColumn());
		if(!tabelaSimbolo.existe(this.nomeVar)) {
			throw new SemanticoCompilerException(" - A variável não foi declarada", (Variavel) simbolo);
		}
		simbolo = tabelaSimbolo.getSimbolo(this.nomeVar);
		tipoVar = ((Variavel)simbolo).getType();
		((Variavel)simbolo).setColumn(token.getColumn());
		((Variavel)simbolo).setLine(token.getLine());
		OPAT();
		EA();
		if(this.idEA == null) {
			EA1 = this.valorVar;			
		}
		else {
			EA1 = this.idEA;
			this.idEA = null;
		}
		CEPV();
		ComandoATR cmd1 = new ComandoATR(this.nomeVar, EA1);
		curThread.add(cmd1);
		this.atrib--;
	}	
		
	public void OPAT() {
		token = scan.nextToken();
		if (token.getType() != Token.TK_OP_ARITMETRICO || !token.getText().equals("=")) {			
							
			throw new SintaticoCompilerException(" - OPERADOR ARITMETICO ATRIBUIÇÂO", token);
				
		}		
	}
	public void ID() {		
		if (token.getType() != Token.TK_IDENTIFICADOR) {
						
			throw new SintaticoCompilerException(" - IDENTIFICADOR", token);
			
		}		
	}
	
	public void CEPV() {
		if (token == null || token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals(";")) {				
				
				throw new NullSintaticoCompilerException(" - FALTA DO CARACTER ESPECIAL PONTO VÍRGULA", scan);
				
			}		
	}
	
	/*
	 * -----------------------------------------------------------
	 * FIM do bloco da Atribuição 
	 * -----------------------------------------------------------
	 */
	
	/*
	 * -----------------------------------------------------------
	 * Expressão Relacional
	 * Tudo que se refere a expressão Relacional esta nesse bloco
	 * -----------------------------------------------------------
	 */
	
	public void ER() {
		String EA1;
		String EA2;
		String OPR;
		EA();
		if(this.idEA == null) {
			EA1 = this.valorVar;
		}
		else {
			EA1 = this.idEA;
			this.idEA = null;			
		}		
		OPR();
		OPR = token.getText();
		EA();
		if(this.idEA == null) {
			EA2 = this.valorVar;
		}
		else {
			EA2 = this.idEA;
			this.idEA = null;			
		}
		this.idEA = "t"+ (this.cont1++);
		ComandoOPR op = new ComandoOPR(this.idEA, EA1, OPR, EA2);
		curThread.add(op);		
		this.OPR--;
	}
	
	public void OPR() {
		if(token != null) {
			if (token.getType() != Token.TK_OP_RELACIONAL) {
			
				throw new SintaticoCompilerException(" - OPERADOR RELACIONAL", token);
				
			}
			this.OPR++;
		}
		else {
			throw new NullSintaticoCompilerException(" - RETORNA NULL - Fim de TEXTO. Não foi encontrado nenhum OPERADOR RELACIONAL ", scan);
		}
			
	}
	
	/*
	 * -----------------------------------------------------------
	 * FIM do bloco da Expressão Relacional 
	 * -----------------------------------------------------------
	 */
	
	/*
	 * -----------------------------------------------------------
	 * Expressão Aritmética
	 * Tudo que se refere a expressão Aritmética esta nesse bloco
	 * -----------------------------------------------------------
	 */
	public void EA() { 
		T();
		EAl();		
	}
	
	public void EAl() {
		
		String opA;
		String valor1;
		String valor2;
		
		if(token != null) {
						
			if(token.getType() == token.TK_OP_RELACIONAL && this.OPR == 1) {
				throw new SintaticoCompilerException(" - Expressão Relacional Com mais de 2 OPERADOR RELACIONAL", token);
			}
			
			switch(token.getText()) {
			
			case "+":
				OP("+", "-");
				opA = "+";
				valor1 = this.valorVar;
				T();
				EAl();
				valor2 = this.valorVar;
				this.idEA = "t"+ (this.cont1++);
				ComandoEA cmd1 = new ComandoEA(this.idEA, opA, valor1, valor2);
				curThread.add(cmd1);
				this.valorVar = this.idEA;
				break;
			case "-":
				OP("+", "-");
				opA = "-";
				valor1 = this.valorVar;
				T();				
				
				
				
				EAl();
				valor2 = this.valorVar;
				this.idEA = "t"+ (this.cont1++);
				ComandoEA cmd2 = new ComandoEA(this.idEA, opA, valor1, valor2);
				curThread.add(cmd2);
				this.valorVar = this.idEA;
				break;
			default:
			
			}
		}
	}
	
	public void T() {   
		F();
		Tl();
	}
	
	public void Tl() {
		String opA;
		String valor1;
		String valor2;
		
		token = scan.nextToken();
		
		if(token != null) {
			
			switch(token.getText()) {
			
			case "*":
				OP("*", "/");
				opA = "*";
				if(this.id1 != null) {
					this.valorVar = this.id1;
				}				
				valor1 = this.valorVar;	
				this.id1= null;
				F();
				valor2 = this.valorVar;
				this.idEA = "t"+ (this.cont1++);
				ComandoEA cmd1 = new ComandoEA(this.idEA, opA, valor1, valor2);
				curThread.add(cmd1);
				this.id1 = this.idEA;
				Tl();
				this.valorVar = this.idEA;
				break;
			case "/":
				OP("*", "/");
				opA = "/";
				if(this.id1 != null) {
					this.valorVar = this.id1;
				}
				valor1 = this.valorVar;
				this.id1= null;
				F();
				valor2 = this.valorVar;
				this.idEA = "t"+ (this.cont1++);
				ComandoEA cmd2 = new ComandoEA(this.idEA, opA, valor1, valor2);
				curThread.add(cmd2);
				this.id1 = this.idEA;
				Tl();
				this.valorVar = this.idEA;
				break;
			default:
			
			}
		}
		
	}
	
	public void F() { 
		token = scan.nextToken();
		if(token != null) {
			if (token.getType() != Token.TK_IDENTIFICADOR 
					&& token.getType() != Token.TK_FLOAT 
					&& token.getType() != Token.TK_INTEIRO
					&& token.getType() != Token.TK_CHAR
					&& (token.getType() != Token.TK_C_ESPECIAL || !token.getText().equals("("))) {
										
					throw new SintaticoCompilerException(" - Inexistêcia de IDENTIFICADOR OU NÚMERO OU ABRE PARENTESE", token);
					
			}
			if(token.getText().equals("(")) {			
				EA();
				fParentese();
			}
		}
		else {
			throw new NullSintaticoCompilerException(" - RETORNA NULL - A expressão precisa de pelo menos um IDENTIFICADOR OU NÚMERO ", scan);
		}
		if(this.atrib == 1 && (token.getType() == Token.TK_IDENTIFICADOR 
				|| token.getType() == Token.TK_FLOAT 
				|| token.getType() == Token.TK_INTEIRO
				|| token.getType() == Token.TK_CHAR)) {
			if(!VarCompativel(token.TK_TEXT[token.getType()])) {
				((Variavel)simbolo).setColumn(token.getColumn());
				((Variavel)simbolo).setLine(token.getLine());
				throw new SemanticoCompilerException(" - Atribuição com Tipo da variavel INCORRETO.", (Variavel) simbolo);
			}
			
		}
		if (token.getText().equals(")")) {
			this.valorVar = this.idEA;
		}
		else {
			this.valorVar = token.getText();
		}
		
	}
	
	public void OP(String s1, String s2) { 
		if (token.getType() != Token.TK_OP_ARITMETRICO) {			
							
			throw new SintaticoCompilerException(" - OPERADOR ARITMETICO", token);
				
		}
		
		if (token.getType() == Token.TK_OP_ARITMETRICO 
			&& !token.getText().equals(s1) 
			&& !token.getText().equals(s2)) {				
				
				throw new SintaticoCompilerException(" - SINAL OPERADOR", token);
				
		}
		
		
	}
	
	
	/*
	 * -----------------------------------------------------------
	 * FIM do bloco da Expressão Aritmética 
	 * -----------------------------------------------------------
	 */
	
	
	public boolean VarCompativel(String tipo) {
		Simbolo auxSimb;
		if(tipo.equals("IDENTIFICADOR") && !tabelaSimbolo.existe(token.getText())) {
			return false;
		}
		else if(tipo.equals("IDENTIFICADOR") && tabelaSimbolo.existe(token.getText())){
			auxSimb = tabelaSimbolo.getSimbolo(token.getText());
			tipo = ((Variavel)auxSimb).getType();
			if(tipo.equals("int")) {
				tipo = "INTERIO";
			}
		}
		
		
		//tipo inteiro
		if(tipoVar.equals("int") && !tipo.equals("INTERIO")) {
			
			return false;
		
		}
		//tipo char
		if(tipoVar.equals("char") && !tipo.equalsIgnoreCase("CHAR")) {
			
			return false;
			
		}
		//tipo float
		if(tipoVar.equals("float") && tipo.equalsIgnoreCase("CHAR")) {
			
			return false;
			
		}
		else if(tipoVar.equals("float") && (token.getType() == 0 || tipo.equals("INTEIRO"))) {
			this.idEA = "t"+ (this.cont1++);
			ComandoConvertFloat cmd1 = new ComandoConvertFloat(this.idEA, token.getText());
			curThread.add(cmd1);
			this.valorVar = this.idEA;
			if(token.getType() == 0) {
				int inteiro = Integer.parseInt(token.getText());
				double Float = inteiro * 1.0;
				String text = this.idEA + "(" + Float + ")";
				token.setText(text);
			}
		}
		return true;
	}
	
	public void exibeComando() {
		for(AbstractComando c : programa.getComandos()) {
			System.out.println(c);
		}
	}
	
	public void GerarCodigo() {
		this.programa.generateTarget();
	}

}
