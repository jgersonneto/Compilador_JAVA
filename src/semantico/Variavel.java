package semantico;

public class Variavel extends Simbolo {
	
	public static final int INTEIRO = 0;
	public static final int FLOAT = 1;
	public static final int CHAR = 2;
	
	
	private String type;
	private String valor;
	private int line;
	private int column;
	private int bloco;
	

	public Variavel(String nome, String type, String valor, int bloco, int line, int column) {
		super(nome);
		this.type = type;
		this.valor = valor;
		this.line = line;
		this.column = column;
		this.bloco = bloco;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getBloco() {
		return bloco;
	}

	public void setBloco(int bloco) {
		this.bloco = bloco;
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
		return "Variavel [nome=" + nome + ", type=" + type + ", valor=" + valor + "]";
	}
}
