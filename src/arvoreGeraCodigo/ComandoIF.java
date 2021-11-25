package arvoreGeraCodigo;

public class ComandoIF extends AbstractComando{
	
	private String IF;
	private String ER;
	private String GOTO;
	private String Label;
	
	
	public ComandoIF(String ER, String label) {		
		this.IF = "if_false";
		this.ER = ER;
		this.GOTO = "GOTO";
		this.Label = label;
	}

	@Override
	public String gerarCodInterm() {
		return this.IF + " " + this.ER + " " + this.GOTO + " " + this.Label;
	}

	@Override
	public String toString() {
		return this.IF + " " + this.ER + " " + this.GOTO + " " + this.Label;
	}

}
