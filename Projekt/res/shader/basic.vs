#version 300

in vec3 position;

out vec3 colour;

void main()
{		
	
	gl_Position = vec4(position, 1);
	colour = vec3(0.5+position.x,1,0.5+position.y);
	
}