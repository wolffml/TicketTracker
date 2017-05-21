package site.swgoh.TicketTracker.helper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;


public class AWSS3Helper {
	private static Logger logger = Logger.getLogger(AWSS3Helper.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	private static final ResourceBundle bundleS = ResourceBundle.getBundle("secrets");
	
	private static final String access_key_id = bundleS.getString("aws.accesskey");
	private static final String secret = bundleS.getString("aws.secret");
	private static final BasicAWSCredentials awsCreds = new BasicAWSCredentials(access_key_id, secret);
	private static final String awsS3Bucket = bundle.getString("awss3.bucketname");
	
	private static final AmazonS3 s3 =  AmazonS3ClientBuilder.standard()
										.withRegion(Regions.US_EAST_2)
										//.withRegion(Regions.DEFAULT_REGION)
										.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
										.build();
	private static final TransferManager tx = TransferManagerBuilder.standard()
										.withS3Client(s3)
										.build();
			
			
	TransferManagerBuilder tmb;
	public static final AmazonS3 getS3(){
		return s3;
	}
	
	public static void copyFile(String bucketName, String s3Path, String filePath){
		logger.info("Putting file: " + filePath + " in S3 bucket: " + bucketName + " with S3Key: " + s3Path);
		File file = new File(filePath);
		
		try {
			
		    //s3.putObject(bucketName, s3Path, filePath);
			Upload xfer = tx.upload(bucketName, s3Path, file);
			logger.info("Transfer in progress.");
            // loop with Transfer.isDone()
			xfer.waitForCompletion();
			logger.info("Transfer complete.");
			//xfer.
            //  or block with Transfer.waitForCompletion()
			
		} catch (AmazonServiceException e) {
		    //System.err.println(e.getErrorMessage());
		    logger.error(e.getErrorMessage());
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("File copied to S3 bucket.");
	}
	
	public static void copyFile(String s3Path, String filePath){
		copyFile(awsS3Bucket, s3Path, filePath);
	}
	
	public static void copyFile(Path path){
		String filePath = path.toAbsolutePath().toString();
		String s3Path = path.getFileName().toString();
		copyFile(s3Path, filePath);
	}
	
	
	public static void listObjects(String bucketName){
		//List Files in a bucket
		ObjectListing ol = s3.listObjects(bucketName);
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		for (S3ObjectSummary os: objects) {
		    logger.info("* " + os.getKey());
		}
	}
	
	
		
}
