package arvoreGeraCodigo;

public class ComandoConvertFloat extends AbstractComando{
	
	private String id;
	private String convertF;
	private String valor;
	
	

	public ComandoConvertFloat(String id, String valor) {		
		this.id = id;
		this.convertF = "(float)";
		this.valor = valor;
	}

	@Override
	public String gerarCodInterm() {
		return this.id + " = " + this.convertF + " " + this.valor;
	}

	@Override
	public String toString() {
		return this.id + " = " + this.convertF + " " + this.valor;
	}

}
