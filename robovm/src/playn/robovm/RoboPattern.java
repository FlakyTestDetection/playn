/**
 * Copyright 2014 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playn.robovm;

import org.robovm.apple.coregraphics.CGColor;

import playn.core.gl.AbstractImageGL;
import playn.core.gl.GLPattern;

public class RoboPattern implements GLPattern {

  CGColor colorWithPattern;
  private final AbstractImageGL<?> image;
  private final boolean repeatX, repeatY;

  public RoboPattern(AbstractImageGL<?> image, CGColor colorWithPattern,
                     boolean repeatX, boolean repeatY) {
    this.image = image;
    this.colorWithPattern = colorWithPattern;
    this.repeatX = repeatX;
    this.repeatY = repeatY;
  }

  @Override
  public boolean repeatX() {
    return repeatX;
  }

  @Override
  public boolean repeatY() {
    return repeatY;
  }

  @Override
  public AbstractImageGL<?> image() {
    return image;
  }
}
