package com.github.parkerwy;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class AbstractBaseMojo  extends AbstractMojo {

    @Parameter(required = true, property = "sourceDirectory", defaultValue = "${project.build.sourceDirectory}")
    private File sourceDirectory;

    @Parameter(required = true, property = "testSourceDirectory", defaultValue = "${project.build.testSourceDirectory}")
    private File testSourceDirectory;

    @Parameter(required = true, defaultValue = "test_coverage_cheating")
    private String basePackage;

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public File getTestSourceDirectory() {
        return testSourceDirectory;
    }

    public String getBasePackage() {
        return basePackage;
    }
}
