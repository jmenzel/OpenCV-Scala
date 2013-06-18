package webcamCaptureTesting


import org.opencv.objdetect.CascadeClassifier
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Core
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.core.Size


object FaceDetector 
{
  val SCALE_FACTOR = 3;
  
  
  
	//val xml_file = "E:/Data/haarcascades/haarcascade_frontalface_default.xml"
	val xml_file = "E:/Data/lbpcascades/lbpcascade_frontalface.xml"
    val faceDetector = new CascadeClassifier(xml_file);
	
    
	def markWithRect(input:Mat, op:Int): Mat = 
	{
	  if(input.empty()) 
	  { 
	    println("emptyMat")
	    input 
	  }
	  else
	  {
	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    val greyMat = new Mat();
	    Imgproc.cvtColor(input, greyMat, Imgproc.COLOR_BGR2GRAY)
	    Imgproc.resize(greyMat, greyMat, new Size( input.size().width / SCALE_FACTOR, input.size().height / SCALE_FACTOR))
	    val faceDetections = new MatOfRect();
	    
	    faceDetector.detectMultiScale(greyMat, faceDetections);
	    
	    // Draw a bounding box around each face.
	    faceDetections.toArray().par.foreach( rect => Core.rectangle(input, new Point(rect.x * SCALE_FACTOR, rect.y * SCALE_FACTOR), new Point(rect.x * SCALE_FACTOR + rect.width * SCALE_FACTOR, rect.y * SCALE_FACTOR + rect.height * SCALE_FACTOR), new Scalar(0, 255, 0)))
	    input
	  }
	}
}