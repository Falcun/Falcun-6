#version 110

uniform vec2 resolution;
const float PI = 3.14159;

vec4 blur(vec2 fragCoord) {
    vec2 uv = fragCoord / iResolution.xy;
    float d = 0.0;
    int n = 18;
    vec3 col = vec3(0.);
    for (int i = 0; i < n; i++) {
        float findex = float(i) / float(n);
        float speed = 1.0 + 1.0 - findex;
        vec2 uv2 = vec2(uv.x, min(1., max(0., uv.y + 0.1 * sin(uv.x * 2. * PI + iTime * speed + findex * 10.1))));
        float offset = 3. * findex * PI + 0.3 * iTime * (0.2 + findex * 0.3);
        float f = 0.5 * (1. + sin(offset));
        float d = abs(f - uv2.y);
        float fcol = pow(1. - pow(d, 0.4), 3.5 + 4. * 0.5 * (1. + sin(iTime * (1. + findex) + 4. * findex * PI)));
        float r = 360. * findex * fcol;
        float b = i % 2 == 1 ? fcol * 0.9 : 0.;
        col += vec3(b, fcol * findex, fcol);
    }
    vec4 returnthing = vec4( col, 1.0 );
    return returnthing;
}

void main() {
    vec2 uv = gl_FragCoord.xy / resolution.xy;
	gl_FragColor = blur(uv);
}
