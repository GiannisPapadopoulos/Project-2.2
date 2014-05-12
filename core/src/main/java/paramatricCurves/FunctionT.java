package paramatricCurves;

public class FunctionT {

	Get_F_t def;
	
	public FunctionT(Get_F_t def) {
		this.def = def;
	}
	
	public double getFt(double t) {
		return def.getFt(t);
	}

}
