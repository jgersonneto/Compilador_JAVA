package lexico;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import excecoes.LexicoCompilerException;



public class ScannerCompiler {
	
	private char[] content;
	private int    estado;
	private int    pos;
	private int    line;
	private int    column;
	
	public ScannerCompiler (String filename) {
		try {
			line = 1;
			column = 0;
			String txtConteudo;
			txtConteudo = new String(Files.readAllBytes(Paths.get(filename)),StandardCharsets.UTF_8);
			System.out.println("DEBUG --------");
			System.out.println(txtConteudo);
			System.out.println("--------------");
			content = txtConteudo.toCharArray();
			pos=0;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Token nextToken() {
		char currentChar;		
		Token token;
		String term="";
		if (isFim()) {
			return null;
		}
		estado = 0;
		while (true) {
			currentChar = nextChar();
			//System.out.println(currentChar);
			column++;
			
			switch(estado) {
			case 0:
				if (isSpaco(currentChar)) {					
					estado = 0;
				}
				else if (isDigito(currentChar)) {
					estado = 1;
					term += currentChar;
				}
				else if (isAspa(currentChar)) {
					estado = 6;
					term += currentChar;
				}
				
				else if (isUnderLine(currentChar)) {
					estado = 9;
					term += currentChar;
				}
				
				else if (isLetra(currentChar)) {
					estado = 11;
					term += currentChar;
				}
				
				else if (isExclamacao(currentChar)) {
					estado = 14;
					term += currentChar;
				}
				
				else if (isOpRelacional(currentChar)) {
					estado = 15;
					term += currentChar;					
				}
				
				else if (isOpAtribuicao(currentChar)) {
					estado = 16;
					term += currentChar;
				}
				
				else if (isOpAritmetrico(currentChar)) {					
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_OP_ARITMETRICO);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
				
				else if (isCEspecial(currentChar)) {					
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_C_ESPECIAL);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());					
					throw new LexicoCompilerException("Símbolo Desconhecido ", token);
				}
				
				break;
				
			case 1:
				if (isDigito(currentChar)) {
					estado = 1;
					term += currentChar;
				}
				
				else if (currentChar == '.'){
					estado = 3;
					term += currentChar;											
				}
				
				else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
						isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
						isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)){
					if (!isFim(currentChar)) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_INTEIRO);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					else {
						token = new Token();
						token.setType(Token.TK_INTEIRO);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());					
					throw new LexicoCompilerException("Inteiro Incompatível", token);
				}
				
				break;
			case 3:
				if (isDigito(currentChar)) {
					estado = 4;
					term += currentChar;
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Float Incompatível", token);
				}
				
				break;
				
			case 4:
				if (isDigito(currentChar)) {
					estado = 4;
					term += currentChar;
				}
				else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
						isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
						isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
					if (!isFim(currentChar)) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_FLOAT);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					else {
						token = new Token();
						token.setType(Token.TK_FLOAT);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Float Desconhecido", token);
				}
				
				break;
											
			case 6:
				if (isDigito(currentChar) || isLetra(currentChar)) {
					estado = 7;
					term += currentChar;
				}				
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Char Incompatível", token);
				}
				
				break;
				
			case 7:				
				if (isAspa(currentChar)) {					
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_CHAR);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
								
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Char Incompatível", token);
				}
				
				//break;
				
			case 9:
				if (isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
					estado = 10;
					term += currentChar;
				}				
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Identificador Incompatível", token);
				}
				
				break;
				
			case 10:
				if (isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
					estado = 10;
					term += currentChar;
				}
				
				else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
						isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
						isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)){
					if (!isFim(currentChar)) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_IDENTIFICADOR);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					else {
						token = new Token();
						token.setType(Token.TK_IDENTIFICADOR);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Identificador Incompatível", token);
				}
				
				break;
				
			case 11:
				if (term.equals("m") || term.equals("i") || term.equals("e") || 
					term.equals("w") ||	term.equals("d") || term.equals("f") || term.equals("c") ) {				
					if(isLetra(currentChar)) {
						estado = 50;
						term += currentChar;
					}else {
						if(isDigito(currentChar) || isUnderLine(currentChar)) {
							estado = 10;
							term += currentChar;
						}
					
						else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
								isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
								isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
							if (!isFim(currentChar)) {
								back();
								this.column--;
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
							else {
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
						}
						else {
							term += currentChar;
							token = new Token();
							token.setType(Token.TK_ERROS_LEXICOS);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							throw new LexicoCompilerException("Identificador Incompatível", token);
						}						
					}									
				}
				else {
					if(isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
						estado = 10;
						term += currentChar;						
					}
					else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
							isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
							isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
						if (!isFim(currentChar)) {
							back();
							this.column--;
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
						else {
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Identificador Incompatível", token);
					}
				}
				
				break;
				
			case 14:
				if (isOpAtribuicao(currentChar)) {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_OP_RELACIONAL);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Operador Relacional incompatível", token);
				}
				
				//break;
				
			case 15:
				if (isOpAtribuicao(currentChar)) {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_OP_RELACIONAL);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
				
				else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) ||
						isLetra(currentChar) ||	isDigito(currentChar) || isFim(currentChar)){
					if (!isFim(currentChar)) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_OP_RELACIONAL);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Operador Relacional final de texto", token);
					}
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Operador Relacional Incompatível", token);
				}
				
				//break;
				
			case 16:
				if (isOpAtribuicao(currentChar)) {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_OP_RELACIONAL);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
				
				else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
						isDigito(currentChar) || isLetra(currentChar) || isFim(currentChar)){
					if (!isFim(currentChar)) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_OP_ARITMETRICO);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Atribuição em final de texto", token);
					}
				}
				
				else {
					term += currentChar;
					token = new Token();
					token.setType(Token.TK_ERROS_LEXICOS);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					throw new LexicoCompilerException("Simbolo Desconhecido", token);
				}
				
				//break;
				
			case 50:
				if (term.equals("ma") || term.equals("if") || term.equals("el") || term.equals("wh") || 
					term.equals("do") || term.equals("fo") || term.equals("in") || 
					term.equals("fl") || term.equals("ch") ) {
					if((term.equals("if") || term.equals("do")) && (isFim() || isSpaco(currentChar) || currentChar == '(')) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_RESERVADO);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}
					else if(isLetra(currentChar)) {
						estado = 51;
						term += currentChar;
					}else {
						if(isDigito(currentChar) || isUnderLine(currentChar)) {
							estado = 10;
							term += currentChar;
						}
					
						else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
								isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
								isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
							if (!isFim(currentChar)) {
								back();
								this.column--;
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
							else {
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
						}
						else {
							term += currentChar;
							token = new Token();
							token.setType(Token.TK_ERROS_LEXICOS);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							throw new LexicoCompilerException("Identificador Incompatível", token);
						}						
					}					
					
				}				
				else {
					if(isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
						estado = 10;
						term += currentChar;						
					}
					else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
							isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
							isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
						if (!isFim(currentChar)) {
							back();
							this.column--;
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
						else {
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Identificador Incompatível", token);
					}
				}
				
				break;
				
			case 51:
				if (term.equals("mai") || term.equals("els") || term.equals("whi") || 
				term.equals("for") || term.equals("int") || term.equals("flo") || term.equals("cha") ) {
					if((term.equals("for") || term.equals("int")) && (isFim() || isSpaco(currentChar) || currentChar == '(')) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_RESERVADO);
						token.setText(term);
						token.setLine(this.line);
						token.setColumn(this.column - term.length());
						return token;
					}					
					else if(isLetra(currentChar)) {
						estado = 52;
						term += currentChar;
					}else {
						if(isDigito(currentChar) || isUnderLine(currentChar)) {
							estado = 10;
							term += currentChar;
						}
					
						else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
								isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
								isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
							if (!isFim(currentChar)) {
								back();
								this.column--;
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
							else {
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
						}
						else {
							term += currentChar;
							token = new Token();
							token.setType(Token.TK_ERROS_LEXICOS);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							throw new LexicoCompilerException("Identificador Incompatível", token);
						}						
					}
				}				
				else {
					if(isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
						estado = 10;
						term += currentChar;						
					}
					else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
							isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
							isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
						if (!isFim(currentChar)) {
							back();
							this.column--;
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
						else {
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Identificador Incompatível", token);
					}
				}
				
				break;
				
			case 52:
				if (term.equals("main") || term.equals("else") || term.equals("whil") || 
				term.equals("floa") || term.equals("char") ) {
					if((term.equals("main") || term.equals("else") || term.equals("char")) 
							&& (isFim() || isSpaco(currentChar) || currentChar == '(')) {
						back();
						this.column--;
						token = new Token();
						token.setType(Token.TK_RESERVADO);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						return token;
					}										
					else if(isLetra(currentChar)) {
						estado = 53;
						term += currentChar;
					}else {
						if(isDigito(currentChar) || isUnderLine(currentChar)) {
							estado = 10;
							term += currentChar;
						}
					
						else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
								isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
								isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
							if (!isFim(currentChar)) {
								back();
								this.column--;
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
							else {
								token = new Token();
								token.setType(Token.TK_IDENTIFICADOR);
								token.setText(term);
								token.setLine(line);
								token.setColumn(column - term.length());
								return token;
							}
						}
						else {
							term += currentChar;
							token = new Token();
							token.setType(Token.TK_ERROS_LEXICOS);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							throw new LexicoCompilerException("Identificador Incompatível", token);
						}						
					}
				}				
				else {
					if(isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
						estado = 10;
						term += currentChar;						
					}
					else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
							isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
							isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
						if (!isFim(currentChar)) {
							back();
							this.column--;
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
						else {
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Identificador Incompatível", token);
					}
				}
				
				break;
				
			case 53:
				if ((term.equals("while") ||	term.equals("float")) && (isFim() || isSpaco(currentChar) || currentChar == '(')) {
					back();
					this.column--;
					token = new Token();
					token.setType(Token.TK_RESERVADO);
					token.setText(term);
					token.setLine(line);
					token.setColumn(column - term.length());
					return token;
				}
				else {
					if(isDigito(currentChar) || isLetra(currentChar) || isUnderLine(currentChar)) {
						estado = 10;
						term += currentChar;						
					}
					else if (isSpaco(currentChar) || isOpAritmetrico(currentChar) || 
							isOpAtribuicao(currentChar) || isExclamacao(currentChar) || 
							isOpRelacional(currentChar) || isCEspecial(currentChar) || isFim(currentChar)) {
						if (!isFim(currentChar)) {
							back();
							this.column--;
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
						else {
							token = new Token();
							token.setType(Token.TK_IDENTIFICADOR);
							token.setText(term);
							token.setLine(line);
							token.setColumn(column - term.length());
							return token;
						}
					}
					else {
						term += currentChar;
						token = new Token();
						token.setType(Token.TK_ERROS_LEXICOS);
						token.setText(term);
						token.setLine(line);
						token.setColumn(column - term.length());
						throw new LexicoCompilerException("Identificador Incompatível", token);
					}
				}
				
				break;
				
			}
		}
		
		
		
	}
	
	private boolean isDigito(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isLetra(char c) {
		return (c >= 'a' && c <= 'z');
	}
	
	private boolean isAspa(char c) {
		return (c == '\'');
	}
	
	private boolean isUnderLine(char c) {
		return (c == '_');
	}
	
	private boolean isExclamacao(char c) {
		return (c == '!');
	}
	
	private boolean isOpRelacional(char c) {
		return c == '>' || c == '<';
	}
	
	private boolean isOpAritmetrico(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	
	private boolean isOpAtribuicao(char c) {
		return c == '=';
	}
	
	private boolean isSpaco(char c) {
		if (c == '\n') {
			line++;
			column = 0;
		}
		return c == ' ' || c == '\t' || c == '\n' || c == '\r'; 
	}
	
	private boolean isCEspecial(char c) {
		return c == '(' || c == ')' || c == '{' || c == '}' || c == ';' ||
				c == ',' || c == '%' || c == ':' || c == '&' || c == '\\';
	}
	
	private char nextChar() {
		if (isFim()) {
			return '\0';
		}		
		return content[pos++];
	}
	public boolean isFim() {
		return pos >= content.length;
	}
	
    private void back() {
    	pos--;
    }
    
    private boolean isFim(char c) {
    	return c == '\0';
    }
    
    public boolean ComparaProxToken(String s1) {
    	Token token;
    	int auxPos = this.pos;
    	int auxLine = this.line;
    	int auxColumn = this.column;
    	
    	token = nextToken();
    	if(token != null && token.getText().equals(s1)) {
    		this.pos = auxPos;
    		this.line = auxLine;
    		this.column = auxColumn;
    		return true;
    	}
    	
    	this.pos = auxPos;
    	this.line = auxLine;
		this.column = auxColumn;
    	return false;
    }
    
    public void tColuna() {
    	this.column = this.column-1;
    }
    
    public boolean ComparaProxTokenType() {
    	Token token;
    	int auxPos = this.pos;
    	int auxLine = this.line;
    	int auxColumn = this.column;
    	token = nextToken();
    	if(token != null && token.getType() == Token.TK_IDENTIFICADOR) {
    		this.pos = auxPos;
    		this.line = auxLine;
    		this.column = auxColumn;
    		return true;
    	}
    	
    	this.pos = auxPos;
    	this.line = auxLine;
		this.column = auxColumn;
    	return false;
    }
    
    public int getLine() {
    	return this.line;
    }
    
    public int getColumn() {
    	return this.column;
    }

}
