#!/usr/bin/env groovy

baseUrl = args[0]

component = baseUrl.split("/")[-1]

println "Getting artifacts for $component"

baseDir = "target/$component" 

new File(baseDir).mkdirs()

new File("$baseDir/RELEASE-NOTES.txt") << new URL ("$baseUrl/RELEASE-NOTES.txt").getText()

getArtifacts("binaries")
getArtifacts("source")

def getArtifacts(String subUrl) {
  response = new XmlSlurper().parseText(new URL ("$baseUrl/$subUrl").getText())

  response.body.ul.li.each { li ->
    def artifact = "${li.a.@href}"
    if(artifact != "../") {
      def artifactUrl = "$baseUrl/$subUrl/$artifact"
      getArtifact(artifactUrl, "$baseDir/$artifact")
    }
  }
}

def getArtifact(String url, String target) {
  println "Saving $url to $target"

  def file = new File(target).newOutputStream()
  file << new URL(url).openStream()
  file.close()  
}
