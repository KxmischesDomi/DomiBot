package me.kxmischesdomi.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.net.URL;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ConfigLoader {

	private final String
			configName = "config.yml",
			devConfigName = "dev-config.yml"; // TODO: LOAD FROM ENVIRONMENT VARIABLES

	private boolean devEnvironment = false;

	public Config loadConfigConfiguration() throws Exception {
		File configFile = loadConfigFile();

		Constructor constructor = new Constructor(Config.class);
		Yaml yaml = new Yaml(constructor);

		FileInputStream io = new FileInputStream(configFile);
		Config config = yaml.load(io);
		config.setDev(devEnvironment);
		return config;
	}

	public File loadConfigFile() throws Exception {

		File config = getFile(devConfigName);
		if (!config.exists()) {
			config = getFile(configName);
		}

		if (!config.exists()) {
			if (!devEnvironment) {
				System.out.println("No config file found. Creating default one.");
				exportResource("/" + configName);
			} else {
				throw new FileNotFoundException("No config file found. (config.yml, dev-config.yml)");
			}
		}

		return config;
	}

	public File getFile(String file) {
		String name = "./" + file;
		URL resource = getClass().getClassLoader().getResource(name);
		if (resource != null) {
			// dev environment
			devEnvironment = true;
			return new File(resource.getPath());
		} else {
			// user environment
			return new File(file);
		}
	}
	private static String exportResource(String resourceName) throws Exception {
		InputStream stream = null;
		OutputStream resStreamOut = null;
		String jarFolder;
		try {
			stream = ConfigLoader.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
			if(stream == null) {
				throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
			}

			int readBytes;
			byte[] buffer = new byte[4096];
			jarFolder = new File(ConfigLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
			resStreamOut = new FileOutputStream(jarFolder + resourceName);
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			stream.close();
			resStreamOut.close();
		}

		return jarFolder + resourceName;
	}

}
