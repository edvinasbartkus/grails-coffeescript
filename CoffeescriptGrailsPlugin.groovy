import grails.util.BuildSettingsHolder

class CoffeescriptGrailsPlugin {
    // the plugin version
    def version = "1.0-SNAPSHOT"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/demo/*",
            "src/coffee/**"
    ]
    def watchedResources = [ "file:./src/coffee/*.coffee", "file:./src/coffee/**/*.coffee",  ]

    def author = "Jeff Brown"
    def authorEmail = ""
    def title = "CoffeeScript Plugin For Grails"
    def description = '''\\
The CoffeeScript plugin for Grails provides CoffeeScript integration.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/coffeescript"

    def onChange = { event ->
        def source = event.source
        if(source instanceof org.springframework.core.io.FileSystemResource &&
            (source.file.name.endsWith('.coffee'))) {
            try {
                def grailsSettings = BuildSettingsHolder.settings
                def resourcesDir = grailsSettings.resourcesDir
                def coffeeCompilerLocation = System.getProperty('grails.coffeescript.compiler.location')
                if(!coffeeCompilerLocation) {
                    coffeeCompilerLocation = grailsSettings.config.grails.coffeescript.compiler.location
                    if(!coffeeCompilerLocation) {
                        coffeeCompilerLocation = 'coffee'
                    }
                }
                def dir = source.file.parent.replace(new File(grailsSettings.baseDir, "src/coffee").toString(), "")
                new File(resourcesDir.path, "js/coffeescriptGenerated${dir}").mkdirs()

                def proc = "${coffeeCompilerLocation} -c -o ${resourcesDir}/js/coffeescriptGenerated/${dir} ${source.file.absolutePath}".execute()
                println source.file.absolutePath
                proc.in.eachLine { println it}
                proc.err.eachLine { System.err.println(it) }
            } catch (e) {
                System.err.println("ERROR Launching CoffeeScript compiler: ${e.message}")
            }
        }
    }
}
