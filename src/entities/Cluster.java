package entities;


public class Cluster implements Comparable<Cluster>{
	
	  private int clusterID; 
      private int pixelCount; 
      private int red; 
      private int green; 
      private int blue; 
      private int reds; 
      private int greens; 
      private int blues; 
       
      public Cluster(int id, int color) {
    	  
          int r = (color >> 16) & 0xFF;  
          int g = (color >>  8) & 0xFF;  
          int b = (color >>  0) & 0xFF;   
          red   = r; 
          green = g; 
          blue  = b; 
          clusterID = id; 
          addPixel(color);
          
      } 
       
      public int getRGB() {
    	  
          int r = reds 	 / pixelCount; 
          int g = greens / pixelCount; 
          int b = blues	 / pixelCount;
          
          return 0xFF000000 | r << 16 | g << 8 | b;
          
      } 
      public void addPixel(int color) {
    	  
          int r = (color >> 16) & 0xFF;  
          int g = (color >>  8) & 0xFF;  
          int b = (color >>  0) & 0xFF;  
          reds 	+= r; 
          greens+= g; 
          blues += b; 
          pixelCount++; 
          red   = reds	 / pixelCount; 
          green = greens / pixelCount; 
          blue  = blues	 / pixelCount;
          
      } 
       
      
      public void removePixel(int color) {
    	  
          int r = (color >> 16) & 0xFF;  
          int g = (color >>  8) & 0xFF;  
          int b = (color >>  0) & 0xFF;  
          reds	 -= r; 
          greens -= g; 
          blues	 -= b; 
          pixelCount--; 
          red   = reds   / pixelCount; 
          green = greens / pixelCount; 
          blue  = blues  / pixelCount;
          
      } 
       
      public int distance(int color) { 
    	  
          int r = (color >> 16) & 0xFF;  
          int g = (color >>  8) & 0xFF;  
          int b = (color >>  0) & 0xFF; 
          int rx = Math.abs(this.red   - r); 
          int gx = Math.abs(this.green - g); 
          int bx = Math.abs(this.blue  - b); 
    
          return rx+gx+bx; 
          
      }

	@Override
	public int compareTo(Cluster o) {
		return this.red - o.red + this.blue - o.blue + this.green - o.green;
	}
	
	public int getId() { return clusterID; } 
      
}
