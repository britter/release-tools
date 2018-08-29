package org.apache.commons.releaseverifcation

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerConfiguration
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

class GrabArtifactsTask extends DefaultTask {

    final Property<String> component = project.objects.property(String)

    ArtifactsType type

    private WorkerExecutor workerExecutor

    @Inject
    GrabArtifactsTask(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor
    }

    @TaskAction
    void getArtifacts() {
        String baseUrl = "https://dist.apache.org/repos/dist/dev/commons/${component.get()}/${type.toString().toLowerCase()}"
        File target = project.mkdir(project.file("${project.buildDir}/${component.get()}/${type.toString().toLowerCase()}"))

        def response = new XmlSlurper().parseText(new URL(baseUrl).getText())

        response.body.ul.li.each { li ->
            def artifact = "${li.a.@href}"
            if(artifact != "../") {
                getArtifact(new URL("$baseUrl/$artifact"), project.file("$target/$artifact"))
            }
        }
    }

    def getArtifact(URL url, File target) {
        getLogger().debug("Saving $url to $target")

        workerExecutor.submit(GetFile.class) { WorkerConfiguration config ->
            config.isolationMode = IsolationMode.NONE
            config.params url, target
        }
    }
}
