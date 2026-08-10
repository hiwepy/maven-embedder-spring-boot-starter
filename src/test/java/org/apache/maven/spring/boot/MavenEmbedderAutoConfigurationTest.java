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
package org.apache.maven.spring.boot;

import java.io.PrintStream;

import org.apache.maven.cli.event.ExecutionEventLogger;
import org.apache.maven.cli.logging.Slf4jLoggerManager;
import org.apache.maven.cli.transfer.Slf4jMavenTransferListener;
import org.apache.maven.spring.boot.ext.MavenCliTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link MavenEmbedderAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("MavenEmbedderAutoConfiguration Tests")
class MavenEmbedderAutoConfigurationTest {

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("transferListener returns Slf4jMavenTransferListener")
    void testTransferListener() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        assertThat(config.transferListener()).isInstanceOf(Slf4jMavenTransferListener.class);
    }

    @Test
    @DisplayName("executionListener returns ExecutionEventLogger")
    void testExecutionListener() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        assertThat(config.executionListener()).isInstanceOf(ExecutionEventLogger.class);
    }

    @Test
    @DisplayName("mavenLoggerManager returns Slf4jLoggerManager")
    void testMavenLoggerManager() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        assertThat(config.mavenLoggerManager()).isInstanceOf(Slf4jLoggerManager.class);
    }

    @Test
    @DisplayName("outputHandler returns PrintStream")
    void testOutputHandler() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        PrintStream out = config.outputHandler();
        assertThat(out).isNotNull();
    }

    @Test
    @DisplayName("errorHandler returns PrintStream")
    void testErrorHandler() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        PrintStream err = config.errorHandler();
        assertThat(err).isNotNull();
    }

    @Test
    @DisplayName("mavenCliTemplate returns MavenCliTemplate")
    void testMavenCliTemplate() {
        MavenEmbedderAutoConfiguration config = new MavenEmbedderAutoConfiguration();
        PrintStream out = config.outputHandler();
        PrintStream err = config.errorHandler();
        MavenCliTemplate template = config.mavenCliTemplate(out, err);
        assertThat(template).isNotNull();
    }
}
