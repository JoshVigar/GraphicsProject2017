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
  
  public void loff() {
    light.getMaterial().setDiffuse(0,0,0);
  }
  
  public void lon() {
    light.getMaterial().setDiffuse(0.8f,0.8f,0.8f);
  }
  
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }
  
  public void Vpos(float angle) {
	reset();
	curlPinkie(angle);
	curlRing(angle);
	bendFingerZ(middlef1Rotate, angle/3f);
	bendFingerZ(indexf1Rotate, -angle/3f);
	bendFingerX(thumb1Rotate, angle*0.6f); 
	curlThumb(angle);
  }
  
  public void Ipos(float angle) {
	reset();
	curlRing(angle);
	curlMiddle(angle);
	curlIndex(angle);
	bendFingerX(thumb1Rotate, angle*0.6f); 
	curlThumb(angle);
  }
  
  public void Gpos(float angle) {
	reset();
	curlPinkie(angle);
	curlRing(angle);
	bendFingerX(middlef1Rotate, angle); 
	bendFingerX(indexf1Rotate, angle); 
	bendFingerX(thumb1Rotate, angle*0.7f); 
  }
  
  public void salute(float angle){
	reset();
	bendFingerZ(indexf1Rotate, -angle);
	bendFingerZ(middlef1Rotate, -angle);
	bendFingerZ(ringf1Rotate, angle);
	bendFingerZ(pinkief1Rotate, angle);
	bendFingerZ(thumb1Rotate, -angle);
  }
  
  //call to bend individual segments in X axis
  private void bendFingerX(TransformNode joint, float degree) {
	joint.setTransform(Mat4Transform.rotateAroundX(degree));
	joint.update();	 
  }
  
  //call to bend individual segments in the Z axis
  private void bendFingerZ(TransformNode joint, float degree) {
	joint.setTransform(Mat4Transform.rotateAroundZ(degree));
	joint.update();	 
  }
  
  //curl all segments of a digit by the same degree in the X axis
  private void curlPinkie(float degree) {
	pinkief1Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	pinkief2Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	pinkief3Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	pinkief1Rotate.update();	
	pinkief2Rotate.update();	
	pinkief3Rotate.update();	 
  }
  private void curlRing(float degree) {
	ringf1Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	ringf2Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	ringf3Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	ringf1Rotate.update();	
	ringf2Rotate.update();	
	ringf3Rotate.update();	 
  }
  private void curlMiddle(float degree) {
	middlef1Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	middlef2Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	middlef3Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	middlef1Rotate.update();	
	middlef2Rotate.update();	
	middlef3Rotate.update();	 
  }
  private void curlIndex(float degree) {
	indexf1Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	indexf2Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	indexf3Rotate.setTransform(Mat4Transform.rotateAroundX(degree));
	indexf1Rotate.update();	
	indexf2Rotate.update();	
	indexf3Rotate.update();	 
  }
  //only curls last two segments because of different axis
  private void curlThumb(float degree) {
	thumb2Rotate.setTransform(Mat4Transform.rotateAroundZ(degree));
	thumb3Rotate.setTransform(Mat4Transform.rotateAroundZ(degree));
	thumb2Rotate.update();	
	thumb3Rotate.update();	 
  }
  
  //resets position of all fingers
  public void reset(){
	bendFingerZ(indexf1Rotate, 0);
	bendFingerZ(middlef1Rotate, 0);
	bendFingerZ(ringf1Rotate, 0);
	bendFingerZ(pinkief1Rotate, 0);
	bendFingerZ(thumb1Rotate, 0);
	curlIndex(0);
	curlMiddle(0);
	curlRing(0);
	curlPinkie(0);
	curlThumb(0);
	bendFingerX(thumb1Rotate, 0); 
  }
  
  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   */

  private Camera camera;
  private Mat4 perspective;
  private Mesh floor, wallwb, wallwsl, wallwsr, wallwt, wall2, wall3, wall4, lamp, lamp2, lampb, lamp2b, ceiling, cube;
  private Light light, lampLight, lampLightb;
  private SGNode exhibit;
 
  //adding some transform nodes for finger rotations
  private TransformNode exhibitMoveTranslate, pinkief1Rotate, pinkief2Rotate, pinkief3Rotate, 
												ringf1Rotate, ringf2Rotate, ringf3Rotate, 
													middlef1Rotate, middlef2Rotate, middlef3Rotate, 
														indexf1Rotate, indexf2Rotate, indexf3Rotate, 
															thumb1Rotate, thumb2Rotate, thumb3Rotate;
  
  private void initialise(GL3 gl) {
    createRandomNumbers();
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/floor.jpg");
	int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/wall.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/ceiling.jpg");
	int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
	int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
	int[] textureIdwb = TextureLibrary.loadTexture(gl, "textures/wallbottom.jpg");
	int[] textureIdwt = TextureLibrary.loadTexture(gl, "textures/walltop.jpg");
	int[] textureIdws = TextureLibrary.loadTexture(gl, "textures/wallside.jpg");
	
	lamp = new Cube(gl, textureId2, textureId3);
	lamp2 = new Sphere(gl, textureId5, textureId6);
	lampb = new Cube(gl, textureId2, textureId3);
	lamp2b = new Sphere(gl, textureId5, textureId6);
    floor = new TwoTriangles(gl, textureId0);
    floor.setModelMatrix(Mat4Transform.scale(35,1,35));   
	wallwb = new TwoTriangles(gl, textureIdwb);
	wallwsl = new TwoTriangles(gl, textureIdws);
	wallwsr = new TwoTriangles(gl, textureIdws);
	wallwt = new TwoTriangles(gl, textureIdwt);
	wall2 = new TwoTriangles(gl, textureId1);
	wall3 = new TwoTriangles(gl, textureId1);
	wall4 = new TwoTriangles(gl, textureId1);
	ceiling = new TwoTriangles(gl, textureId4);
   
	//wall transformations
	Mat4 n = Mat4Transform.scale(35,7.8f,35);
    n = Mat4.multiply(n, Mat4Transform.translate(0,0.5f,-0.5f));
    n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	wallwb.setModelMatrix(n);  
	n = new Mat4(1);
	n = Mat4Transform.scale(10,6.1f,10f);
    n = Mat4.multiply(n, Mat4Transform.translate(-1.25f,1.75f,-1.75f));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	wallwsl.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(10,6.1f,10f);
    n = Mat4.multiply(n, Mat4Transform.translate(1.25f,1.75f,-1.75f));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	wallwsr.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(35,6.28f,35);
	n = Mat4.multiply(n, Mat4Transform.translate(0,2.685f,-0.5f));
    n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	wallwt.setModelMatrix(n);  
	n = new Mat4(1);
	n = Mat4Transform.scale(35,20,35);
    n = Mat4.multiply(n, Mat4Transform.translate(0.5f,0.5f,0));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundZ(90));
	wall2.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(35,20,35);
    n = Mat4.multiply(n, Mat4Transform.translate(0,0.5f,0.5f));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundZ(180));
	wall3.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(35,20,35);
    n = Mat4.multiply(n, Mat4Transform.translate(-0.5f,0.5f,0));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundX(90));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundZ(-90));
	wall4.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(35,20,35);
    n = Mat4.multiply(n, Mat4Transform.translate(0,1f,0));
	n = Mat4.multiply(n, Mat4Transform.rotateAroundX(180));
	ceiling.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(0.5f,9.5f,0.5f);
    n = Mat4.multiply(n, Mat4Transform.translate(-30,0.5f,-30));
	lamp.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(1,1,1);
    n = Mat4.multiply(n, Mat4Transform.translate(-15,9.8f,-15));
	lamp2.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(0.5f,9.5f,0.5f);
    n = Mat4.multiply(n, Mat4Transform.translate(30,0.5f,-30));
	lampb.setModelMatrix(n);
	n = new Mat4(1);
	n = Mat4Transform.scale(1,1,1);
    n = Mat4.multiply(n, Mat4Transform.translate(15,9.8f,-15));
	lamp2b.setModelMatrix(n);
	
    cube = new Cube(gl, textureId3, textureId4);
    light = new Light(gl);
	lampLight = new Light(gl);
	lampLightb = new Light(gl);
    light.setCamera(camera);
	lampLight.setCamera(camera);
	lampLightb.setCamera(camera);
    lamp.setLight(light);
    lamp.setCamera(camera);
	lamp2.setLight(light);
    lamp2.setCamera(camera);
	lampb.setLight(light);
    lampb.setCamera(camera);
	lamp2b.setLight(light);
    lamp2b.setCamera(camera);
    floor.setLight(light);
    floor.setCamera(camera);
	wallwb.setLight(light);
	wallwb.setCamera(camera);
	wallwsl.setLight(light);
	wallwsl.setCamera(camera);
	wallwsr.setLight(light);
	wallwsr.setCamera(camera);
	wallwt.setLight(light);
	wallwt.setCamera(camera);
	wall2.setLight(light);
	wall2.setCamera(camera);
	wall3.setLight(light);
	wall3.setCamera(camera);
	wall4.setLight(light);
	wall4.setCamera(camera);
	ceiling.setLight(light);
	ceiling.setCamera(camera);
    cube.setLight(light);
    cube.setCamera(camera);   
	
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
    pinkief1Rotate = new TransformNode("pinkie1 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerPinkie,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode pinkief1Transform = new TransformNode("pinkief1 transform", m);
	
	//pinkie finger middle segment transformations
	TransformNode pinkief2ToTop = new TransformNode("pinkie to top of hand", Mat4Transform.translate(0,fingerPinkie+0.2f,0));
    pinkief2Rotate = new TransformNode("pinkie2 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerPinkie,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode pinkief2Transform = new TransformNode("pinkief2 transform", m);
	
	//pinkie finger top segment transformations
	TransformNode pinkief3ToTop = new TransformNode("pinkie to top of hand", Mat4Transform.translate(0,fingerPinkie+0.2f,0));
    pinkief3Rotate = new TransformNode("pinkie3 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerPinkie,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode pinkief3Transform = new TransformNode("pinkief3 transform", m);
	
	//ring finger bottom segment transformations
	TransformNode ringf1ToTop = new TransformNode("ring to top of hand", Mat4Transform.translate(-handWidth*0.15f,podiumHeight+handHeight+0.2f,0));
    ringf1Rotate = new TransformNode("ring rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerRing,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode ringf1Transform = new TransformNode("ringf1 transform", m);
	
	//ring finger middle segment transformations
	TransformNode ringf2ToTop = new TransformNode("ring to top of hand", Mat4Transform.translate(0,fingerRing+0.2f,0));
    ringf2Rotate = new TransformNode("ring2 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerRing,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode ringf2Transform = new TransformNode("ringf2 transform", m);
	
	//ring finger top segment transformations
	TransformNode ringf3ToTop = new TransformNode("ring to top of hand", Mat4Transform.translate(0,fingerRing+0.2f,0));
    ringf3Rotate = new TransformNode("ring3 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerRing,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode ringf3Transform = new TransformNode("ringf3 transform", m);
	
	//middle finger bottom segment transformations
	TransformNode middlef1ToTop = new TransformNode("middle to top of hand", Mat4Transform.translate(handWidth*0.1f,podiumHeight+handHeight+0.2f,0));
    middlef1Rotate = new TransformNode("middle rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerMiddle,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode middlef1Transform = new TransformNode("middlef1 transform", m);
	
	//middle finger middle segment transformations
	TransformNode middlef2ToTop = new TransformNode("middle to top of hand", Mat4Transform.translate(0,fingerMiddle+0.2f,0));
    middlef2Rotate = new TransformNode("middle2 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerMiddle,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode middlef2Transform = new TransformNode("middlef2 transform", m);
	
	//middle finger top segment transformations
	TransformNode middlef3ToTop = new TransformNode("middle to top of hand", Mat4Transform.translate(0,fingerMiddle+0.2f,0));
    middlef3Rotate = new TransformNode("middle3 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerMiddle,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode middlef3Transform = new TransformNode("middlef3 transform", m);
	
	//index finger bottom segment transformations
	TransformNode indexf1ToTop = new TransformNode("index to top of hand", Mat4Transform.translate(handWidth*0.35f,podiumHeight+handHeight+0.2f,0));
    indexf1Rotate = new TransformNode("index rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerIndex,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode indexf1Transform = new TransformNode("indexf1 transform", m);
	
	//index finger middle segment transformations
	TransformNode indexf2ToTop = new TransformNode("index to top of hand", Mat4Transform.translate(0,fingerIndex+0.2f,0));
    indexf2Rotate = new TransformNode("index2 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerIndex,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode indexf2Transform = new TransformNode("indexf2 transform", m);
	
	//index finger top segment transformations
	TransformNode indexf3ToTop = new TransformNode("index to top of hand", Mat4Transform.translate(0,fingerIndex+0.2f,0));
    indexf3Rotate = new TransformNode("index3 rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,fingerIndex,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode indexf3Transform = new TransformNode("indexf3 transform", m);
	
	//thumb bottom segment transformations
	TransformNode thumb1ToTop = new TransformNode("thumb to top of hand", Mat4Transform.translate(handWidth/2f+fingerScale,podiumHeight+handHeight/2f,0));
    thumb1Rotate = new TransformNode("thumb rotate",Mat4Transform.rotateAroundX(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,thumb,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode thumb1Transform = new TransformNode("thumb1 transform", m);
	
	//thumb finger thumb segment transformations
	TransformNode thumb2ToTop = new TransformNode("thumb to top of hand", Mat4Transform.translate(0,thumb+0.2f,0));
    thumb2Rotate = new TransformNode("thumb2 rotate",Mat4Transform.rotateAroundZ(0));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(fingerScale,thumb,fingerScale));
	m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode thumb2Transform = new TransformNode("thumb2 transform", m);
	
	//thumb finger top segment transformations
	TransformNode thumb3ToTop = new TransformNode("thumb to top of hand", Mat4Transform.translate(0,thumb+0.2f,0));
    thumb3Rotate = new TransformNode("thumb3 rotate",Mat4Transform.rotateAroundZ(0));
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
				pinkief1ToTop.addChild(pinkief1Rotate);
				pinkief1Rotate.addChild(pinkief1);
				pinkief1.addChild(pinkief1Transform);
				pinkief1Transform.addChild(pinkieShape1);
				pinkief1.addChild(pinkief2ToTop);
					pinkief2ToTop.addChild(pinkief2Rotate);
					pinkief2Rotate.addChild(pinkief2);
					pinkief2.addChild(pinkief2Transform);
					pinkief2Transform.addChild(pinkieShape2);
					pinkief2.addChild(pinkief3ToTop);
						pinkief3ToTop.addChild(pinkief3Rotate);
						pinkief3Rotate.addChild(pinkief3);
						pinkief3.addChild(pinkief3Transform);
						pinkief3Transform.addChild(pinkieShape3);
			hand.addChild(ringf1ToTop);
				ringf1ToTop.addChild(ringf1Rotate);
				ringf1Rotate.addChild(ringf1);
				ringf1.addChild(ringf1Transform);
				ringf1Transform.addChild(ringShape1);
				ringf1.addChild(ringf2ToTop);
					ringf2ToTop.addChild(ringf2Rotate);
					ringf2Rotate.addChild(ringf2);
					ringf2.addChild(ringf2Transform);
					ringf2Transform.addChild(ringShape2);
					ringf2.addChild(ringf3ToTop);
						ringf3ToTop.addChild(ringf3Rotate);
						ringf3Rotate.addChild(ringf3);
						ringf3.addChild(ringf3Transform);
						ringf3Transform.addChild(ringShape3);
			hand.addChild(middlef1ToTop);
				middlef1ToTop.addChild(middlef1Rotate);
				middlef1Rotate.addChild(middlef1);
				middlef1.addChild(middlef1Transform);
				middlef1Transform.addChild(middleShape1);
				middlef1.addChild(middlef2ToTop);
					middlef2ToTop.addChild(middlef2Rotate);
					middlef2Rotate.addChild(middlef2);
					middlef2.addChild(middlef2Transform);
					middlef2Transform.addChild(middleShape2);
					middlef2.addChild(middlef3ToTop);
						middlef3ToTop.addChild(middlef3Rotate);
						middlef3Rotate.addChild(middlef3);
						middlef3.addChild(middlef3Transform);
						middlef3Transform.addChild(middleShape3);
			hand.addChild(indexf1ToTop);
				indexf1ToTop.addChild(indexf1Rotate);
				indexf1Rotate.addChild(indexf1);
				indexf1.addChild(indexf1Transform);
				indexf1Transform.addChild(indexShape1);
				indexf1.addChild(indexf2ToTop);
					indexf2ToTop.addChild(indexf2Rotate);
					indexf2Rotate.addChild(indexf2);
					indexf2.addChild(indexf2Transform);
					indexf2Transform.addChild(indexShape2);
					indexf2.addChild(indexf3ToTop);
						indexf3ToTop.addChild(indexf3Rotate);
						indexf3Rotate.addChild(indexf3);
						indexf3.addChild(indexf3Transform);
						indexf3Transform.addChild(indexShape3);
			hand.addChild(thumb1ToTop);
				thumb1ToTop.addChild(thumb1Rotate);
				thumb1Rotate.addChild(thumb1);
				thumb1.addChild(thumb1Transform);
				thumb1Transform.addChild(thumbShape1);
				thumb1.addChild(thumb2ToTop);
					thumb2ToTop.addChild(thumb2Rotate);
					thumb2Rotate.addChild(thumb2);
					thumb2.addChild(thumb2Transform);
					thumb2Transform.addChild(thumbShape2);
					thumb2.addChild(thumb3ToTop);
						thumb3ToTop.addChild(thumb3Rotate);
						thumb3Rotate.addChild(thumb3);
						thumb3.addChild(thumb3Transform);
						thumb3Transform.addChild(thumbShape3);
          
    
    exhibit.update();
  }
  
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    updatePerspectiveMatrices();
    
    light.setPosition(0, 20f, 0);
    light.render(gl);
	lampLight.setPosition(-15f, 10f, -15f);
    lampLight.render(gl);
	lampLightb.setPosition(15f, 10f, -15f);
    lampLightb.render(gl);
	lamp.render(gl);
	lamp2.render(gl);
	lampb.render(gl);
	lamp2b.render(gl);
	wallwb.render(gl);
	wallwsl.render(gl);
	wallwsr.render(gl);
	wallwt.render(gl);
	wall2.render(gl);
	wall3.render(gl);
	wall4.render(gl);
    floor.render(gl); 
	ceiling.render(gl);
    
	if (animation) updateHand();
	
    exhibit.draw(gl);
  }
  
  //timer for running through each animation in a loop
  private float timecounter = 0;
  
  private void updateHand(){
	//reset hand first to avoid errors in transformations
	reset();
	double elapsedTime = getSeconds()-startTime-timecounter;
    float rotateAngle = 90f*(float)Math.pow(Math.sin(elapsedTime), 2);
 
	//checks to decide which gesture is being performed
	if (elapsedTime > 12.5){timecounter += 12.5;}
	
	if (elapsedTime < 3.1){
		Vpos(rotateAngle);
	}
    if (elapsedTime > 3.1 && elapsedTime < 6.2){
		Ipos(rotateAngle);
	}
	if (elapsedTime > 6.2 && elapsedTime < 9.35){
		Gpos(rotateAngle);
	}
	if (elapsedTime > 9.35 && elapsedTime < 12.5){
		salute(rotateAngle*0.3f);
	}
  }
  
  
  private void updatePerspectiveMatrices() {
    // needs to be changed if user resizes the window
    perspective = Mat4Transform.perspective(45, aspect);
    light.setPerspective(perspective);
	lampLight.setPerspective(perspective);
	lamp.setPerspective(perspective);
	lamp2.setPerspective(perspective);
	lampLightb.setPerspective(perspective);
	lampb.setPerspective(perspective);
	lamp2b.setPerspective(perspective);
    floor.setPerspective(perspective);
	wallwb.setPerspective(perspective);
	wallwsl.setPerspective(perspective);
	wallwsr.setPerspective(perspective);
	wallwt.setPerspective(perspective);
	wall2.setPerspective(perspective);
	wall3.setPerspective(perspective);
	wall4.setPerspective(perspective);
	ceiling.setPerspective(perspective);
    cube.setPerspective(perspective);
  }
  
  private void disposeMeshes(GL3 gl) {
    light.dispose(gl);
	lampLight.dispose(gl);
	lamp.dispose(gl);
	lamp2.dispose(gl);
	lampLightb.dispose(gl);
	lampb.dispose(gl);
	lamp2b.dispose(gl);
    floor.dispose(gl);
	wallwb.dispose(gl);
	wallwsl.dispose(gl);
	wallwsr.dispose(gl);
	wallwt.dispose(gl);
	wall2.dispose(gl);
	wall3.dispose(gl);
	wall4.dispose(gl);
	ceiling.dispose(gl);
    cube.dispose(gl);
  }
  
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  // private Vec3 getLightPosition() {
    // double elapsedTime = getSeconds()-startTime;
    // float x = 0;
    // float y = 20f;
    // float z = 0;
    // return new Vec3(x,y,z);   
  // }
  
}