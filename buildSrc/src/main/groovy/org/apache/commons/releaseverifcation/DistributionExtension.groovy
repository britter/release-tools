package org.apache.commons.releaseverifcation

import org.gradle.api.Project
import org.gradle.api.provider.Property

class DistributionExtension {
    Property<String> component

    DistributionExtension(Project project) {
        component = project.objects.property(String)
    }
}
