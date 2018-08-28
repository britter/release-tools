package org.apache.commons.releaseverifcation

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerConfiguration
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

class ReleaseVerificationPlugin implements Plugin<Project> {

    private WorkerExecutor workerExecutor

    @Inject
    ReleaseVerificationPlugin(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor
    }

    @Override
    void apply(final Project project) {
        def extension = project.extensions.create('distribution', DistributionExtension)

        project.task('getArtifacts') {
            doLast {
                def component = extension.baseUrl.split('/')[-1]

                project.logger.info("Getting artifacts for $component")

                def baseDir = project.mkdir(new File(project.buildDir, component))

                workerExecutor.submit(GetFile.class) { WorkerConfiguration config ->
                    config.isolationMode = IsolationMode.NONE
                    config.params new URL ("${extension.baseUrl}/RELEASE-NOTES.txt"), new File(baseDir,'RELEASE-NOTES.txt')
                }

                getArtifacts(extension.baseUrl, 'binaries', baseDir)
                getArtifacts(extension.baseUrl, 'source', baseDir)
            }
        }
    }

    def getArtifacts(String baseUrl, String subUrl, File baseDir) {
        def response = new XmlSlurper().parseText(new URL ("$baseUrl/$subUrl").getText())

        response.body.ul.li.each { li ->
            def artifact = "${li.a.@href}"
            if(artifact != "../") {
                def artifactUrl = "$baseUrl/$subUrl/$artifact"
                getArtifact(artifactUrl, new File(baseDir, artifact))
            }
        }
    }

    def getArtifact(String url, File target) {
        println "Saving $url to $target"

        workerExecutor.submit(GetFile.class) { WorkerConfiguration config ->
            config.isolationMode = IsolationMode.NONE
            config.params new URL(url), target
        }
    }
}
