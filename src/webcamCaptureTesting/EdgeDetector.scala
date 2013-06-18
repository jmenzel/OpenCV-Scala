package webcamCaptureTesting

import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.core.Size
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import java.util.{ArrayList => JList}
import org.opencv.core.CvType
import scala.util.Random

object EdgeDetector 
{
  
  def detect(src: Mat, lowThreshold:Int) : Mat =
  {
	  val dest = new Mat
	  val src_gray = new Mat
	  val detected_edges = new Mat
	  
	  val edgeThresh = 1
	  val max_lowThreshhold = 100
	  val ratio = 3
	  val kernel_size = 3
	  
	  Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY)
	  Imgproc.blur(src_gray, detected_edges, new Size(3,3))
	  Imgproc.Canny(detected_edges, detected_edges, lowThreshold*ratio, kernel_size);
	  
	  src.copyTo(dest, detected_edges)
	  dest
  }
  
  def markConturs(src:Mat, threshold:Int) : Mat = 
  {
    val sthreshold = if(threshold <= 0) 1 else threshold
    val matList = new JList[MatOfPoint]
    val hierarchy = new Mat
    
    val dest = new Mat
    val src_gray = new Mat
    val detected_edges = new Mat
  
    val edgeThresh = 1
    val max_lowThreshhold = 100
    val ratio = 3
    val kernel_size = 3
  
    Imgproc.cvtColor(src, src_gray, Imgproc.COLOR_BGR2GRAY)
    Imgproc.blur(src_gray, detected_edges, new Size(3,3))
    Imgproc.Canny(detected_edges, detected_edges, sthreshold*ratio, kernel_size);
    Imgproc.findContours(detected_edges, matList, hierarchy, 3, 2, new Point(0, 0))
    
    val drawing = Mat.zeros( detected_edges.size, CvType.CV_8UC3)
       
    for(i <- 0 to matList.size - 1) 
      Imgproc.drawContours(drawing, matList, i, new Scalar(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)), 2, 8, hierarchy, 0, new Point)
    
    drawing
  }
}