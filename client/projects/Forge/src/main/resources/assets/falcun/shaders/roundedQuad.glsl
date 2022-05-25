#version 110

uniform vec4 color;
uniform float curve;
uniform vec4 area;
uniform float feather;

void main() {
    vec2 pos = gl_FragCoord.xy;
    float left = area.x;
    float right = area.y;
    float bottom = area.z;
    float top = area.w;
    if (pos.x > left + curve && pos.x < right - curve && pos.y < top - curve && pos.y > bottom + curve) {
        gl_FragColor = color;
    } else {
        if (pos.x < left + curve) {
            if (pos.y < bottom + curve) {
                vec2 distance = vec2(gl_FragCoord.x - (left + curve), gl_FragCoord.y - (bottom + curve));
                float step = 1.0 - smoothstep(curve-feather, curve+feather, length(distance));

                gl_FragColor = vec4(color.rgb, color.a * step);
            } else if (pos.y > top - curve) {
                vec2 distance = vec2(gl_FragCoord.x - (left + curve), gl_FragCoord.y - (top - curve));
                float step = 1.0 - smoothstep(curve-feather, curve+feather, length(distance));
                gl_FragColor = vec4(color.rgb, color.a * step);
            } else {
                gl_FragColor = color;
            }
        } else if (pos.x > right - curve) {
            if (pos.y < bottom + curve) {
                vec2 distance = vec2(gl_FragCoord.x - (right - curve), gl_FragCoord.y - (bottom + curve));
                float step = 1.0 - smoothstep(curve-feather, curve+feather, length(distance));
                gl_FragColor = vec4(color.rgb, color.a * step);
            } else if (pos.y > top - curve) {
                vec2 distance = vec2(gl_FragCoord.x - (right - curve), gl_FragCoord.y - (top - curve));
                float step = 1.0 - smoothstep(curve-feather, curve+feather, length(distance));
                gl_FragColor = vec4(color.rgb, color.a * step);
            } else {
                gl_FragColor = color;
            }
        } else {
            gl_FragColor = color;
        }
    }
}