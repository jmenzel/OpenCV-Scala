import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.highgui.Highgui
import org.opencv.objdetect.CascadeClassifier

class DetectFaceDemo {
  def run = 
  {
    println("\nRunning DetectFaceDemo");

    // Create a face detector from the cascade file in the resources
    // directory.
    val faceDetector = new CascadeClassifier("E:/Projekte/Studium/Scala/OpenCVTest/resources/lbpcascade_frontalface.xml");
    val image = Highgui.imread("E:/Projekte/Studium/Scala/OpenCVTest/resources/lena.png");

    // Detect faces in the image.
    // MatOfRect is a special container class for Rect.
    val faceDetections = new MatOfRect();
    faceDetector.detectMultiScale( image, faceDetections);

    println("Detected "+faceDetections.toArray().length+" faces");

    // Draw a bounding box around each face.
    
    faceDetections.toArray().foreach( rect => Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0)))
    

    // Save the visualized detection.
    val filename = "faceDetection.png";
    println("Writing " + filename);
    Highgui.imwrite(filename, image);
  }
}


object FirstTest {

  def main(args: Array[String]):Unit = 
  {
    try
    {
	    println("Hello, OpenCV");
	
	    // Load the native library.
	    System.loadLibrary("opencv_java245");
	    val x = new DetectFaceDemo run
    }
    catch
    {
      case ex:Exception => println(ex.getLocalizedMessage()); ex.getStackTrace.foreach( x => println(x))
    }
  }
  
}