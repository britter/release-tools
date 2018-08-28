package org.apache.commons.releaseverifcation

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReleaseVerificationPlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        def extension = project.extensions.create('distribution', DistributionExtension)

        project.task('getArtifacts') {
            doLast {
                def component = extension.baseUrl.split('/')[-1]

                println "Getting artifacts for $component"

                def baseDir = project.mkdir(new File(project.buildDir, component))

                new File(baseDir,'RELEASE-NOTES.txt') << new URL ("${extension.baseUrl}/RELEASE-NOTES.txt").getText()

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

        def file = target.newOutputStream()
        file << new URL(url).openStream()
        file.close()
    }
}
