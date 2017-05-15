package site.swgoh.TicketTracker.helper;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;


public class FileHelper {
	private static Logger logger = Logger.getLogger(FileHelper.class);
	
	public static void moveFiles(String sourceDir, String destDir, String glob) {
		logger.debug("Moving files from: " + sourceDir + " to: " + destDir);
		Path sourceDirPath = Paths.get(sourceDir);
    	Path destDirPath = Paths.get(destDir);
    	
    	DirectoryStream<Path> directoryStream;
		try {
			directoryStream = Files.newDirectoryStream(sourceDirPath, glob);
			for (Path path : directoryStream) {
	            logger.debug("copying " + path.toString());
	            Path d2 = destDirPath.resolve(path.getFileName());
	            logger.debug("destination File=" + d2);
	            Files.move(path, d2, REPLACE_EXISTING);
	        }
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	public static void moveFiles(String sourceDir, String destDir) {
		moveFiles(sourceDir, destDir, "*.*");
	}
	
	
	public static void mkDir(String path){
		logger.debug("Creating directory: " + path);
		File newDir = new File(path);
    	boolean mkDirSuccess = newDir.mkdir();
    	logger.debug("CreateDir status: " + mkDirSuccess);
	}
}
