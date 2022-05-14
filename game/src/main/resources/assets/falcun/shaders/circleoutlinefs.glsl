#version 110

uniform vec2 center;
uniform float radius;
uniform float feather;
uniform vec4 color;
uniform float lineWidth;

void main() {
    vec2 distance = gl_FragCoord.xy - center.xy;
    float step = smoothstep(radius + feather, radius - feather, length(distance));
    if (step > 0.95) {
        step = 1.0 - smoothstep(radius - lineWidth + feather, radius - lineWidth - feather, length(distance));
        gl_FragColor = vec4(color.rgb, color.a * step);
    } else {
        gl_FragColor = vec4(color.rgb, color.a * step);
    }
}
