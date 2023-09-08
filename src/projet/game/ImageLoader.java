package projet.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ImageLoader {

	private final Map<String, BufferedImage> images;

	public ImageLoader(String dir, String blank, String... pics) {
		Objects.requireNonNull(pics);
		
		images = new HashMap<>();
		setImage(0, dir, blank);
		for (var i = 0; i < pics.length; i++) {
			setImage(i + 1, dir, pics[i]);
		}
	}

	/**
	 * ajoute des images avec leur nom dans le ImageLoader
	 * @param position
	 * @param dirPath
	 * @param imagePath
	 */
	private void setImage(int position, String dirPath, String imagePath) {
		Objects.requireNonNull(dirPath);
		Objects.requireNonNull(imagePath);
		var path = Path.of(dirPath + "/" + imagePath);
		try (var input = Files.newInputStream(path)) {
			images.put(imagePath, ImageIO.read(input));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return image null
	 */
	public BufferedImage blank() {
		return images.get(null);
	}

	/**
	 * renvoi une image en fonction de son nom
	 * @param img
	 * @return
	 */
	public BufferedImage image(String img) {
		return images.get(img);
	}
	
}