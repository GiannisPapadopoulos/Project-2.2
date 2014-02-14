package utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.imagepacker.TexturePacker;
import com.badlogic.gdx.tools.imagepacker.TexturePacker.Settings;

public class ImagePacker {

	public static void run() {
		Settings settings = new Settings();
		settings.padding = 1;
		settings.incremental = true;
		settings.stripWhitespace = false;
		settings.minHeight = 1024;
		settings.minWidth = 1024;
		settings.defaultFilterMin = Texture.TextureFilter.Linear;
		settings.defaultFilterMag = Texture.TextureFilter.Linear;
		TexturePacker.process(settings, "assets/textures", "assets/packed-textures");
	}

}
