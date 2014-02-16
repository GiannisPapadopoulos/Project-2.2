package utils;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class ImagePacker {

	public static void run() {
		TexturePacker2.process("assets/textures", "assets/packed-textures", "textures.pack");
	}

}
