package arvoreGeraCodigo;

public class Label extends AbstractComando{
	
	private String label;
	
	public Label(String label) {		
		this.label = label;		
	}

	@Override
	public String gerarCodInterm() {
		return this.label+":";
	}

	@Override
	public String toString() {
		return this.label+":";
	}

}
