package com.will;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
//
//	private void searchForDirectories(List<String> paths, String directory) {
//
//        final Enumeration<String> entryenum = frag.getEntryPaths("/");
//        while (entryenum.hasMoreElements()) {
//        	final String path = entryenum.nextElement();
//        	if (path.endsWith("/")) {
//        		allPaths.add(path);
//        	}
////            System.out.println(path);
//        }
//	}

    public Object start(final IApplicationContext context) throws Exception {

        final Bundle real = Platform.getBundle("com.will");
        final Bundle frag = Platform.getBundle("com.will.nl.en");
        final Bundle fake = Platform.getBundle("fake");

        final Charset charset = Charset.forName("UTF_16BE");

        final Bundle[] realsFrags = Platform.getFragments(real);
        final Bundle enFrag = realsFrags[0];
        final URL propsFile = enFrag.getEntry("nl/en/US/common.properties");
        final InputStream is = propsFile.openStream();
        final Reader reader = new InputStreamReader(is, charset);
        final Properties props = new Properties();
        props.load(reader);
        is.close();

//        for (final Object keyO : props.keySet()) {
//            final String keyStr = (String) keyO;
//            final String value = props.getProperty(keyStr);
//            System.out.println(keyStr + "  " + value);
//        }
//        final String key = "say-hello";
//        System.out.println("Property from common.properties: " + props.getProperty(key));

//        final Location loc = Platform.getInstallLocation();
//        final URL installUrl = loc.getURL();
//        System.out.println("Platform install location: " + installUrl.getFile());
//        System.out.println("Base bundle location: " + real.getLocation());

//        final Enumeration<URL> entryenum = enFrag.findEntries("/nl", "*", true);

      final Enumeration<URL> entryenum = real.findEntries("/nl", "*", true);
        while (entryenum.hasMoreElements()) {
        	final URL path = entryenum.nextElement();
        	System.out.println(path.getFile());
        	
        }
        
//      final Enumeration<URL> entryenum = real.findEntries("nl", "", true);
//        final List<String> allPaths = new ArrayList<String>();
//        final Enumeration<String> entryenum = frag.getEntryPaths("/nl");
//        while (entryenum.hasMoreElements()) {
//        	final String path = entryenum.nextElement();
//        	if (path.endsWith("/")) {
//        		allPaths.add(path);
//        	}
////            System.out.println(path);
//        }
//        for (final String path : allPaths) {
//        	System.out.println(path);
//        }
        
        
//        final Enumeration<URL> entryenum = real.findEntries("nl", "", true);
//        while (entryenum.hasMoreElements()) {
//            final URL fileUrl = entryenum.nextElement();
//            final URI uri = fileUrl.toURI();
//            System.out.println(fileUrl + " uri = " + uri);
//            // error here!
//            final File bundleFile = new File(uri);
//            System.out.println(bundleFile.getAbsolutePath());
//        }

//        final Enumeration<URL> entryenum = real.findEntries("nl", "", true);
//        while (entryenum.hasMoreElements()) {
//            final URL bundleProtoUrl = entryenum.nextElement();
//            final URL fileProtoUrl = FileLocator.toFileURL(bundleProtoUrl);
//            final URI uri = fileProtoUrl.toURI();
//            System.out.println(bundleProtoUrl + " uri = " + uri);
//            // error here!
//            final File bundleFile = new File(uri);
//            System.out.println(bundleFile.getAbsolutePath());
//        }

//        final Enumeration<String> pathsEnum = enFrag.getEntryPaths("nl");
//        while (pathsEnum.hasMoreElements()) {
//            final String ffileUrl = pathsEnum.nextElement();
//            System.out.println(ffileUrl);
//        }

//        IPath path = new Path("$nl$/plugin.properties");
//        FileLocator.find(real, path);


        // Basic charset
//        IPath path = new Path("$nl$/plugin-ascii.properties");
//        URL fileUrl = FileLocator.find(real, path, null);
//        is = fileUrl.openStream();
//        props.load(is);
//        is.close();
//        key = "plugin-ascii";
//        System.out.println(fileUrl.toString());
//        System.out.println("Property from plugin-ascii.properties: " + props.getProperty(key));


        // Complex charset
//        path = new Path("$nl$/plugin-utf16be.properties");
////        URL fileUrl = FileLocator.find(real, path, null);
//        fileUrl = FileLocator.find(real, path, null);
//        is = fileUrl.openStream();
////        System.out.println(fileUrl.toString());
//        reader = new InputStreamReader(is, charset);
//        props.load(reader);
//        is.close();
//        key = "plugin-utf16be.arabic";
//        System.out.println("Property from plugin-utf16be.properties, Arabic: " + props.getProperty(key));
//        key = "plugin-utf16be.chinese";
//        System.out.println("Property from plugin-utf16be.properties, Chinese: " + props.getProperty(key));


//        System.out.println("NL is " + Platform.getNL());
//        final String productName = Platform.getResourceString(real, "%product-name");
//        System.out.println("Product name: " + productName);
//        final String pluginName = Platform.getResourceString(real, "%plugin-name");
//        System.out.println("Plugin name: " + pluginName);
//        final String managestStr = Platform.getResourceString(real, "%manafest-str");
//        System.out.println("Manafest name: " + managestStr);

        return IApplication.EXIT_OK;
    }


    public void stop() {

    }
}