import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Room implements GLEventListener {

  private Mesh floor, wall1, wall2, wall3, wall4, lamp, lamp2, ceiling, cube;

  public init(GL3 gl){
	  
	int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/metal_specular.jpg");
	
	lamp = new Cube(gl, textureId3, textureId4);
	
	Mat4 n = Mat4Transform.scale(1.5f,9,1.5f);
    n = Mat4.multiply(n, Mat4Transform.translate(-1,0.5f,-1));
	lamp.setModelMatrix(n);
	
	lamp.render(gl);
	
  }
}