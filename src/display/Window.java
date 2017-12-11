package display;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Window
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static final String TITLE = "LWJGL3 2D Game";
	
	public static final int ORTHO_LEFT = -WIDTH / 2;
	public static final int ORTHO_RIGHT = WIDTH / 2;
	public static final int ORTHO_BOTTOM = -HEIGHT / 2;
	public static final int ORTHO_TOP = HEIGHT / 2;
	
	private int width = WIDTH;
	private int height = HEIGHT;
	
	private long window;
	
	public Window()
	{
		
	}
	
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public long getWindow()
	{
		return window;
	}
	
	public void createDisplay()
	{
		if(!glfwInit())
		{
			throw new IllegalStateException("ERROR: no se pudo iniciar GLFW");
		}
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		window = glfwCreateWindow(width, height, TITLE, 0, 0);
		//FullScreen Mode
//		long window = GLFW.glfwCreateWindow(800, 600, "LWJGL3 2D Game", glfwGetPrimaryMonitor(), 0);
		
		if(window == 0)
		{
			throw new IllegalStateException("ERROR: no se pudo crear la ventana");
		}
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() -800) / 2, (videoMode.height() - 600) /2);
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
		
		GL.createCapabilities();
		
		glEnable(GL_TEXTURE_2D);
	}
	
	public void swapBuffers()
	{
		glfwSwapBuffers(window);
	}
	
	public boolean shouldClose()
	{
		return glfwWindowShouldClose(window);
	}
}
