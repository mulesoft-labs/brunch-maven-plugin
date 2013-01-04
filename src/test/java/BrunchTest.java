/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mozilla.javascript.Function;
import org.mule.tools.rhinodo.api.ConsoleProvider;
import org.mule.tools.rhinodo.api.NodeModuleProvider;
import org.mule.tools.rhinodo.impl.Rhinodo;
import org.mule.tools.rhinodo.impl.RhinodoBuilder;
import org.mule.tools.rhinodo.impl.console.SystemOutConsole;
import org.mule.tools.rhinodo.impl.console.WrappingConsoleProvider;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BrunchTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testRun() throws Exception {
        File rhinodoDestDir = folder.newFolder(".rhinodo");
        File userDir = folder.newFolder("userDir");
        RhinodoBuilder rhinodoBuilder = mock(RhinodoBuilder.class);
        Rhinodo rhinodo = mock(Rhinodo.class);

        when(rhinodoBuilder.destDir(any(File.class))).thenReturn(rhinodoBuilder);
        when(rhinodoBuilder.moduleFactory(any(NodeModuleProvider.class))).thenReturn(rhinodoBuilder);
        when(rhinodoBuilder.consoleFactory(any(ConsoleProvider.class))).thenReturn(rhinodoBuilder);
        when(rhinodoBuilder.env(any(Map.class))).thenReturn(rhinodoBuilder);
        when(rhinodoBuilder.build(any(Function.class))).thenReturn(rhinodo);

        WrappingConsoleProvider wrappingConsoleFactory = new WrappingConsoleProvider(new SystemOutConsole());
        new Brunch(rhinodoBuilder, wrappingConsoleFactory,rhinodoDestDir, userDir, true);

        verify(rhinodoBuilder, times(1)).destDir(rhinodoDestDir);
        verify(rhinodoBuilder, times(1)).consoleFactory(wrappingConsoleFactory);
        verify(rhinodoBuilder, times(1)).build(any(Function.class));
        verify(rhinodoBuilder, times(1)).env(any(Map.class));
        verify(rhinodoBuilder, times(1)).moduleFactory(any(NodeModuleProvider.class));
        verifyNoMoreInteractions(rhinodoBuilder);
    }

    @Test
    @Ignore
    public void testITBrunchRun() throws Exception {
        String userHome = System.getProperty("user.home");
        File rhinodoDestDir = new File(userHome, ".rhinodo");
        File userDir = new File(System.getenv().get("PATH_TO_WORKSPACE"));

        new Brunch(rhinodoDestDir, userDir, true);

    }
}
