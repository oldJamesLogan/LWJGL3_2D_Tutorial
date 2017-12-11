package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import display.Window;
import models.Model;
import models.Texture;
import render.Camera;
import shaders.Shader;

public class Main
{
	/*
	 * ver capitulo 12 12:08
	 */
	
	public static final float SPEED = 5f;
	
	private static float x, y;
	private static double frame_time = 0;
	private static int frames = 0;
	
	//OBJETOS
	private static Window window;
	private static Camera camera;
	
	private static Shader shader;
	private static Matrix4f scale;
	private static Matrix4f target;
	
	private static Model model;
	private static Texture texture;
	
	private static float[] vertices;
	private static float[] tex_coords;
	private static int[] indices;
	
	public static void main(String[] args)
	{
		init();
		
		double frame_cap = 1.0/60.0;
		double time = Timer.getTime();
		double delta = 0;
		
		while(!window.shouldClose())
		{
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			delta += passed;
			frame_time += passed;
			
			time = time_2;
			
			while(delta >= frame_cap)
			{
				delta -= frame_cap;
				update();
				
				render();
				
				if(frame_time >= 1)
				{
					frame_time = 0;
					System.out.println(frames);
					frames = 0;
				}
			}
			
			
		}
		
		glfwTerminate();
	}
	
	
	
	private static void init()
	{
		window = new Window();
//		window.setSize(100, 100);
		window.createDisplay();
		
		x = 0;
		y = 0;
		
		texture = new Texture("./res/char.png");
		shader = new Shader("shader");
		camera = new Camera();
		
		vertices = new float[]
		{
				-1f,  1f,  0,	//	TOP LEFT	0
				 1f,  1f,  0,	//	TOP RIGHT	1
				 1f, -1f,  0,	//	BOTOM RIGHT	2
				-1f, -1f,  0, 	//	BOTOM LEFT	3
				 
		};
		
		tex_coords = new float[]
		{
				0, 0,	//	0
				1, 0,	//	1
				1, 1,	//	2
				0, 1	//	3
		};
		
		indices = new int[]
		{
				0, 1, 2,
				2, 3, 0
		};
		
		model = new Model(vertices, tex_coords, indices);
		
		scale = new Matrix4f().translate(new Vector3f(0,0,0)).scale(32);
		target = new Matrix4f();
	}

	private static void update()
	{
		frames++;
		glfwPollEvents();
		
		if(glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) == GL_TRUE){glfwSetWindowShouldClose(window.getWindow(), true);}
		
		if(glfwGetKey(window.getWindow(), GLFW_KEY_A) == GL_TRUE){x -= SPEED;}
		if(glfwGetKey(window.getWindow(), GLFW_KEY_D) == GL_TRUE){x += SPEED;}
		if(glfwGetKey(window.getWindow(), GLFW_KEY_W) == GL_TRUE){y += SPEED;}
		if(glfwGetKey(window.getWindow(), GLFW_KEY_S) == GL_TRUE){y -= SPEED;}
		
		target = scale;
		
		camera.setPosition(new Vector3f(x, y, 0));
	}
	
	private static void render()
	{
		glClear(GL_COLOR_BUFFER_BIT);
		
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", camera.getProjection().mul(target));
		texture.bind(0);
		model.render();
		
		window.swapBuffers();
	}
}
