package org.apache.commons.releaseverifcation

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.IsolationMode
import org.gradle.workers.WorkerConfiguration
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

class GrabReleaseNotesTask extends DefaultTask {

    final Property<String> component = project.objects.property(String)

    String description = 'Downloads the RELEASE-NOTES.txt file'

    private WorkerExecutor workerExecutor

    @Inject
    GrabReleaseNotesTask(WorkerExecutor workerExecutor) {
        this.workerExecutor = workerExecutor
    }

    @TaskAction
    void grabReleaseNotes() {
        URL baseUrl = new URL("https://dist.apache.org/repos/dist/dev/commons/${component.get()}/")

        File target = project.mkdir(project.file("${project.buildDir}/${component.get()}"))

        workerExecutor.submit(GetFile.class) { WorkerConfiguration config ->
            config.isolationMode = IsolationMode.NONE
            config.params new URL (baseUrl, 'RELEASE-NOTES.txt'), project.file("$target/RELEASE-NOTES.txt")
        }
    }
}
