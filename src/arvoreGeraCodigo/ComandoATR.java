package arvoreGeraCodigo;

public class ComandoATR extends AbstractComando{
	
	private String id;
	private String EA;
	
	public ComandoATR(String id, String EA) {
		this.id = id;
		this.EA = EA;
	}

	@Override
	public String gerarCodInterm() {
		return this.id + " = " + this.EA;
	}

	@Override
	public String toString() {		
		return this.id + " = " + this.EA;
	}

}
