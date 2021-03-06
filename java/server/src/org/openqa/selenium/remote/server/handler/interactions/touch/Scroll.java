/*
Copyright 2011 WebDriver committers
Copyright 2011 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.openqa.selenium.remote.server.handler.interactions.touch;

import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.HasTouchScreen;
import org.openqa.selenium.TouchScreen;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebElementHandler;
import org.openqa.selenium.remote.server.rest.ResultType;

import java.util.Map;

public class Scroll extends WebElementHandler implements JsonParametersAware {

  private static final String ELEMENT = "element";
  private static final String XOFFSET = "xoffset";
  private static final String YOFFSET = "yoffset";
  String elementId;
  int xOffset;
  int yOffset;

  public Scroll(Session session) {
    super(session);
  }

  public ResultType call() throws Exception {
    TouchScreen touchScreen = ((HasTouchScreen) getDriver()).getTouch();
    WebElement element = getKnownElements().get(elementId);
    Coordinates elementLocation = ((Locatable) element).getCoordinates();

    touchScreen.scroll(elementLocation, xOffset, yOffset);

    return ResultType.SUCCESS;
  }

  @Override
  public String toString() {
    return String.format("[scroll: %s]", elementId);
  }

  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    elementId = (String) allParameters.get(ELEMENT);
    xOffset = ((Long) allParameters.get(XOFFSET)).intValue();
    yOffset = ((Long) allParameters.get(YOFFSET)).intValue();
  }

}
