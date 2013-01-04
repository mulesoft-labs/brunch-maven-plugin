/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.mule.tools.rhinodo.impl.console.WrappingConsoleProvider;
import org.mule.tools.rhinodo.maven.MavenConsole;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Goal that offers Brunch support in Maven builds.
 *
 * @goal build
 * @phase compile
 */
public class BuildMojo extends AbstractJavascriptMojo {

    /**
     * The directory where the Brunch build should take place.
     *
     * @parameter expression="${brunch.sourceDirectory}"
     */
    protected File sourceDirectory;

    /**
     * Whether the minify should take place or not.
     *
     * @parameter expression="${brunch.minify}"
     */
    protected boolean minify;

    /**
     * Additional Environment variables to send to Brunch.
     *
     * @parameter expression="${brunch.env}"
     */
    protected Map env;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();

        sourceDirectory = sourceDirectory.getAbsoluteFile();

        if ( !(sourceDirectory.exists() && sourceDirectory.isDirectory()) ) {
            log.warn(String.format("Not executing brunch as source directory [%s] does not exist.", sourceDirectory));
            return;
        }

        Map<String,String> myEnv = new HashMap<String, String>(System.getenv());
        myEnv.putAll(env);

        new Brunch(new WrappingConsoleProvider(MavenConsole.fromMavenLog(log)), getJavascriptFilesDirectory(),
                sourceDirectory, myEnv, minify);

    }
}
