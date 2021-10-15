/*
 * Copyright (c) superblaubeere27 2020
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fuck.you.glslmenu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public class GLSLSandboxShader
{
    private int programId;
    private int timeUniform;
    private int mouseUniform;
    private int resolutionUniform;

    public boolean initialized = false;

    public GLSLSandboxShader( String name, InputStream is ) throws IOException
    {
        int program = glCreateProgram( );

        glAttachShader( program, createShader( "/shaders/passthrough.vsh", GLSLSandboxShader.class.getResourceAsStream( "/shaders/passthrough.vsh" ), GL_VERTEX_SHADER ) );
        glAttachShader( program, createShader( name, is, GL_FRAGMENT_SHADER ) );

        glLinkProgram( program );

        int linked = glGetProgrami( program, GL_LINK_STATUS );

        // If linking failed
        if( linked == 0 )
        {
            Main.logger.error( glGetShaderInfoLog( program, glGetProgrami( program, GL_INFO_LOG_LENGTH ) ) );
            return;
        }

        programId = program;

        // Setup uniforms
        glUseProgram( program );

        timeUniform = glGetUniformLocation( program, "time" );
        mouseUniform = glGetUniformLocation( program, "mouse" );
        resolutionUniform = glGetUniformLocation( program, "resolution" );

        glUseProgram( 0 );

        initialized = true;
    }

    public void useShader( int width, int height, float mouseX, float mouseY, float time )
    {
        glUseProgram( programId );

        glUniform2f( resolutionUniform, width, height );
        glUniform2f( mouseUniform, mouseX / width, 1.0f - mouseY / height );
        glUniform1f( timeUniform, time );
    }

    public int createShader( String check, InputStream inputStream, int shaderType ) throws IOException
    {
        int shader = glCreateShader( shaderType );

        glShaderSource( shader, readStreamToString( inputStream ) );

        glCompileShader( shader );

        int compiled = glGetShaderi( shader, GL_COMPILE_STATUS );

        // If compilation failed
        if( compiled == 0 )
        {
            Main.logger.error( glGetShaderInfoLog( shader, glGetShaderi( shader, GL_INFO_LOG_LENGTH ) ) );
            Main.logger.error( "Caused by {}", check );
            return 0;
        }

        return shader;
    }

    public String readStreamToString( InputStream inputStream ) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream( );

        byte[ ] buffer = new byte[ 512 ];

        int read;

        while( ( read = inputStream.read( buffer, 0, buffer.length ) ) != -1 )
            out.write( buffer, 0, read );

        return new String( out.toByteArray( ), StandardCharsets.UTF_8 );
    }
}
