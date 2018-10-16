package com.github.parkerwy;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Mojo(name = "clean", defaultPhase = LifecyclePhase.NONE)
public class CleanMojo extends AbstractBaseMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String[] path = StringUtils.split(this.getBasePackage(), ".");

        {
            File directory = FileUtils.getFile(this.getTestSourceDirectory(), path);
            this.getLog().info("Clean generated test sources in " + directory);
            cleanFiles(directory);
        }

        {
            File directory = FileUtils.getFile(this.getSourceDirectory(), path);
            this.getLog().info("Clean generated sources in " + directory);
            cleanFiles(directory);
        }

    }

    private void cleanFiles(File directory) throws MojoExecutionException {
        if (!directory.exists()) {
            return;
        }
        try {
            Collection<File> files = FileUtils.listFilesAndDirs(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            List<File> fileList = new ArrayList<>(files);
            Collections.sort(fileList);
            Collections.reverse(fileList);
            for (File file : fileList) {
                if (file.isFile()) {
                    String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                    if (StringUtils.contains(content, "test-coverage-cheating")) {
                        FileUtils.deleteQuietly(file);
                    } else {
                        this.getLog().warn("Skip file [" + file.getAbsolutePath() + "] as tag not found.");
                    }
                } else if (file.isDirectory()) {
                    Collection<File> collection = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
                    if (collection.isEmpty()) {
                        FileUtils.deleteQuietly(file);
                    } else {
                        this.getLog().warn("Skip directory [" + file.getAbsolutePath() + "] as it's not empty.");
                    }
                } else {
                    throw new MojoExecutionException("Unsupported file " + file);
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to clean files.", e);
        }
    }
}
