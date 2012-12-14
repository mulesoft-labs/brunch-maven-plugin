import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.shell.Global;
import org.mule.tools.rhinodo.api.NodeModule;
import org.mule.tools.rhinodo.api.Runnable;
import org.mule.tools.rhinodo.impl.JavascriptRunner;
import org.mule.tools.rhinodo.impl.NodeModuleFactoryImpl;
import org.mule.tools.rhinodo.impl.NodeModuleImpl;
import org.mule.tools.rhinodo.impl.NodeModuleImplBuilder;
import org.mule.tools.rhinodo.rhino.RhinoHelper;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Brunch implements Runnable {
    public static final String RHINODO_FOLDER = ".rhinodo";
    private Context ctx;
    private Global global;
    private final RhinoHelper rhinoHelper;
    private final JavascriptRunner javascriptRunner;
    private final NodeModuleImpl mime;

    public Brunch() {

        this.rhinoHelper = new RhinoHelper();
        String userHome = System.getProperty("user.home");

        File destDir = new File(userHome, RHINODO_FOLDER);
        String destDirString = destDir.toString();
        destDir.mkdirs();

        NodeModule brunch = NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/brunch", destDirString);
        mime = NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/mime", destDirString);

        List<? extends NodeModule> nodeModuleList = Arrays.asList(
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/ansi-color", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/argumentum", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/async", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/brunch", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/bytes", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/chokidar", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/clean-css", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/coffee-script", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/commander", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/connect", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/cookie", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/crc", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/date-utils", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/debug", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/diff", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/express", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/fast-list", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/forEachAsync", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/formidable", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/fresh", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/graceful-fs", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/growl", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/handlebars", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/inflection", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/jade", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/methods", destDirString),
                mime,
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/mkdirp", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/mocha", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/less", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/ncp", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/optimist", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/pause", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/qs", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/range-parser", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/rimraf", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/send", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/sequence", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/uglify-js", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/walk", destDirString),
                NodeModuleImplBuilder.fromJarOrFile(this.getClass(), "META-INF/node_modules/wordwrap", destDirString),
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
        Scriptable result = (Scriptable) require.call(ctx, global, global, new String[]{"brunch"});
        BoundFunction build = (BoundFunction) Context.jsToJava(result.get("build", result), Function.class);
        Map<String, String> options = new HashMap<String,String>();
        options.put("mimify", "true");
        System.setProperty("user.dir","/Users/apose/Documents/habitat/web-ui/habitat-ui-ember");
        Object object = ScriptRuntime.doTopCall(build, ctx, global, global,
                new Object[]{ new RhinoHelper().mapToNativeObject(options), new BaseFunction(){
                    @Override
                    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
//                        System.out.println(args);
                        return Undefined.instance;
                    }

                    @Override
                    public Object getDefaultValue(Class<?> typeHint) {
                        return "Callback function of brunch build";
                    }
                }});
    }
}
