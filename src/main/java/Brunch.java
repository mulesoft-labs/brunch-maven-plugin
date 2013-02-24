/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

import org.mozilla.javascript.*;
import org.mule.tools.rhinodo.impl.NodeModuleProviderImpl;
import org.mule.tools.rhinodo.impl.Rhinodo;
import org.mule.tools.rhinodo.impl.RhinodoBuilder;
import org.mule.tools.rhinodo.impl.console.SystemOutConsole;
import org.mule.tools.rhinodo.impl.console.WrappingConsoleProvider;

import java.io.File;
import java.util.Map;

public class Brunch {

    public Brunch(final File rhinodoDestDir, final File userDir) {
        this(new WrappingConsoleProvider(new SystemOutConsole()), rhinodoDestDir, userDir, System.getenv(), false);
    }

    public Brunch(WrappingConsoleProvider wrappingConsoleFactory,
                  final File rhinodoDestDir, final File userDir, final Map<String,String> env, final boolean minify) {
        this(Rhinodo.create(), wrappingConsoleFactory, rhinodoDestDir, userDir, env, minify);
    }

    public Brunch(RhinodoBuilder rhinodoBuilder, WrappingConsoleProvider wrappingConsoleFactory,
                  final File rhinodoDestDir, final File userDir, final Map<String,String> env, final boolean minify) {

        String destDirString = rhinodoDestDir.toString();
        rhinodoDestDir.mkdirs();

        env.put("RHINODO_IGNORE_NOT_IMPLEMENTED_EXTENSIONS", "true");

        if ( !env.containsKey("BRUNCH_DEBUG") ) {
            env.put("BRUNCH_DEBUG", "*");
        }

        NodeModuleProviderImpl nodeModuleProvider;
        try {
            nodeModuleProvider = NodeModuleProviderImpl.fromJar(this.getClass(), destDirString);
        } catch (Exception e) {
            nodeModuleProvider = new NodeModuleProviderImpl(this.getClass(), destDirString);
        }

        rhinodoBuilder
                .destDir(rhinodoDestDir)
                .moduleFactory(nodeModuleProvider)
                .consoleFactory(wrappingConsoleFactory)
                .env(env)
                .build(new BaseFunction() {
                    @Override
                    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                        Scriptable brunch = (Scriptable) ScriptableObject.callMethod(cx, scope, "require",
                                new Object[]{Context.javaToJS("brunch", scope)});

                        Scriptable options = cx.newObject(scope);
                        ScriptableObject.putProperty(options, "minify", minify);
                        System.setProperty("user.dir", userDir.getAbsolutePath());

                        ScriptRuntime.doTopCall(ScriptableObject.getTypedProperty(brunch, "build", Function.class),
                                cx, scope, thisObj, new Object[]{
                                options,
                                new BaseFunction() {
                                    @Override
                                    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                                        return Undefined.instance;
                                    }
                                }
                        });

                        return Undefined.instance;
                    }
                });

    }

}
