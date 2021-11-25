package arvoreGeraCodigo;

public class ComandoWHILE extends AbstractComando{
	
	private String WHILE;
	private String ER;
	private String GOTO;
	private String Label;
	

	public ComandoWHILE(String ER, String label) {
		this.WHILE = "while_false";
		this.ER = ER;
		this.GOTO = "GOTO";
		this.Label = label;
	}

	@Override
	public String gerarCodInterm() {
		return this.WHILE + " " + this.ER + " " + this.GOTO + " " + this.Label;
	}

	@Override
	public String toString() {
		return this.WHILE + " " + this.ER + " " + this.GOTO + " " + this.Label;
	}

}
