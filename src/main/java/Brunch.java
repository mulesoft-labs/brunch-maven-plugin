import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.tools.shell.Global;
import org.mule.tools.rhinodo.api.NodeModule;
import org.mule.tools.rhinodo.api.Runnable;
import org.mule.tools.rhinodo.impl.JavascriptRunner;
import org.mule.tools.rhinodo.impl.NodeModuleFactoryImpl;
import org.mule.tools.rhinodo.impl.NodeModuleImpl;
import org.mule.tools.rhinodo.rhino.RhinoHelper;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Brunch implements Runnable {
    public static final String RHINODO_FOLDER = ".rhinodo";
    private Context ctx;
    private Global global;
    private final RhinoHelper rhinoHelper;
    private final JavascriptRunner javascriptRunner;

    public Brunch(){
        this.rhinoHelper = new RhinoHelper();
        String userHome = System.getProperty("user.home");

        File destDir = new File(userHome, RHINODO_FOLDER);
        String destDirString = destDir.toString();
        destDir.mkdirs();

        NodeModule brunch = NodeModuleImpl.fromJar(this.getClass(), "META-INF/brunch", destDirString);

        List<? extends NodeModule> nodeModuleList = Arrays.asList(
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/ansi-color", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/argumentum", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/async", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/brunch", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/bytes", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/chokidar", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/coffee-script", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/commander", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/connect", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/cookie", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/crc", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/date-utils", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/debug", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/diff", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/express", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/fast-list", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/forEachAsync", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/formidable", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/fresh", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/graceful-fs", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/growl", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/handlebars", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/inflection", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/jade", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/methods", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/mime", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/mkdirp", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/mocha", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/ncp", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/optimist", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/pause", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/qs", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/range-parser", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/rimraf", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/send", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/sequence", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/uglify-js", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/walk", destDirString),
                NodeModuleImpl.fromJar(this.getClass(), "META-INF/wordwrap", destDirString),
                brunch);

        javascriptRunner = new JavascriptRunner(new NodeModuleFactoryImpl(nodeModuleList),
                this, destDirString);
    }

    public void run() {
        javascriptRunner.run();
    }

    @Override
    public void executeJavascript(final Context context, final Global global) {
        this.ctx = context;
        this.global = global;
        Function require = (Function)global.get("require", global);
        Object result = require.call(ctx,global,global,new String [] {"brunch"});

//        var src = path.join(path.dirname(fs.realpathSync(__filename)), '..', 'lib');
//        require(path.join(src, 'cli')).run();



    }
}
