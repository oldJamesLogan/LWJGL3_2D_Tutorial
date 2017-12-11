package shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader
{
	public static final int SHADER_VERTICES_ATTRIB = 0;
	public static final int SHADER_TEX_COORDS_ATTRIB = 1;
	
	private int program;
	private int vs;
	private int fs;
	
	public Shader(String fileName)
	{
		program = glCreateProgram();
		
		vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, readFile(fileName + ".vs"));
		glCompileShader(vs);
		if(glGetShaderi(vs, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetShaderInfoLog(vs));
			System.exit(1);
		}
		
		fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, readFile(fileName + ".fs"));
		glCompileShader(fs);
		if(glGetShaderi(fs, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetShaderInfoLog(fs));
			System.exit(1);
		}
		
		glAttachShader(program, vs);
		glAttachShader(program, fs);
		
		glBindAttribLocation(program, SHADER_VERTICES_ATTRIB, "vertices");
		glBindAttribLocation(program, SHADER_TEX_COORDS_ATTRIB, "texture");
		
		glLinkProgram(program);
		if(glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE)
		{
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
		glValidateProgram(program);
		if(glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
	}
	
	public void setUniform(String name, int value)
	{
		int location = glGetUniformLocation(program, name);
		
		if(location != -1)
		{
			glUniform1i(location, value);
		}
	}
	
	public void setUniform(String name, float value)
	{
		int location = glGetUniformLocation(program, name);
		
		if(location != -1)
		{
			glUniform1f(location, value);
		}
	}
	
	public void setUniform(String name, Matrix4f value)
	{
		int location = glGetUniformLocation(program, name);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		
		value.get(buffer);
		
		if(location != -1)
		{
			glUniformMatrix4fv(location, false, buffer);
		}
	}
	
	public void bind()
	{
		glUseProgram(program);
	}
	
	private String readFile(String fileName)
	{
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		
		try
		{
			br = new BufferedReader(new FileReader( new File("./shaders/" + fileName)));
			String line;
			while((line = br.readLine()) != null)
			{
				sb.append(line);
				sb.append("\n");
			}
			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return sb.toString() ;
	}
}
