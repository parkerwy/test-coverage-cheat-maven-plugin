package com.github.parkerwy;


import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MojoTest {
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    @Test
    public void shouldRunGenerateMojo() throws Exception {
        File pom = new File("target/test-classes/project-to-generate/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        GenerateMojo generateMojo = (GenerateMojo) rule.lookupConfiguredMojo(pom, "generate");
        assertNotNull(generateMojo);
        generateMojo.execute();
    }

    @Test
    public void shouldRunCleanMojo() throws Exception {
        File pom = new File("target/test-classes/project-to-clean/");
        assertNotNull(pom);
        assertTrue(pom.exists());

        CleanMojo cleanMojo = (CleanMojo) rule.lookupConfiguredMojo(pom, "clean");
        assertNotNull(cleanMojo);
        cleanMojo.execute();
    }
}

