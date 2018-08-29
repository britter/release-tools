package org.apache.commons.releaseverifcation

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReleaseVerificationPlugin implements Plugin<Project> {

    public static final String PLUGIN_TASK_GROUP = 'Release verification'

    @Override
    void apply(final Project project) {
        def extension = project.extensions.create('distribution', DistributionExtension, project)

        def grabReleaseNotes = project.tasks.create('grabReleaseNotes', GrabReleaseNotesTask) {
            group = PLUGIN_TASK_GROUP
            component = extension.component
        }

        def grabBinaries = project.tasks.create('grabBinaries', GrabArtifactsTask) {
            group = PLUGIN_TASK_GROUP
            description = "Grabs the binary artifacts from the binaries artifacts page and downloads them"
            component = extension.component
            type = ArtifactsType.BINARIES
        }

        def grabSource = project.tasks.create('grabSource', GrabArtifactsTask) {
            group = PLUGIN_TASK_GROUP
            description = "Grabs the source artifacts from the source artifacts page and downloads them"
            component = extension.component
            type = ArtifactsType.SOURCE
        }

        project.task('verify', group: PLUGIN_TASK_GROUP, description: 'Executes the full release verification logic')
                .dependsOn(grabReleaseNotes, grabBinaries, grabSource)
    }
}
