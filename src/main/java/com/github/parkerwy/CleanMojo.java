package com.github.parkerwy;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "clean", defaultPhase = LifecyclePhase.NONE)
public class CleanMojo extends AbstractBaseMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.getLog().info("Clean generated sources in " + this.getSourceDirectory());
        this.getLog().info("Clean generated test sources in " + this.getTestSourceDirectory());
    }
}
