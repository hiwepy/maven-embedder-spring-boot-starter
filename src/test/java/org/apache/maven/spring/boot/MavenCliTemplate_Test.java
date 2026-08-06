/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
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
package org.apache.maven.spring.boot;

import java.io.PrintStream;

import org.apache.maven.spring.boot.ext.MavenCliTemplate;
import org.apache.maven.spring.boot.ext.MavenResource;
import org.junit.Test;

public class MavenCliTemplate_Test {

	public PrintStream outputHandler = new PrintStream(System.out, false);
	public PrintStream errorHandler = new PrintStream(System.err, false);

	@Test
	public void testInstall() {

		MavenCliTemplate template = new MavenCliTemplate(outputHandler, errorHandler);

		int result1 = template.install(MavenResource.parse("D:\\p6spy-3.8.1.jar", "p6spy:p6spy:3.8.1-xx"));
		System.out.println("ExitCode:" + result1);

		MavenResource resource = new MavenResource.Builder().filepath("D:\\p6spy-3.8.1.jar").groupId("p6spy")
				.artifactId("p6spy").version("3.8.1-xx").generatePom(true).createChecksum(true).build();

		int result = template.install(resource);

		System.out.println("ExitCode:" + result);

	}

	// @Test
	public void testDeploy() {

		MavenCliTemplate template = new MavenCliTemplate(outputHandler, errorHandler);

		MavenResource resource = new MavenResource.Builder().filepath("D:\\p6spy-3.8.1.jar").groupId("p6spy")
				.artifactId("p6spy").version("3.8.1-xx").repositoryId("nexus-releases")
				.repositoryUrl("http://127.0.0.1:8081/repository/maven-releases/").build();

		int result = template.deploy(resource);

		System.out.println("ExitCode:" + result);

	}

	// @Test
	public void testExecute() {

		MavenCliTemplate template = new MavenCliTemplate(outputHandler, errorHandler);

		int result = template.execute("D:\\", "deploy:deploy-file", "-DgroupId=p6spy", "-DartifactId=p6spy",
				"-Dversion=3.7.0-xx", "-Dpackaging=jar", "-Dfile=p6spy-3.7.0.jar",
				"-Durl=http://10.71.19.153:8081/nexus/content/repositories/thirdparty/",
				"-DrepositoryId=nexus-thirdparty");

		System.out.println("ExitCode:" + result);

	}

}
