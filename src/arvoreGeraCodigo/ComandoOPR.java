package arvoreGeraCodigo;

public class ComandoOPR extends AbstractComando{
	
	private String ID;
	private String EA1;
	private String OPR;
	private String EA2;

	public ComandoOPR(String ID, String eA1, String oPR, String eA2) {
		this.ID = ID;
		this.EA1 = eA1;
		this.OPR = oPR;
		this.EA2 = eA2;
	}

	@Override
	public String gerarCodInterm() {
		return this.ID + " = " + this.EA1 + " " + this.OPR + " " + this.EA2;
	}

	@Override
	public String toString() {		
		return this.ID + " = " + this.EA1 + " " + this.OPR + " " + this.EA2;
	}

}
