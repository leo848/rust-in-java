package leo.rustjava;

@SuppressWarnings({"MethodNameSameAsClassName", "InstantiationOfUtilityClass"})
public final class Unit {
	private static final Unit instance = new Unit();

	private Unit() {
	}

	public static Unit Unit() {
		return instance;
	}
}
