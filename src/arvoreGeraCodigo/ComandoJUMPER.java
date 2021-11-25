package arvoreGeraCodigo;

public class ComandoJUMPER extends AbstractComando{
	
	private String Label;
	private String GOTO;
	
	public ComandoJUMPER(String label) {
		this.Label = label;
		this.GOTO = "GOTO";
	}

	@Override
	public String gerarCodInterm() {
		return this.GOTO + " " + this.Label;
	}

	@Override
	public String toString() {
		return this.GOTO + " " + this.Label;
	}

}
