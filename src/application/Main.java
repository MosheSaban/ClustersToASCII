package application;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import entities.Cluster;

/*
 * Moshe Saban
 */

public class Main {
	
	private static List<Cluster> clusters;
	private static int width;
	private static int height;
	private static int[][] lookupMat;
	private static HashMap<Integer, Character> map;
	
	public static void main(String[] args) {
		
		String fileContent = "";
		map = new HashMap<Integer, Character>();
		BufferedImage origImg;
		BufferedImage resultImage;
		
		try {
			origImg = ImageIO.read(new File("./src/input/me.png"));
			resultImage = kMeans(origImg, 6);
			map.put(clusters.get(0).getId(), '@');
			map.put(clusters.get(1).getId(), '$');
			map.put(clusters.get(2).getId(), '&');
			map.put(clusters.get(3).getId(), '*');
			map.put(clusters.get(4).getId(), '^');
			map.put(clusters.get(5).getId(), '|');
			
			fileContent = praperToFile(fileContent);
            makeFile(fileContent);
            saveResultImage(resultImage);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
       			
        lookupMat = new int[height][width]; 
        for(int i=0; i < height; i++)
        	Arrays.fill(lookupMat[i], -1);
        
        do  {
            changeFlag = false;    
            for (y=0; y < height; y++)  
                for (x=0; x < width; x++) { 
                    int pixel = image.getRGB(x, y); 
                    Cluster cluster = findMinimalCluster(pixel); 
                    if (lookupMat[y][x] != cluster.getId()) { // If we need to find new cluster to the pixel. 
                            if (lookupMat[y][x] != -1)		  // If the cluster is not empty one.
                                clusters.get(lookupMat[y][x])
                                		.removePixel(pixel); 
                            cluster.addPixel(pixel); 		  // Add the pixel value to the cluster. 
                            lookupMat[y][x] = cluster.getId();// Update lookup matrix.
                            changeFlag = true; 				  // The clusters are not stable yet so continuum the loop.
                    } 
                }            
        } while(changeFlag); 
        BufferedImage resultImageultImage = new BufferedImage(width, height, image.getType());
        
        for (y=0; y < height; y++)  
            for (x=0; x < width; x++) { 
                int clusterId = lookupMat[y][x]; 
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
    
    private static String praperToFile(String fileContent) {
        for(int y=0; y < height; y++) {  
            for(int x=0; x < width; x++)
            		fileContent = fileContent + " " + map.get(lookupMat[y][x]); 
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
