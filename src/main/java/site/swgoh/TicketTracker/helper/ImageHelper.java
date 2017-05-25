package site.swgoh.TicketTracker.helper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;


public class ImageHelper {
	private static Logger logger = Logger.getLogger(ImageHelper.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	//Constants for image cropping
	private static final int startX = Integer.parseInt(bundle.getString("cropping.startX"));
	private static final int startY = Integer.parseInt(bundle.getString("cropping.startY"));
	private static final int endX = Integer.parseInt(bundle.getString("cropping.endX"));
	private static final int endY = Integer.parseInt(bundle.getString("cropping.endY"));
	private static final String outputFilePrefix = bundle.getString("cropping.fileprefix");
	
	
	public static String cropImages(String dailyDirString){
    	/* Not too much to say here.  This procedure just takes a directly location as a parameter
    	 * and will create a bunch of cropped images in the subfolder defined in app.properties as "cropped.dir"
    	 */
    	String croppedDirString = dailyDirString + bundle.getString("cropped.dir");
    	FileHelper.mkDir(croppedDirString);
        
    	Path sourceDirPath = Paths.get(dailyDirString);
    	try {
	    	DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceDirPath, "*.png");
	    	for (Path path : directoryStream) {
	    		logger.debug("Opening file: " + path.toString());
	            File sourceFile = path.toFile();
	            BufferedImage sourceImage = ImageIO.read(sourceFile);
	            BufferedImage img = sourceImage.getSubimage(startX, startY, endX, endY); //fill in the corners of the desired crop location here
	            BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
	            Graphics g = copyOfImage.createGraphics();
	            g.drawImage(img, 0, 0, null);
	            g.dispose();
	            //Now save the Image file in the destination folder
	            String outputFileString = croppedDirString + outputFilePrefix + sourceFile.getName();
	            File outputFile = new File(outputFileString);
	            logger.debug("Writing cropped file: " + outputFileString);
	            ImageIO.write(copyOfImage, "png", outputFile);
	        }
    	}catch (IOException e) {
			logger.error(e.toString());
		}
    	return croppedDirString;
    }
	
}
