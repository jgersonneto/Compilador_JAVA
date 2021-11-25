package arvoreGeraCodigo;

public class ComandoEA extends AbstractComando{
	
	private String id;
	private String op;
	private String valor1;
	private String valor2;
	
	
	
	public ComandoEA(String id, String op, String valor1, String valor2) {		
		this.id = id;
		this.op = op;
		this.valor1 = valor1;
		this.valor2 = valor2;
	}

	@Override
	public String gerarCodInterm() {
		return id + " = " + valor1 +" "+ op +" "+ valor2;
	}

	@Override
	public String toString() {
		return id + " = " + valor1 +" "+ op +" "+ valor2;
	}
	
	
	

}
