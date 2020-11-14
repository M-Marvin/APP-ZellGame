#version 350

in vec3 colour;

void main()
{		
	
	gl_Color = vec4(colour, 1);
	
}