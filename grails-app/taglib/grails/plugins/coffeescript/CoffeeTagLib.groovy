package grails.plugins.coffeescript

class CoffeeTagLib {

    static namespace = 'coffee'
    
    def inline = { attrs, body ->
        if(!pageScope.variables.alreadyIncludedCoffeeJs) {
            def uri = g.resource(plugin: 'coffeescript', dir:'js',file:'coffee-script.js')
            out << g.render(plugin: 'coffeescript', template: '/templates/coffeeScriptJs', model: [uri: uri])
            pageScope.alreadyIncludedCoffeeJs = true
        }
        def code = body()
        out << g.render(plugin: 'coffeescript', template: '/templates/inlineCoffeeScript', model: [coffeeScriptCode: code])
    }
    
    def script = { attrs ->
        def name = attrs.name
        def uri = g.createLink(controller: 'coffeeScript', action: 'generatedJavascript', params: [fileName: name])
        out << g.render(plugin: 'coffeescript', template: '/templates/coffeeScriptJs', model: [uri: uri])
    }
}
