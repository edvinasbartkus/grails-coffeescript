eventCompileEnd = { binding ->
    // This is very much POC at this point...
    try {
        coffeeSrcDir = new File(basedir, 'src/coffee')
        if(coffeeSrcDir.exists()) {
            coffeeSrcDir.eachFileMatch(~/.*\.coffee/) { coffeeFile ->
                proc = "coffee -c -o ${resourcesDirPath}/js/coffeescriptGenerated ${coffeeFile.absolutePath}".execute()
                proc.in.eachLine { println it}
                proc.err.eachLine { System.err.println(it) }
            }
        }
    } catch (e) {
        System.err.println("ERROR Launching CoffeeScript compiler: ${e.message}")
    }
}

eventCreateWarStart = { name, stagingDir ->
    Ant.copy(todir: "${stagingDir}/js/coffeescriptGenerated/", overwrite: true) {
        fileset(dir: "${resourcesDirPath}/js/coffeescriptGenerated", includes: '**')
    }
    Ant.delete {
        fileset dir: "${stagingDir}/WEB-INF/classes", includes:'CoffeeScriptUrlMappings*'
        fileset dir: "${stagingDir}/WEB-INF/classes", includes:'**/CoffeeScriptController*'
    }
}
