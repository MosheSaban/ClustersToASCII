package application;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import entities.Cluster;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * Moshe Saban
 */

public class Main extends Application{
	
	private static List<Cluster> clusters;
	private static int width;
	private static int height;
	private static HashMap<String, Integer> clustersMap;
	private static HashMap<Integer, Character> asciiMap;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		StackPane sp = new StackPane();
		Scene root = new Scene(sp, 750, 750);
		primaryStage.setScene(root);
		primaryStage.show();
		
	}
	
	
	public static void main(String[] args) {
		
		String fileContent;
		asciiMap = new HashMap<Integer, Character>();
		clustersMap = new HashMap<String, Integer>();
		BufferedImage origImg;
		BufferedImage resultImage;
		
		try {
			origImg = ImageIO.read(new File("./src/input/moshe_small.png"));
			resultImage = kMeans(origImg, 6);
			setASCIIMap();
			fileContent = praperToFile();
            makeFile(fileContent);
            saveResultImage(resultImage);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		launch(args);
		
	}
	
	/*
	 * setASCIIMap()
	 * Get: void
	 * Return: void
	 * This method initializes the ASCII map.
*/
	public static void setASCIIMap() {
		
		asciiMap.put(clusters.get(0).getId(), '@');
		asciiMap.put(clusters.get(1).getId(), '$');
		asciiMap.put(clusters.get(2).getId(), '&');
		asciiMap.put(clusters.get(3).getId(), '*');
		asciiMap.put(clusters.get(4).getId(), '^');
		asciiMap.put(clusters.get(5).getId(), '|');
	}

	public static BufferedImage kMeans(BufferedImage image, int k) {
        
        width = image.getWidth(); 
        height = image.getHeight(); 
        clusters = new ArrayList<Cluster>();
        int x = 0; 			 int y = 0;      
        int dx = width  / k; int dy = height / k;
        boolean changeFlag;
        
        for (int i=0; i < k; i++) {		// Initialize the k centers.
            clusters.add(new Cluster(i, image.getRGB(x, y))); 
            x += dx; y += dy; 
        } 
        
        do  {
            changeFlag = false;    
            for (y=0; y < height; y++)  
                for (x=0; x < width; x++) { 
                    int pixel = image.getRGB(x, y);
                    String position = "(" + x + "," + y + ")";
                    Cluster cluster = findMinimalCluster(pixel); 
                    if(!clustersMap.containsKey(position))		 		// If the hash map not contain this key 
                	    clustersMap.put(position, -1);
                    if (clustersMap.get(position) != cluster.getId()) { // If we need to find new cluster to the pixel. 
                    		if (clustersMap.get(position) != -1)		// If the cluster is not empty one.
                                clusters.get(clustersMap.get(position))
                                		.removePixel(pixel); 
                            cluster.addPixel(pixel); 		  			// Add the pixel value to the cluster. 
                            clustersMap.put(position, cluster.getId()); // Update the new value.
                            changeFlag = true; 				  			// The clusters are not stable yet so continuum the loop.
                    } 
                }            
        } while(changeFlag); 
        BufferedImage resultImageultImage = new BufferedImage(width, height, image.getType());
        
        for (y=0; y < height; y++)  
            for (x=0; x < width; x++) { 
            	String position = "(" + x + "," + y + ")";
            	int clusterId = clustersMap.get(position);
                resultImageultImage.setRGB(x, y, clusters.get(clusterId).getRGB()); 
            } 
        
       Collections.sort(clusters);
       return resultImageultImage;
	}
	
    public static Cluster findMinimalCluster(int pixel) {
    	
        Cluster cluster = null; 
        int min = Integer.MAX_VALUE;
        
        for (int i=0; i < clusters.size(); i++) { 
            int distance = clusters.get(i).distance(pixel); 
            if (distance < min) { 
                min = distance; 
                cluster = clusters.get(i); 
            } 
        }
        return cluster; 
    }
    
    /*
     * praperToFile()
     * Get: void
     * Return: String - fileContent
     * fileContent are fill by the ASCII lines and break line (\n) in the end of each line.   
      */
    private static String praperToFile() {
    	
    	String fileContent = "";
        for(int y=0; y < height; y++) {  
            for(int x=0; x < width; x++) {
            		String position = "(" + x + "," + y + ")";
            		fileContent = fileContent + " " + asciiMap.get(clustersMap.get(position)); 
            }
            fileContent = fileContent + "\n";
        } 
        return fileContent;
    }
    
    private static void makeFile(String fileContent) {
    	BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter("./src/output/asciiPic.txt");
			bw = new BufferedWriter(fw);
			bw.write(fileContent);
			//System.out.println(fileContent);
		} catch (IOException e) { e.printStackTrace();}
		finally {
			try {
					if (bw != null) bw.close();
					if (fw != null) fw.close();
				} catch (IOException ex) { ex.printStackTrace(); }
		}
    }
    
    private static void saveResultImage(BufferedImage resultImage) {
		try {
			ImageIO.write(resultImage, "png", new File("./src/output/out.png"));		
		}catch (Exception e){ e.printStackTrace(); }
    }



}
