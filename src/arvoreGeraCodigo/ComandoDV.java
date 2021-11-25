package arvoreGeraCodigo;

public class ComandoDV extends AbstractComando{
	
	private String tipo;
	private String id;
	
	public ComandoDV(String tipo, String id) {
		this.tipo = tipo;
		this.id = id;
	}

	@Override
	public String gerarCodInterm() {		
		return this.tipo + " " + this.id;
	}

	@Override
	public String toString() {		
		return this.tipo + " " + this.id;
	}

}
