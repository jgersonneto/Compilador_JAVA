package lexico;

public class Token {
	public static final int TK_INTEIRO         = 0;
	public static final int TK_FLOAT           = 1;
	public static final int TK_CHAR            = 2;
	public static final int TK_IDENTIFICADOR   = 3;
	public static final int TK_OP_RELACIONAL   = 4;
	public static final int TK_OP_ARITMETRICO  = 5;
	public static final int TK_C_ESPECIAL      = 6;
	public static final int TK_RESERVADO       = 7;
	public static final int TK_ERROS_LEXICOS   = 8;
	public static final int TK_ERROS_SITATICO  = 9;
	
	public static final String TK_TEXT[] = {
		"INTERIO", "FLOAT", "CHAR", "IDENTIFICADOR", "OPERAÇÕES RELACIONAIS",
			"OPERAÇÕES ARITMÉTRICA", "CARACTERES ESPECIAIS", "PALAVRA RESERVADA", "ERROS LEXICOS", "ERROS SINTATICO" 
	};
	
	private int    type;
	private String text;
	private int    line;
	private int    column;
	
	public Token(int type, String text) {
		super();
		this.type = type;
		this.text = text;
	}

	public Token() {
		super();
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}	

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	@Override
	public String toString() {
		return "Token [type=" + TK_TEXT[type] + ", text= |" + text + "| ]";
	}

}
