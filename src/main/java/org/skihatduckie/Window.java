package org.skihatduckie;

import java.nio.*;
import java.util.ArrayList;

import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    // The window handle
	private long hWindow;

    // Parallel ArrayLists for holding key callback events
    ArrayList<Integer> keys = new ArrayList<Integer>();
    ArrayList<Integer> actions = new ArrayList<Integer>();

    // Create a new GLFW window object.
    Window(int width, int height, boolean visible, boolean resizable) {
        initGlfwLib();

		glfwWindowHint(GLFW_VISIBLE, visible ? 1 : 0);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);

		hWindow = glfwCreateWindow(width, height, "", NULL, NULL);
		if ( hWindow == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
        }
        updateMemoryStack();
    }

    // Return the hande to the window.
    long getHandle() {
        return hWindow;
    }

    // Set the window title.
    void setTitle(String title) {
        glfwSetWindowTitle(hWindow, title);
    }

    // Request for the window to be closed.
    void close() {
        glfwSetWindowShouldClose(hWindow, true);
    }

    // Returns false if no request to close the window has been sent.
    boolean isClosed() {
        return glfwWindowShouldClose(hWindow);
    }

    // Free the window callbacks and destroy the window.
    // Precondition: `Window.close()` has been called.
    void dispose() {
		glfwFreeCallbacks(hWindow);
		glfwDestroyWindow(hWindow);

        // Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
    }

    // Make the OpenGL context current
    void makeContextCurrent() {
        glfwMakeContextCurrent(hWindow);
    }

    // Set the visibilty of the window.
    void setVisibility(boolean visible) {
        if ( visible ) {
            glfwShowWindow(hWindow);
        } else {
            glfwHideWindow(hWindow);
        }
    }

    // Swap the color buffers
    void swapBuffers() {
        glfwSwapBuffers(hWindow);
    }

    // Get the thread stack and push a new frame.
    // The stack frame is popped automatically.
    private void updateMemoryStack() {
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			glfwGetWindowSize(hWindow, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				hWindow,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
    }

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    private void initGlfwLib() {
        // Setup an error callback. The default implementation
	    // will print the error message in System.err.
	    GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
    }
}
