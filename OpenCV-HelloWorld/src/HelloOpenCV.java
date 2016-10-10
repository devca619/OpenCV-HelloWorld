import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
class DetectFaceDemo {
  public void run() {
    System.out.println("\nRunning DetectFaceDemo");
    System.out.println(System.getProperty("java.class.path"));
    System.out.println(getClass().getResource("lbpcascade_frontalface.xml").getPath());
    // Create a face detector from the cascade file in the resources
    // directory.
    CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("lbpcascade_frontalface.xml").getPath());
    Mat image = Imgcodecs.imread(getClass().getResource("g20.jpg").getPath());
    // Detect faces in the image.
    // MatOfRect is a special container class for Rect.
    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);
    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
    // Draw a bounding box around each face.
    for (Rect rect : faceDetections.toArray()) {
        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
    }
    // Save the visualized detection.
    String filename = "g20-faceDetection.png";
    System.out.println(String.format("Writing %s", filename));
    Imgcodecs.imwrite(filename, image);
    displayImage(image);
  }
  
  public BufferedImage Mat2BufferedImage(Mat m){
	  // source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
	  // Fastest code
	  // The output can be assigned either to a BufferedImage or to an Image

	  int type = BufferedImage.TYPE_BYTE_GRAY;
	  if ( m.channels() > 1 ) {
		  type = BufferedImage.TYPE_3BYTE_BGR;
	  }
	  int bufferSize = m.channels()*m.cols()*m.rows();
	  byte [] b = new byte[bufferSize];
	  m.get(0,0,b); // get all the pixels
	  BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
	  final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	  System.arraycopy(b, 0, targetPixels, 0, b.length);  
	  return image;

  }
  
  public void displayImage(Mat img)
  {   
	  //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
	  BufferedImage image = Mat2BufferedImage(img);
	  ImageIcon icon=new ImageIcon(image);
	  JFrame frame=new JFrame();
	  frame.setLayout(new FlowLayout());        
	  frame.setSize(image.getWidth(null)+50, image.getHeight(null)+50);     
	  JLabel lbl=new JLabel();
	  lbl.setIcon(icon);
	  frame.add(lbl);
	  frame.setVisible(true);
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}


public class HelloOpenCV {
  public static void main(String[] args) {
    System.out.println("Hello, OpenCV");
    // Load the native library.
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    new DetectFaceDemo().run();
  }
}

