import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Arty_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private float aspect;
    
  public Arty_GLEventListener(Camera camera) {
    this.camera = camera;
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    aspect = (float)width/(float)height;
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    disposeMeshes(gl);
  }
  
  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
   
  private boolean animation = false;
  private double savedTime = 0;
   
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }
  
  private void updateMove() {
    exhibitMoveTranslate.setTransform(Mat4Transform.translate(xPosition,0,0));
    exhibitMoveTranslate.update();
  }
  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   */

  private Camera camera;
  private Mat4 perspective;
  private Mesh floor, cube, cube2;
  private Light light;
  private SGNode exhibit;
  
  private float xPosition = 0;
  //adding some transform nodes for finger rotations
  private TransformNode exhibitMoveTranslate, pinkief1Rotate, pinkief2Rotate, pinkief3Rotate, 
												ringf1Rotate, ringf2Rotate, ringf3Rotate, 
													middlef1Rotate, middlef2Rotate, middlef3Rotate, 
														indexf1Rotate, indexf2Rotate, indexf3Rotate, 
															thumb1Rotate, thumb2Rotate, thumb3Rotate;
  
  private void initialise(GL3 gl) {
    createRandomNumbers();
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/Woodfloor.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/darkwood1_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/jup0vss1.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/jup0vss1_specular.jpg");
	
	
    floor = new TwoTriangles(gl, textureId0);
    floor.setModelMatrix(Mat4Transform.scale(25,1,25));   
    cube = new Cube(gl, textureId3, textureId4);
    cube2= new Cube(gl, textureId5, textureId6);

    light = new Light(gl);
    light.setCamera(camera);
    
    floor.setLight(light);
    floor.setCamera(camera);
    cube.setLight(light);
    cube.setCamera(camera);  
    cube2.setLight(light);
    cube2.setCamera(camera);  
	
	// make nodes
    MeshNode podiumShape = new MeshNode("Cube(podium)", cube);
    MeshNode handShape = new MeshNode("Cube(hand)", cube);
    MeshNode pinkieShape1 = new MeshNode("Cube(pinkie)", cube);
	MeshNode pinkieShape2 = new MeshNode("Cube(pinkie)", cube);
	MeshNode pinkieShape3 = new MeshNode("Cube(pinkie)", cube);
	MeshNode ringShape1 = new MeshNode("Cube(ring)", cube);
	MeshNode ringShape2 = new MeshNode("Cube(ring)", cube);
	MeshNode ringShape3 = new MeshNode("Cube(ring)", cube);
	MeshNode middleShape1 = new MeshNode("Cube(middle)", cube);
	MeshNode middleShape2 = new MeshNode("Cube(middle)", cube);
	MeshNode middleShape3 = new MeshNode("Cube(middle)", cube);
	MeshNode indexShape1 = new MeshNode("Cube(index)", cube);
	MeshNode indexShape2 = new MeshNode("Cube(index)", cube);
	MeshNode indexShape3 = new MeshNode("Cube(index)", cube);
    MeshNode thumbShape1 = new MeshNode("Cube(thumb)", cube);
    MeshNode thumbShape2 = new MeshNode("Cube(thumb)", cube);
    MeshNode thumbShape3 = new MeshNode("Cube(thumb)", cube);
    
    exhibit = new NameNode("root");
	//arm
    NameNode podium = new NameNode("podium");
	//hand
    NameNode hand = new NameNode("hand");
	//little finger
    NameNode pinkief1= new NameNode("pinkie1");
	NameNode pinkief2= new NameNode("pinkie2");
	NameNode pinkief3= new NameNode("pinkie3");
	//ring finger
    NameNode ringf1 = new NameNode("ring1");
	NameNode ringf2 = new NameNode("ring2");
	NameNode ringf3 = new NameNode("ring3");
	///middle finger
    NameNode middlef1 = new NameNode("middle1");
	NameNode middlef2 = new NameNode("middle2");
	NameNode middlef3 = new NameNode("middle3");
	//index finger
    NameNode indexf1 = new NameNode("index1");
	NameNode indexf2 = new NameNode("index2");
	NameNode indexf3 = new NameNode("index3");
	//thumb
	NameNode thumb1 = new NameNode("thumb1");
	NameNode thumb2 = new NameNode("thumb2");
	NameNode thumb3 = new NameNode("thumb3");
	
	// exhibit
    
    float podiumHeight = 3f;
    float podiumWidth = 1.5f;
    float podiumDepth = 1f;
    float handHeight = 3f;
    float handWidth = 3f;
    float handDepth = 1f;
	float fingerScale = 0.5f;
    float fingerPinkie = 1.2f;
	float fingerRing = 1.4f;
	float fingerMiddle = 1.6f;
	float fingerIndex = 1.5f;
	float thumb = 1.2f;
	
	
    TransformNode exhibitTranslate = new TransformNode("exhibit transform",Mat4Transform.translate(0,0,0));
    
    //arm transformations
	Mat4 m = Mat4Transform.scale(podiumWidth,podiumHeight,podiumDepth);
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
	TransformNode podiumTransform = new TransformNode("podium transform", m);
		
	//hand transformations
	m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,podiumHeight,0));
    m = Mat4.multiply(m, Mat4Transform.scale(handWidth,handHeight,handDepth));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode handTransform = new TransformNode("hand transform", m);
	
	//pinkie finger bottom segment transformations
	TransformNode pinkief1ToTop = new TransformNode("pinkie to top of hand", Mat4Transform.translate(-handWidth*0.4f,podiumHeight+handHeight+0.2f,0));
    pinkief1Rotate = new TransformNode("pinkie1 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerPinkie,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode pinkief1Transform = new TransformNode("pinkief1 transform", m);
	
	//pinkie finger middle segment transformations
	TransformNode pinkief2ToTop = new TransformNode("pinkie to top of hand", Mat4Transform.translate(0,fingerPinkie+0.2f,0));
    pinkief2Rotate = new TransformNode("pinkie2 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerPinkie,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode pinkief2Transform = new TransformNode("pinkief2 transform", m);
	
	//pinkie finger top segment transformations
	TransformNode pinkief3ToTop = new TransformNode("pinkie to top of hand", Mat4Transform.translate(0,fingerPinkie+0.2f,0));
    pinkief2Rotate = new TransformNode("pinkie3 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerPinkie,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode pinkief3Transform = new TransformNode("pinkief3 transform", m);
	
	//ring finger bottom segment transformations
	TransformNode ringf1ToTop = new TransformNode("ring to top of hand", Mat4Transform.translate(-handWidth*0.15f,podiumHeight+handHeight+0.2f,0));
    ringf1Rotate = new TransformNode("ring rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerRing,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode ringf1Transform = new TransformNode("ringf1 transform", m);
	
	//ring finger middle segment transformations
	TransformNode ringf2ToTop = new TransformNode("ring to top of hand", Mat4Transform.translate(0,fingerRing+0.2f,0));
    ringf2Rotate = new TransformNode("ring2 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerRing,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode ringf2Transform = new TransformNode("ringf2 transform", m);
	
	//ring finger top segment transformations
	TransformNode ringf3ToTop = new TransformNode("ring to top of hand", Mat4Transform.translate(0,fingerRing+0.2f,0));
    ringf2Rotate = new TransformNode("ring3 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerRing,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode ringf3Transform = new TransformNode("ringf3 transform", m);
	
	//middle finger segment transformations
	TransformNode middlef1ToTop = new TransformNode("middle to top of hand", Mat4Transform.translate(handWidth*0.1f,podiumHeight+handHeight+0.2f,0));
    middlef1Rotate = new TransformNode("middle rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerMiddle,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode middlef1Transform = new TransformNode("middlef1 transform", m);
	
	//middle finger middle segment transformations
	TransformNode middlef2ToTop = new TransformNode("middle to top of hand", Mat4Transform.translate(0,fingerMiddle+0.2f,0));
    middlef2Rotate = new TransformNode("middle2 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerMiddle,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode middlef2Transform = new TransformNode("middlef2 transform", m);
	
	//middle finger top segment transformations
	TransformNode middlef3ToTop = new TransformNode("middle to top of hand", Mat4Transform.translate(0,fingerMiddle+0.2f,0));
    middlef2Rotate = new TransformNode("middle3 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerMiddle,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode middlef3Transform = new TransformNode("middlef3 transform", m);
	
	//index finger bottom segment transformations
	TransformNode indexf1ToTop = new TransformNode("index to top of hand", Mat4Transform.translate(handWidth*0.35f,podiumHeight+handHeight+0.2f,0));
    indexf1Rotate = new TransformNode("index rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerIndex,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode indexf1Transform = new TransformNode("indexf1 transform", m);
	
	//index finger index segment transformations
	TransformNode indexf2ToTop = new TransformNode("index to top of hand", Mat4Transform.translate(0,fingerIndex+0.2f,0));
    indexf2Rotate = new TransformNode("index2 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerIndex,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode indexf2Transform = new TransformNode("indexf2 transform", m);
	
	//index finger top segment transformations
	TransformNode indexf3ToTop = new TransformNode("index to top of hand", Mat4Transform.translate(0,fingerIndex+0.2f,0));
    indexf2Rotate = new TransformNode("index3 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerIndex,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode indexf3Transform = new TransformNode("indexf3 transform", m);
	
	//thumb bottom segment transformations
	TransformNode thumb1ToTop = new TransformNode("thumb to top of hand", Mat4Transform.translate(handWidth/2f+fingerScale,podiumHeight+handHeight/2f,0));
    thumb1Rotate = new TransformNode("thumb rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,thumb,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode thumb1Transform = new TransformNode("thumb1 transform", m);
	
	//thumb finger thumb segment transformations
	TransformNode thumb2ToTop = new TransformNode("thumb to top of hand", Mat4Transform.translate(0,thumb+0.2f,0));
    thumb2Rotate = new TransformNode("thumb2 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,thumb,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode thumb2Transform = new TransformNode("thumb2 transform", m);
	
	//thumb finger top segment transformations
	TransformNode thumb3ToTop = new TransformNode("thumb to top of hand", Mat4Transform.translate(0,thumb+0.2f,0));
    thumb2Rotate = new TransformNode("thumb3 rotate",Mat4Transform.rotateAroundX(180));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,thumb,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode thumb3Transform = new TransformNode("thumb3 transform", m);

	exhibit.addChild(exhibitTranslate);
      exhibitTranslate.addChild(podium);
          podium.addChild(podiumTransform);
          podiumTransform.addChild(podiumShape);
          podium.addChild(hand);
			hand.addChild(handTransform);
			handTransform.addChild(handShape);
			hand.addChild(pinkief1ToTop);
				pinkief1ToTop.addChild(pinkief1);
				pinkief1.addChild(pinkief1Transform);
				pinkief1Transform.addChild(pinkieShape1);
				pinkief1.addChild(pinkief2ToTop);
					pinkief2ToTop.addChild(pinkief2);
					pinkief2.addChild(pinkief2Transform);
					pinkief2Transform.addChild(pinkieShape2);
					pinkief2.addChild(pinkief3ToTop);
						pinkief3ToTop.addChild(pinkief3);
						pinkief3.addChild(pinkief3Transform);
						pinkief3Transform.addChild(pinkieShape3);
			hand.addChild(ringf1ToTop);
				ringf1ToTop.addChild(ringf1);
				ringf1.addChild(ringf1Transform);
				ringf1Transform.addChild(ringShape1);
				ringf1.addChild(ringf2ToTop);
					ringf2ToTop.addChild(ringf2);
					ringf2.addChild(ringf2Transform);
					ringf2Transform.addChild(ringShape2);
					ringf2.addChild(ringf3ToTop);
						ringf3ToTop.addChild(ringf3);
						ringf3.addChild(ringf3Transform);
						ringf3Transform.addChild(ringShape3);
			hand.addChild(middlef1ToTop);
				middlef1ToTop.addChild(middlef1);
				middlef1.addChild(middlef1Transform);
				middlef1Transform.addChild(middleShape1);
				middlef1.addChild(middlef2ToTop);
					middlef2ToTop.addChild(middlef2);
					middlef2.addChild(middlef2Transform);
					middlef2Transform.addChild(middleShape2);
					middlef2.addChild(middlef3ToTop);
						middlef3ToTop.addChild(middlef3);
						middlef3.addChild(middlef3Transform);
						middlef3Transform.addChild(middleShape3);
			hand.addChild(indexf1ToTop);
				indexf1ToTop.addChild(indexf1);
				indexf1.addChild(indexf1Transform);
				indexf1Transform.addChild(indexShape1);
				indexf1.addChild(indexf2ToTop);
					indexf2ToTop.addChild(indexf2);
					indexf2.addChild(indexf2Transform);
					indexf2Transform.addChild(indexShape2);
					indexf2.addChild(indexf3ToTop);
						indexf3ToTop.addChild(indexf3);
						indexf3.addChild(indexf3Transform);
						indexf3Transform.addChild(indexShape3);
			hand.addChild(thumb1ToTop);
				thumb1ToTop.addChild(thumb1);
				thumb1.addChild(thumb1Transform);
				thumb1Transform.addChild(thumbShape1);
				thumb1.addChild(thumb2ToTop);
					thumb2ToTop.addChild(thumb2);
					thumb2.addChild(thumb2Transform);
					thumb2Transform.addChild(thumbShape2);
					thumb2.addChild(thumb3ToTop);
						thumb3ToTop.addChild(thumb3);
						thumb3.addChild(thumb3Transform);
						thumb3Transform.addChild(thumbShape3);
          
    
    exhibit.update();  // IMPORTANT - don't forget this
    //robot.print(0, false);
    //System.exit(0);
  }
  
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    updatePerspectiveMatrices();
    
    light.setPosition(getLightPosition());  // changing light position each frame
    light.render(gl);

    floor.render(gl); 
    
    exhibit.draw(gl);
  }
  
  private void updatePerspectiveMatrices() {
    // needs to be changed if user resizes the window
    perspective = Mat4Transform.perspective(45, aspect);
    light.setPerspective(perspective);
    floor.setPerspective(perspective);
    cube.setPerspective(perspective);
    cube2.setPerspective(perspective);
  }
  
  private void disposeMeshes(GL3 gl) {
    light.dispose(gl);
    floor.dispose(gl);
    cube.dispose(gl);
    cube2.dispose(gl);
  }
  
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 5.0f;  //*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 5f;
    float z = 5.0f;  //*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }
  
}