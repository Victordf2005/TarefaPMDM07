#ifdef GL_ES
precision mediump float;
#endif
varying vec2 v_textCoord;
uniform sampler2D u_texture;
void main()
{
    vec4 texColor = texture2D(u_texture, v_textCoord);
    gl_FragColor = texColor;
}