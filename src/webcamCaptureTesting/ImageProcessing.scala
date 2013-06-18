package webcamCaptureTesting

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgproc.Imgproc
import org.opencv.core.Size
import org.opencv.core.Point3
import org.opencv.core.MatOfPoint2f

object ImageProcessing 
{
  def blur(src: Mat, op: Int) : Mat =
  {
    val dest = new Mat
        
    val saveOp = if (op == 0)  1 else  op
    
	Imgproc.blur(src, dest, new Size(saveOp, saveOp))
	dest
  }
  
  def warp(src: Mat, op: Int) : Mat =
  {
    val warp_dst = new Mat
    val warp_rotate_dst = new Mat
    
    val a = 0.33
    val b = 0.85
    val c = 0.25
    val d = 0.15
    val e = 0.7
    
    val srcTri = new MatOfPoint2f(new Point(0, 0), new Point(src.cols - 1, 0), new Point(0, src.cols - 1))
    val dstTri = new MatOfPoint2f(new Point(src.cols*0.0, src.rows*a), new Point(src.cols*b, src.rows*c),new Point(src.cols*d, src.rows*e))
    
    val warp_mat = Imgproc.getAffineTransform(srcTri, dstTri)
    
    Imgproc.warpAffine(src, warp_dst, warp_mat, warp_dst.size)
    
    val center = new Point( warp_dst.cols/2, warp_dst.rows/2 );
    val angle = op * 3.6;
    val scale = 0.6;
    
    val rot_mat = Imgproc.getRotationMatrix2D(center, angle, scale)
    Imgproc.warpAffine( warp_dst, warp_rotate_dst, rot_mat, warp_dst.size )
    
    warp_rotate_dst 
  }
}