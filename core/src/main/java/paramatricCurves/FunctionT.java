package paramatricCurves;

public class FunctionT {

	Get_F_t def;
	
	public FunctionT(Get_F_t def) {
		this.def = def;
	}
	
	public float getFt(float t) {
		return def.getFt(t);
	}

	@Override
	public String toString() {
		return "FunctionT [def=" + def + "]";
	}

}
