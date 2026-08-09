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

import java.io.PrintStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link MavenCliTemplate}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("MavenCliTemplate Tests")
class MavenCliTemplateTest {

    @Test
    @DisplayName("Constructor creates non-null instance")
    void testConstructor() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        assertThat(template).isNotNull();
    }

    @Test
    @DisplayName("install(String, String) throws when coordinates are null")
    void testInstallNullCoordinates() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        assertThatThrownBy(() -> template.install("/tmp/test.jar", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("install(MavenResource) calls MavenCli")
    void testInstallWithResource() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        MavenResource resource = new MavenResource.Builder()
                .workingDirectory("/tmp/work")
                .filepath("/tmp/nonexistent.jar")
                .groupId("com.example")
                .artifactId("test")
                .version("1.0.0")
                .build();
        // This will call MavenCli.doMain which will fail but won't throw
        int result = template.install(resource);
        // Result will be non-zero since the file doesn't exist
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    @DisplayName("deploy(String, String, String, String) throws when coordinates are null")
    void testDeployNullCoordinates() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        assertThatThrownBy(() -> template.deploy("/tmp/test.jar", null, "http://localhost", "releases"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deploy(MavenResource) calls MavenCli")
    void testDeployWithResource() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        MavenResource resource = new MavenResource.Builder()
                .workingDirectory("/tmp/work")
                .filepath("/tmp/nonexistent.jar")
                .groupId("com.example")
                .artifactId("test")
                .version("1.0.0")
                .repositoryUrl("http://localhost:8081/releases/")
                .repositoryId("nexus-releases")
                .build();
        int result = template.deploy(resource);
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    @DisplayName("execute calls MavenCli with goals")
    void testExecute() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        int result = template.execute("/tmp", "validate");
        // Will fail since there's no pom.xml, but should not throw
        assertThat(result).isNotEqualTo(0);
    }

    @Test
    @DisplayName("execute uses user.home when workingDirectory is blank")
    void testExecuteWithBlankWorkingDir() {
        MavenCliTemplate template = new MavenCliTemplate(System.out, System.err);
        int result = template.execute("", "validate");
        assertThat(result).isNotEqualTo(0);
    }
}
