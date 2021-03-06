package webcamCaptureTesting

import scala.swing._
import java.awt.image.BufferedImage
import org.opencv.core.Mat
import org.opencv.highgui.Highgui
import org.opencv.core.MatOfByte
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import org.opencv.highgui.VideoCapture
import org.opencv.objdetect.CascadeClassifier
import org.opencv.core.MatOfRect
import org.opencv.core.Core
import org.opencv.highgui.Highgui
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.swing.event.ButtonClicked

object SimpleGui extends SimpleSwingApplication
{  
  System.loadLibrary("opencv_java245");
  
  val cap = new VideoCapture(0);
  val x = new Highgui
  val label = new Label
  val slider = new Slider() { min = 0; max = 100 }
  
  // List with ToggleButtons
  
  val blist = new ToggleButton
  			 {
	        	text = "No Action  "
    			reactions += { case ButtonClicked(_) => { unToggleButtonsExceptOf(this); showWebcam(this)} }
  			 } :: new ToggleButton {
  			   text = "Face Detect"
			   reactions += { case ButtonClicked(_) => unToggleButtonsExceptOf(this); showWebcam(this, FaceDetector.markWithRect) }
  			 } :: new ToggleButton {
  			   text = "Edge Detect"
  			   reactions += { case ButtonClicked(_) => unToggleButtonsExceptOf(this); showWebcam(this, EdgeDetector.detect) }
  			 } :: new ToggleButton {
  			   text = "Image Blur"
  			   reactions += { case ButtonClicked(_) => unToggleButtonsExceptOf(this); showWebcam(this, ImageProcessing.blur) }
  			 } :: new ToggleButton {
  			   text = "Image Warp"
  			   reactions += { case ButtonClicked(_) => unToggleButtonsExceptOf(this); showWebcam(this, ImageProcessing.warp) }
  			 } :: new ToggleButton {
  			   text = "Contours"
  			   reactions += { case ButtonClicked(_) => unToggleButtonsExceptOf(this); showWebcam(this, EdgeDetector.markConturs) }
  			 } :: Nil
  			 
  
  
  // Untoggle all other Buttons
  def unToggleButtonsExceptOf(b: ToggleButton): Unit =
  {
    blist.filterNot(_.text==b.text).foreach(_.selected=false)
  }
    
  def top = new MainFrame 
  {
    // Component for displaying the image
        val imageView = label

        // Layout frame contents
        
        //Global Flowpanel
        contents = new FlowPanel
        {
          //Boxpanel for left Side
          contents += new BoxPanel(Orientation.Vertical)
          {
            //add buttons and slider
            blist.foreach(contents.append(_))
            contents.append(slider)                       
	      }
          
          //flowpanel for image
          contents += new FlowPanel
          {
            val imageScrollPane = new ScrollPane(imageView) {
                preferredSize = new Dimension(640, 480)
            }
            contents.append(imageScrollPane)
          }
        }
        
        //center window on the screen
        centerOnScreen()
        
        //start sho the webcam with no manipulation
        blist(0).selected = true
        showWebcam(blist(0))
  }
  
  def aktImage(orig: Mat) =
  {
    val bytes = new MatOfByte();

    //Umwandeln des Bildes
    Highgui.imencode(".jpg", orig, bytes); 

    val byteArr = bytes.toArray();
    val bufImage: BufferedImage = null

    try 
    {
        //Bytes als Bild umrechnen
        val in = new ByteArrayInputStream(byteArr);
        val bufImage = ImageIO.read(in);
        
        //Auf Gui anzeigen
        label.icon = new ImageIcon(bufImage)
    } 
    catch
    {
      case e:Exception => println(e)
    }
  }
  
 
  def showWebcam(b: ToggleButton, f:(Mat,Int) => Mat = (x:Mat, y:Int) => x) =
  {
    import org.opencv.core._
    import scala.util.{Success, Failure}
     
	if(cap.isOpened)
	{
	  inner
	  
	  def inner: Unit = 
	  {
	      //Async Block starten
		  val fu = future
		  {
		    val frame = new Mat
		    
		    //Bild von Webcam holen
		    cap.retrieve(frame)
		    
		    //HOF für Manipulation anwenden
		    if(!frame.empty()) aktImage(f(frame, slider.value))
		    
		    //Kurze Pause ;)
			Thread.sleep(35);
		    
		  }onComplete { 
		    //Und von vorne
	     	case Success(result) => if(b.selected) inner 
	     	case Failure(failure) => println(failure) 
	      }
		  ()
	  }
	  
	}

  }
}