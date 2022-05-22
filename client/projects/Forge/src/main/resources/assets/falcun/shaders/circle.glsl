#version 110

uniform vec2 center;
uniform float radius;
uniform float feather;
uniform vec4 color;

void main(){
    vec2 distance = gl_FragCoord.xy - center.xy;
    float step = 1.0 - smoothstep(radius - feather, radius + feather, length(distance));
    gl_FragColor =  vec4(color.rgb, color.a * step);
}