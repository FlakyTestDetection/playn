/**
 * Copyright 2011 The PlayN Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import playn.core.Platform;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameViewGL extends GLSurfaceView implements SurfaceHolder.Callback {

  public final AndroidGL20 gl20;
  private final SurfaceHolder holder;
  private GameLoop loop;
  private final GameActivity activity;
  private boolean gameInitialized = false;
  private boolean gameSizeSet = false; // Set by AndroidGraphics

  public GameViewGL(AndroidGL20 _gl20, GameActivity activity, Context context) {
    super(context);
    this.gl20 = _gl20;
    this.activity = activity;
    holder = getHolder();
    holder.addCallback(this);
    setFocusable(true);
    setEGLContextClientVersion(2);
    this.setRenderer(new AndroidRendererGL());
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // Default to filling all the available space when the game is first loads
    Platform platform = activity.platform();
    if (platform != null && gameSizeSet) {
      int width = platform.graphics().width();
      int height = platform.graphics().height();
      if (width == 0 || height == 0) {
        Log.e("playn", "Invalid game size set: (" + width + " , " + height + ")");
      } else {
        int minWidth = getSuggestedMinimumWidth();
        int minHeight = getSuggestedMinimumHeight();
        width = width > minWidth ? width : minWidth;
        height = height > minHeight ? height : minHeight;
        setMeasuredDimension(width, height);
        Log.i("playn", "Using game-specified sizing. (" + width + " , " + height + ")");
        return;
      }
    }

    Log.i("playn", "Using default sizing.");
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public void notifyVisibilityChanged(int visibility) {
    Log.i("playn", "notifyVisibilityChanged: " + visibility);
    if (visibility == INVISIBLE) {
      if (loop != null)
        loop.pause();
      onPause();
    } else {
      if (loop != null)
        loop.start();
      onResume();
    }
  }

  void gameSizeSet() {
    gameSizeSet = true;
  }

  private class AndroidRendererGL implements Renderer {
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
      gl20.glViewport(0, 0, width, height);
      Log.i("playn", "Surface dimensions changed to ( " + width + " , " + height + ")");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
      // Wait until onDrawFrame to make sure all the metrics
      // are in place at this point.
      if (!gameInitialized) {
        AndroidPlatform.register(gl20, activity);
        activity.main();
        loop = new GameLoop();
        loop.start();
        gameInitialized = true;
      }
      // Handle updating, clearing the screen, and drawing
      if (loop.running() && gameInitialized)
        loop.run();
    }
  }
}
