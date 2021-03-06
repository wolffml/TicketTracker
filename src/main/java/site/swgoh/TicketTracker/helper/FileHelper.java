package site.swgoh.TicketTracker.helper;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import static java.nio.file.StandardWatchEventKinds.*;

public class FileHelper {
	private static Logger logger = Logger.getLogger(FileHelper.class);
	
	public static void waitForFiles(String watchDir) {
		logger.info("Waiting for files in Directory: " + watchDir);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(new Date());
		Path watchdir = Paths.get(watchDir);
		//try (final WatchService watchService = FileSystems.getDefault().newWatchService()){
		try{
			WatchService watchService = watchdir.getFileSystem().newWatchService();
			
			watchdir.register(watchService,	StandardWatchEventKinds.ENTRY_CREATE);
			
			//final WatchKey watchKey = watchdir.register(watchService, ENTRY_CREATE);
			
			while (true){
				final WatchKey wk = watchService.take();
				logger.info("Watching dir.");
				for (WatchEvent<?> event : wk.pollEvents()){
					final Path changed = (Path) event.context();
					logger.info(changed.getFileName());
					//Try loading files to an S3 bucket
					//AWSS3Helper.copyFile(Paths.get(watchDir + changed.getFileName()));
					AWSS3Helper.copyFile(dateString +"/"+ changed.getFileName(), watchDir + changed.getFileName());
				}
				wk.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		
	}
	
	public static void moveFiles(String sourceDir, String glob) {
		logger.debug("Moving files from: " + sourceDir);
		Path sourceDirPath = Paths.get(sourceDir);
    	
    	
    	DirectoryStream<Path> directoryStream;
		try {
			directoryStream = Files.newDirectoryStream(sourceDirPath, glob);
			for (Path path : directoryStream) {
				logger.info(path.getFileName().toString());
				String destDir = FileHelper.getDateFromFile(path.getFileName().toString());
				logger.info("dest dir from filename: " + destDir);
				
				String fullDestDir = sourceDir + "/" + destDir;
				logger.info("Full Dest dir: " + fullDestDir);
				
				mkDir(fullDestDir);
				Path destDirPath = Paths.get(fullDestDir);
				
	            logger.debug("copying " + path.toString());
	            Path d2 = destDirPath.resolve(path.getFileName());
	            logger.debug("destination File=" + d2);
	            Files.move(path, d2, REPLACE_EXISTING);
	        }
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	
	public static String mkDailyDir(String rootDir){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(new Date());
        logger.debug("Today's Date: " + dateString);
        return mkDailyDir(rootDir, dateString);
        
	}
	
	public static String mkDailyDir(String rootDir, String foldername){
		String dailyDirString = rootDir + foldername + "\\";
        //Make the new Daily Directory
        FileHelper.mkDir(dailyDirString);
        return dailyDirString;
	}
	
	public static void mkDir(String path){
		logger.debug("Creating directory: " + path);
		File newDir = new File(path);
    	boolean mkDirSuccess = newDir.mkdir();
    	logger.debug("CreateDir status: " + mkDirSuccess);
	}

	public static void moveToAmazon(String sourceDir, String glob) {
		logger.debug("Moving files from: " + sourceDir + " to Amazon.");
		Path sourceDirPath = Paths.get(sourceDir);
    	
    	DirectoryStream<Path> directoryStream;
		try {
			directoryStream = Files.newDirectoryStream(sourceDirPath, glob);
			for (Path path : directoryStream) {
				String s3PathString = getDateFromFile(path.getFileName().toString()) +"/" + path.getFileName().toString();
				String sourceFileString = path.toAbsolutePath().toString();
				logger.info("Uploading file: " + sourceFileString + " to S3 object:" + s3PathString);
				AWSS3Helper.copyFile(s3PathString, sourceFileString);
	        }
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	public static String getDateFormatString(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
   
	public static String getDateFromFile(String fileName){
		SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssSSS");
		SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
		String datePart= fileName.substring(fileName.indexOf("_") + 1, 
				fileName.indexOf("."));
		
		String strReturn = "";
		
		try{
			Date dateFromString = sdfIn.parse(datePart);
			strReturn = sdfOut.format(dateFromString);
		} catch(ParseException e){
			logger.error(e.getMessage());
		}
		logger.info("String version:" + strReturn);
		
		return strReturn;
	}

	public static Date getDateFromFileD(String fileName) {
		SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssSSS");
		SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
		String datePart= fileName.substring(fileName.indexOf("shot_") + 5, 
				fileName.indexOf("."));
		logger.info("DatePare: " + datePart);
		String strReturn = "";
		Date dateFromString = new Date();
		try{
			dateFromString = sdfIn.parse(datePart);
			strReturn = sdfOut.format(dateFromString);
		} catch(ParseException e){
			logger.error(e.getMessage());
		}
		logger.info("String version:" + strReturn);
		
		return dateFromString;
	}
	
}
