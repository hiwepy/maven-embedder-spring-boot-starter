/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.maven.spring.boot.ext;

import org.apache.maven.cli.MavenCli;
import org.apache.maven.shared.utils.logging.MessageUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.PrintStream;

/**
 *	嵌入式的Maven集成（基于MavenCli）
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class MavenCliTemplate {

	private PrintStream outputHandler;
	private PrintStream errorHandler;

	public MavenCliTemplate(PrintStream outputHandler, PrintStream errorHandler) {
		this.outputHandler = outputHandler;
		this.errorHandler = errorHandler;
	}
	/**
	 * <p>Do before.</p>
	 * @return the static void
	 */

	private static void doBefore() {

		MessageUtils.systemInstall();
		MessageUtils.registerShutdownHook();

		String mavenHomeProperty = System.getProperty("maven.home");
		if (mavenHomeProperty != null) {
			System.setProperty("maven.multiModuleProjectDirectory", mavenHomeProperty);
		}
		mavenHomeProperty = System.getenv().get("M2_HOME");
		if (mavenHomeProperty != null) {
			System.setProperty("maven.multiModuleProjectDirectory", mavenHomeProperty);
		}
		mavenHomeProperty = System.getenv().get("MAVEN_HOME");
		if (mavenHomeProperty != null) {
			System.setProperty("maven.multiModuleProjectDirectory", mavenHomeProperty);
		}
	}
	/**
	 * <p>Install.</p>
	 * @param filepath the filepath
	 * @param coordinates the coordinates
	 * @return the int
	 */

	public int install(String filepath, String coordinates)  {
		Assert.notNull(coordinates, "coordinates must not be null");
		return this.install(MavenResource.parse(filepath, coordinates));
	}
	/**
	 * <p>Install.</p>
	 * @param resource the resource
	 * @return the int
	 */
	
	public int install(MavenResource resource)  {
		
		doBefore();

		MavenCli cli = new MavenCli();

		int result = cli.doMain(
				new String[] { "install:install-file", "-Dfile=" + resource.getFilepath(), "-DgroupId=" + resource.getGroupId(),
						"-DartifactId=" + resource.getArtifactId(), "-Dversion=" + resource.getVersion(), "-Dpackaging=" + resource.getExtension(),
						"-DgeneratePom=" + resource.isGeneratePom(), "-DcreateChecksum=" + resource.isCreateChecksum() }, // maven line
				resource.getWorkingDirectory(), // working
				outputHandler, // output stream
				errorHandler // error stream
		);

		MessageUtils.systemUninstall();
		
		return result;
		
	}
	/**
	 * <p>Deploy.</p>
	 * @param filepath the filepath
	 * @param coordinates the coordinates
	 * @param repositoryUrl the repository url
	 * @param repositoryId the repository id
	 * @return the int
	 */
	  
	public int deploy(String filepath, String coordinates, String repositoryUrl, String repositoryId) {
		Assert.notNull(coordinates, "coordinates must not be null");
		MavenResource resource = MavenResource.parse(filepath, coordinates);
		resource.setRepositoryId(repositoryId);
		resource.setRepositoryUrl(repositoryUrl);
		return this.deploy(resource);
	}
	/**
	 * <p>Deploy.</p>
	 * @param resource the resource
	 * @return the int
	 */

	public int deploy(MavenResource resource) {
 
		doBefore();

		MavenCli cli = new MavenCli();

		int result = cli.doMain(
				new String[] { "deploy:deploy-file", "-Dfile=" + resource.getFilepath(),
						"-DgroupId=" + resource.getGroupId(), "-DartifactId=" + resource.getArtifactId(),
						"-Dversion=" + resource.getVersion(), "-Dpackaging=" + resource.getExtension(),
						"-Durl=" + resource.getRepositoryUrl(), "-DrepositoryId=" + resource.getRepositoryId() }, // maven
				resource.getWorkingDirectory(), // working
				outputHandler, // output stream
				errorHandler // error stream
		);
		
		MessageUtils.systemUninstall();

		return result;
	}
	/**
	 * <p>Execute.</p>
	 * @param workingDirectory the working directory
	 * @param goals the goals
	 * @return the int
	 */

	public int execute(String workingDirectory, String... goals) {
		
		doBefore();

		MavenCli cli = new MavenCli();

		int result = cli.doMain(goals, // maven line
			StringUtils.hasText(workingDirectory) ? workingDirectory : System.getProperty("user.home"), // working
			outputHandler, // output stream
			errorHandler // error stream
		);
		
		MessageUtils.systemUninstall();

		return result;
	}

}
