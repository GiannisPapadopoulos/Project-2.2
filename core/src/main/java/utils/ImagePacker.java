package utils;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class ImagePacker {

	public static void run() {
		TexturePacker2.Settings settings = new TexturePacker2.Settings();
		settings.maxHeight = 4096;
		settings.maxWidth = 4096;
		TexturePacker2.processIfModified(settings, "assets/textures", "assets/packed-textures", "textures.pack");
	}

}
