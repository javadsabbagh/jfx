/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.glass.ui.monocle;

import com.sun.glass.events.WindowEvent;

import java.util.Arrays;

public final class MonocleWindowManager {

    static private MonocleWindowManager instance = new MonocleWindowManager();

    private MonocleWindow[] windows = new MonocleWindow[0];
    private int nextID = 1;

    private MonocleWindow focusedWindow = null;

    private MonocleWindowManager() {
        //singleton
    }

    public static MonocleWindowManager getInstance() {
        return instance;
    }

    private int getWindowIndex(MonocleWindow window) {
        for (int i = 0; i < windows.length; i++) {
            // Any two MonocleWindow objects represent different windows, so
            // equality can be determined by reference comparison.
            if (windows[i] == window) {
                return i;
            }
        }
        return -1;
    }
    public void toBack(MonocleWindow window) {
        int index = getWindowIndex(window);
        if (index != 0 && index != -1) {
            System.arraycopy(windows, 0, windows, 1, index);
            windows[0] = window;
        }
    }

    public void toFront(MonocleWindow window) {
        int index = getWindowIndex(window);
        if (index != windows.length - 1 && index != -1) {
            System.arraycopy(windows, index + 1, windows, index,
                             windows.length - index - 1);
            windows[windows.length - 1] = window;
        }
    }

    public int addWindow(MonocleWindow window) {
        int index = getWindowIndex(window);
        if (index == -1) {
            windows = Arrays.copyOf(windows, windows.length + 1);
            windows[windows.length - 1] = window;
        }
        return nextID++;

    }

    public boolean closeWindow(MonocleWindow window) {
        int index = getWindowIndex(window);
        if (index != -1) {
            System.arraycopy(windows, index + 1, windows, index,
                             windows.length - index - 1);
            windows = Arrays.copyOf(windows, windows.length - 1);
        }
        return true;

    }

    public boolean minimizeWindow(MonocleWindow window) {
        return true;
    }

    public boolean maximizeWindow(MonocleWindow window) {
        return true;
    }

    public boolean requestFocus(MonocleWindow window) {
        int index = getWindowIndex(window);
        if (index != -1) {
            focusedWindow = window;
            window._notifyFocus(WindowEvent.FOCUS_GAINED);
            return true;
        } else {
            return false;
        }
    }

    public boolean grabFocus(MonocleWindow window) {
        return true;
    }

    public void ungrabFocus(MonocleWindow window) {

    }

    public MonocleWindow getWindowForLocation(int x, int y) {
        for (int i = 0; i < windows.length; i++) {
            MonocleWindow w = windows[i];
            if (x >= w.getX() && y >= w.getY()
                   && x < w.getX() + w.getWidth()
                   && y < w.getY() + w.getHeight()) {
                return w;
            }
        }
        return null;
    }
    
    public MonocleWindow getFocusedWindow() {
        return focusedWindow;
    }

}