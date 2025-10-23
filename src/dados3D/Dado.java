package dados3D;

import java.util.Random;

public class Dado {
	private final int caras;
	private final Random random = new Random();

	public Dado(int caras) {
		this.caras = caras;
	}

	public int tirar() {
		return random.nextInt(caras) + 1;
	}

	public int getCaras() {
		return caras;
	}
}
