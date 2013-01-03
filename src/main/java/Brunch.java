/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

import org.mozilla.javascript.*;
import org.mule.tools.rhinodo.impl.NodeModuleFactoryImpl;
import org.mule.tools.rhinodo.impl.Rhinodo;
import org.mule.tools.rhinodo.impl.RhinodoBuilder;
import org.mule.tools.rhinodo.impl.console.SystemOutConsole;
import org.mule.tools.rhinodo.impl.console.WrappingConsoleFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Brunch {

    public Brunch(final File rhinodoDestDir, final File userDir, final boolean debug) {
        this(new WrappingConsoleFactory(new SystemOutConsole()), rhinodoDestDir, userDir, debug);
    }

    public Brunch(WrappingConsoleFactory wrappingConsoleFactory,
                  final File rhinodoDestDir, final File userDir, final boolean debug) {
        this(Rhinodo.create(), wrappingConsoleFactory, rhinodoDestDir, userDir, debug);
    }

    public Brunch(RhinodoBuilder rhinodoBuilder, WrappingConsoleFactory wrappingConsoleFactory,
                  final File rhinodoDestDir, final File userDir, final boolean debug) {

        String destDirString = rhinodoDestDir.toString();
        rhinodoDestDir.mkdirs();

        Map<String,String> env = new HashMap<String, String>(System.getenv());

        if ( debug ) {
            env.put("BRUNCH_DEBUG", "*");
        }

        rhinodoBuilder.destDir(rhinodoDestDir).moduleFactory(new NodeModuleFactoryImpl(
                this.getClass(), destDirString, "ansi-color", "argumentum", "async", "brunch",
                "bytes", "chokidar", "coffee-script", "commander", "connect", "cookie", "crc", "date-utils",
                "debug", "diff", "express", "fast-list", "forEachAsync", "formidable", "fresh", "graceful-fs",
                "growl", "handlebars", "inflection", "jade", "methods", "mime", "mkdirp", "mocha", "ncp",
                "optimist", "pause", "qs", "range-parser", "rimraf", "send", "sequence", "uglify-js", "walk",
                "wordwrap"))
                .consoleFactory(wrappingConsoleFactory)
                .env(env)
                .build(new BaseFunction() {
                    @Override
                    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
//                        ScriptableObject.callMethod(scope, "strikeThePose", new Object[0]);
                        Scriptable brunch = (Scriptable) ScriptableObject.callMethod(cx, scope, "require",
                                new Object[]{Context.javaToJS("brunch", scope)});

                        Scriptable options = cx.newObject(scope);
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
