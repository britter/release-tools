package org.apache.commons.releaseverifcation

import javax.inject.Inject

class GetFile implements Runnable {

    private URL source
    private File target

    @Inject
    GetFile(URL source, File target) {
        this.source = source
        this.target = target
    }

    @Override
    void run() {
        def file = target.newOutputStream()
        file << source.openStream()
        file.close()
    }
}
