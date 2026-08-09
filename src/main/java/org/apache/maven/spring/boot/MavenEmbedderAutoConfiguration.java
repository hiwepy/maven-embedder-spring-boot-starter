package org.apache.maven.spring.boot;

import java.io.PrintStream;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.cli.event.ExecutionEventLogger;
import org.apache.maven.cli.logging.Slf4jLoggerManager;
import org.apache.maven.cli.transfer.Slf4jMavenTransferListener;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.spring.boot.ext.MavenCliTemplate;
import org.codehaus.plexus.logging.LoggerManager;
import org.eclipse.aether.transfer.TransferListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ MavenCli.class })
@EnableConfigurationProperties({ MavenEmbedderProperties.class })
/**\n * Auto-configuration for Maven Embedder integration.\n *\n * @author [@Loong Wan](https://github.com/loong10k)\n * @since 1.0.0\n */
public class MavenEmbedderAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public TransferListener transferListener() {
		return new Slf4jMavenTransferListener();
	}

	@Bean
	@ConditionalOnMissingBean
	public ExecutionListener executionListener() {
		return new ExecutionEventLogger();
	}

	@Bean
	@ConditionalOnMissingBean
	public LoggerManager mavenLoggerManager() {
		return new Slf4jLoggerManager();
	}

	@Bean
	@ConditionalOnMissingBean
	public PrintStream outputHandler() {
		return new PrintStream(System.out, false);
	}

	@Bean
	@ConditionalOnMissingBean
	public PrintStream errorHandler() {
		return new PrintStream(System.err, false);
	}

	@Bean
	public MavenCliTemplate mavenCliTemplate(PrintStream outputHandler, PrintStream errorHandler) {
		return new MavenCliTemplate(outputHandler, errorHandler);
	}

}
