package org.apache.maven.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(MavenEmbedderProperties.PREFIX)
/**\n * Configuration properties for Maven Embedder.\n *\n * @author <a href="https://github.com/loong10k">Loong Wan</a>\n * @since 1.0.0\n */
public class MavenEmbedderProperties {

	public static final String PREFIX = "maven.embedder";
	
	
}