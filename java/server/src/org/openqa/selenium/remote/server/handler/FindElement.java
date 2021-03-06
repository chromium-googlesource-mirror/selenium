/*
Copyright 2007-2009 WebDriver committers
Copyright 2007-2009 Google Inc.

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

package org.openqa.selenium.remote.server.handler;

import static java.util.logging.Level.SEVERE;

import com.google.common.collect.ImmutableMap;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.rest.ResultType;

import java.util.Map;
import java.util.logging.Logger;

public class FindElement extends ResponseAwareWebDriverHandler implements JsonParametersAware {
  private static Logger log = Logger.getLogger(FindElement.class.getName());
  private volatile By by;

  public FindElement(Session session) {
    super(session);
  }

  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    by = newBySelector().pickFromJsonParameters(allParameters);
  }

  public ResultType call() throws Exception {
    try {
      WebElement element = getDriver().findElement(by);
      String elementId = getKnownElements().add(element);
      response.setValue(ImmutableMap.of("ELEMENT", elementId));

      return ResultType.SUCCESS;
    } catch (RuntimeException e) {
      // Add logging to detect when issue #1800 occurs
      if (!(e instanceof NoSuchElementException)) {
        log.log(SEVERE, "Unexpected exception during findElement", e);
      }
      throw e;
    }
  }

  @Override
  public String toString() {
    return String.format("[find element: %s]", by);
  }

}
