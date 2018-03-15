import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.platform.scripting.MimeFixer

class GroovyMimeFixer implements MimeFixer {
    final static FIND_ALL_QUERY = "SELECT {PK} FROM {Media} WHERE {mime} IS NOT NULL"
    final static FIND_FOR_EXT_QUERY = FIND_ALL_QUERY + " AND {realfilename} LIKE ?realfilename"

    def flexibleSearchService
    def mimeService
    def modelService
    def int fixAllMimes() {
        def query = new FlexibleSearchQuery(FIND_ALL_QUERY)
        def counter = 0
        flexibleSearchService.search(query).result.each {
            it.mime = mimeService.getMimeFromFileExtension(it.realfilename)
            modelService.save it
            counter++
        }
        counter
    }

    def int fixMimesForExtension(String extension) {
        def query = new FlexibleSearchQuery(FIND_FOR_EXT_QUERY)
        query.addQueryParameter("realfilename", "%.${extension}");
        def counter = 0
        flexibleSearchService.search(FIND_FOR_EXT_QUERY).result.each {
            it.mime = mimeService.getMimeFromFileExtension(it.realfilename)
            modelService.save it
            counter++
        }
        counter
    }
}

flexibleSearchService = spring.getBean "flexibleSearchService"
mimeService = spring.getBean "mimeService"
modelService = spring.getBean "modelService"

new GroovyMimeFixer(flexibleSearchService: flexibleSearchService, mimeService: mimeService, modelService: modelService)