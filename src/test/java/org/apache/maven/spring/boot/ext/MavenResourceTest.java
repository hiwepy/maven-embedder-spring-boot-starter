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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link MavenResource}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@DisplayName("MavenResource Tests")
class MavenResourceTest {

    @Test
    @DisplayName("Builder creates MavenResource with all fields")
    void testBuilder() {
        MavenResource resource = new MavenResource.Builder()
                .workingDirectory("/tmp/work")
                .filepath("/tmp/p6spy-3.8.1.jar")
                .groupId("p6spy")
                .artifactId("p6spy")
                .version("3.8.1")
                .extension("jar")
                .classifier("exec")
                .generatePom(true)
                .createChecksum(true)
                .repositoryUrl("http://localhost:8081/releases/")
                .repositoryId("nexus-releases")
                .build();

        assertThat(resource).isNotNull();
        assertThat(resource.getGroupId()).isEqualTo("p6spy");
        assertThat(resource.getArtifactId()).isEqualTo("p6spy");
        assertThat(resource.getVersion()).isEqualTo("3.8.1");
        assertThat(resource.getExtension()).isEqualTo("jar");
        assertThat(resource.getClassifier()).isEqualTo("exec");
        assertThat(resource.getFilepath()).isEqualTo("/tmp/p6spy-3.8.1.jar");
        assertThat(resource.isGeneratePom()).isTrue();
        assertThat(resource.isCreateChecksum()).isTrue();
        assertThat(resource.getRepositoryUrl()).isEqualTo("http://localhost:8081/releases/");
        assertThat(resource.getRepositoryId()).isEqualTo("nexus-releases");
    }

    @Test
    @DisplayName("Builder uses default extension 'jar'")
    void testBuilderDefaults() {
        MavenResource resource = new MavenResource.Builder()
                .workingDirectory("/tmp/work")
                .filepath("/tmp/test.jar")
                .groupId("com.example")
                .artifactId("my-app")
                .version("1.0.0")
                .build();

        assertThat(resource.getExtension()).isEqualTo("jar");
        assertThat(resource.getClassifier()).isEmpty();
        assertThat(resource.isGeneratePom()).isFalse();
        assertThat(resource.isCreateChecksum()).isFalse();
    }

    @Test
    @DisplayName("parse parses coordinates without extension and classifier")
    void testParseSimple() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:1.0.0");
        assertThat(resource.getGroupId()).isEqualTo("org.example");
        assertThat(resource.getArtifactId()).isEqualTo("my-app");
        assertThat(resource.getVersion()).isEqualTo("1.0.0");
        assertThat(resource.getExtension()).isEqualTo("jar");
        assertThat(resource.getClassifier()).isEmpty();
    }

    @Test
    @DisplayName("parse parses coordinates with extension and classifier")
    void testParseFull() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:war:sources:1.0.0");
        assertThat(resource.getGroupId()).isEqualTo("org.example");
        assertThat(resource.getArtifactId()).isEqualTo("my-app");
        assertThat(resource.getExtension()).isEqualTo("war");
        assertThat(resource.getClassifier()).isEqualTo("sources");
        assertThat(resource.getVersion()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("parse parses coordinates with extension but no classifier")
    void testParseWithExtension() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:war:1.0.0");
        assertThat(resource.getGroupId()).isEqualTo("org.example");
        assertThat(resource.getArtifactId()).isEqualTo("my-app");
        assertThat(resource.getExtension()).isEqualTo("war");
        assertThat(resource.getClassifier()).isEmpty();
        assertThat(resource.getVersion()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("parse throws on invalid coordinates")
    void testParseInvalid() {
        assertThatThrownBy(() -> MavenResource.parse("/tmp/app.jar", "invalid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("parse throws on blank coordinates")
    void testParseBlank() {
        assertThatThrownBy(() -> MavenResource.parse("/tmp/app.jar", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("getFilename returns correct format without classifier")
    void testGetFilenameNoClassifier() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:1.0.0");
        assertThat(resource.getFilename()).isEqualTo("my-app-1.0.0.jar");
    }

    @Test
    @DisplayName("getFilename returns correct format with classifier")
    void testGetFilenameWithClassifier() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:jar:sources:1.0.0");
        assertThat(resource.getFilename()).isEqualTo("my-app-1.0.0-sources.jar");
    }

    @Test
    @DisplayName("getWorkingDirectory returns set workingDirectory")
    void testGetWorkingDirectory() {
        MavenResource resource = new MavenResource.Builder()
                .workingDirectory("/tmp/work")
                .filepath("/tmp/my-app.jar")
                .groupId("com.example")
                .artifactId("my-app")
                .version("1.0.0")
                .build();
        assertThat(resource.getWorkingDirectory()).isEqualTo("/tmp/work");
    }

    @Test
    @DisplayName("getWorkingDirectory returns filepath baseName when workingDirectory is empty via parse")
    void testGetWorkingDirectoryFromFilePath() {
        MavenResource resource = MavenResource.parse("/tmp/my-app.jar", "com.example:my-app:1.0.0");
        // parse sets workingDirectory to FilenameUtils.getBaseName(filepath)
        assertThat(resource.getWorkingDirectory()).isEqualTo("my-app");
    }

    @Test
    @DisplayName("toString returns correct format without classifier")
    void testToStringNoClassifier() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:1.0.0");
        assertThat(resource.toString()).isEqualTo("org.example:my-app:jar:1.0.0");
    }

    @Test
    @DisplayName("toString returns correct format with classifier")
    void testToStringWithClassifier() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:jar:sources:1.0.0");
        assertThat(resource.toString()).isEqualTo("org.example:my-app:jar:sources:1.0.0");
    }

    @Test
    @DisplayName("equals returns true for same coordinates")
    void testEquals() {
        MavenResource r1 = MavenResource.parse("/tmp/a.jar", "org.example:my-app:1.0.0");
        MavenResource r2 = MavenResource.parse("/tmp/b.jar", "org.example:my-app:1.0.0");
        assertThat(r1).isEqualTo(r2);
    }

    @Test
    @DisplayName("equals returns false for different coordinates")
    void testNotEquals() {
        MavenResource r1 = MavenResource.parse("/tmp/a.jar", "org.example:my-app:1.0.0");
        MavenResource r2 = MavenResource.parse("/tmp/b.jar", "org.example:my-app:2.0.0");
        assertThat(r1).isNotEqualTo(r2);
    }

    @Test
    @DisplayName("equals returns false for non-MavenResource object")
    void testNotEqualsOtherType() {
        MavenResource r1 = MavenResource.parse("/tmp/a.jar", "org.example:my-app:1.0.0");
        assertThat(r1.equals("not a resource")).isFalse();
    }

    @Test
    @DisplayName("equals returns true for same instance")
    void testEqualsSameInstance() {
        MavenResource r1 = MavenResource.parse("/tmp/a.jar", "org.example:my-app:1.0.0");
        assertThat(r1.equals(r1)).isTrue();
    }

    @Test
    @DisplayName("hashCode is consistent for equal objects")
    void testHashCode() {
        MavenResource r1 = MavenResource.parse("/tmp/a.jar", "org.example:my-app:1.0.0");
        MavenResource r2 = MavenResource.parse("/tmp/b.jar", "org.example:my-app:1.0.0");
        assertThat(r1.hashCode()).isEqualTo(r2.hashCode());
    }

    @Test
    @DisplayName("hashCode varies with classifier")
    void testHashCodeWithClassifier() {
        MavenResource r1 = MavenResource.parse("/tmp/a.jar", "org.example:my-app:jar:1.0.0");
        MavenResource r2 = MavenResource.parse("/tmp/b.jar", "org.example:my-app:jar:sources:1.0.0");
        assertThat(r1.hashCode()).isNotEqualTo(r2.hashCode());
    }

    @Test
    @DisplayName("URI_SCHEME constant has expected value")
    void testUriScheme() {
        assertThat(MavenResource.URI_SCHEME).isEqualTo("maven");
    }

    @Test
    @DisplayName("getDescription returns toString")
    void testGetDescription() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:1.0.0");
        assertThat(resource.getDescription()).isEqualTo(resource.toString());
    }

    @Test
    @DisplayName("setRepositoryUrl and setRepositoryId work correctly")
    void testSetters() {
        MavenResource resource = MavenResource.parse("/tmp/app.jar", "org.example:my-app:1.0.0");
        resource.setRepositoryUrl("http://localhost:8081/releases/");
        resource.setRepositoryId("nexus-releases");
        assertThat(resource.getRepositoryUrl()).isEqualTo("http://localhost:8081/releases/");
        assertThat(resource.getRepositoryId()).isEqualTo("nexus-releases");
    }

    @Test
    @DisplayName("Builder constructor creates non-null instance")
    void testBuilderConstructor() {
        MavenResource.Builder builder = new MavenResource.Builder();
        assertThat(builder).isNotNull();
    }

    @Test
    @DisplayName("Builder throws when required fields are missing")
    void testBuilderMissingFields() {
        assertThatThrownBy(() -> new MavenResource.Builder()
                .filepath("/tmp/test.jar")
                .groupId("com.example")
                .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
