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
/**\n * Auto-configuration for Maven Embedder integration.\n *\n * @author <a href="https://github.com/loong10k">Loong Wan</a>\n * @since 1.0.0\n */
public class MavenEmbedderAutoConfiguration {
	/**
	 * <p>Transfer listener.</p>
	 * @return the transfer listener
	 */

	@Bean
	@ConditionalOnMissingBean
	public TransferListener transferListener() {
		return new Slf4jMavenTransferListener();
	}
	/**
	 * <p>Execution listener.</p>
	 * @return the execution listener
	 */

	@Bean
	@ConditionalOnMissingBean
	public ExecutionListener executionListener() {
		return new ExecutionEventLogger();
	}
	/**
	 * <p>Maven logger manager.</p>
	 * @return the logger manager
	 */

	@Bean
	@ConditionalOnMissingBean
	public LoggerManager mavenLoggerManager() {
		return new Slf4jLoggerManager();
	}
	/**
	 * <p>Output handler.</p>
	 * @return the print stream
	 */

	@Bean
	@ConditionalOnMissingBean
	public PrintStream outputHandler() {
		return new PrintStream(System.out, false);
	}
	/**
	 * <p>Error handler.</p>
	 * @return the print stream
	 */

	@Bean
	@ConditionalOnMissingBean
	public PrintStream errorHandler() {
		return new PrintStream(System.err, false);
	}
	/**
	 * <p>Maven cli template.</p>
	 * @param outputHandler the output handler
	 * @param errorHandler the error handler
	 * @return the maven cli template
	 */

	@Bean
	public MavenCliTemplate mavenCliTemplate(PrintStream outputHandler, PrintStream errorHandler) {
		return new MavenCliTemplate(outputHandler, errorHandler);
	}

}
