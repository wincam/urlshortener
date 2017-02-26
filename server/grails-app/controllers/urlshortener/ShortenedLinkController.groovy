package urlshortener

import grails.converters.JSON
import grails.rest.*

/**
 * Controller for shortened links
 */
class ShortenedLinkController extends RestfulController {
    static allowedMethods = [save: "POST", index: "GET", show: "GET"]
	static responseFormats = ['json']

    ShortenedLinkController(){
        super(ShortenedLink)
    }

    /**
     * The default method for this class
     *
     * @param l     Id of link
     */
    def index (){
        this.displayLink(params.l)
    }

    /**
     * Redirects to a page of the link exists
     *
     * @param id    Id of link
     */
    def show() {
        this.displayLink(params.id)
    }



    /**
     * Creates a new link or returns one if it already exists
     *
     * @param dest  Destination of link
     * @return      New or existing link
     */
    def save() {

        // find if all exists
        println("hello")
        String dest = params.dest

        // dest not given
        if (dest == null){
            render (status : 400)
            return
        }

        def link = ShortenedLink.findByDestination(dest)

        if (link != null){
            // link already exists
            render (link as JSON)
        } else {
            // link does not exist
            def newLink = new ShortenedLink(destination: dest, shortName: this.randomString((('A'..'Z')+('a'..'z')+('0'..'9')+'-'+'_').join(),5))

            // invalid destination
            if (newLink.errors.hasFieldErrors("destination")){
                render (status: 400)
                return
            }

            // regen shortName if necessary
            while (newLink.hasErrors()) {
                newLink.setShortName(this.randomString((('A'..'Z') + ('a'..'z') + ('0'..'9') + '-' + '_').join(), 5))
            }

            newLink.save()

            render (newLink as JSON)

        }

    }

    /**
     * Displays link from id
     * @param linkId    Id of link
     * @return          A redirect to the link, 404 or 400
     */
    private def displayLink(String linkId){

        // no param provided
        if (linkId == null){
            render (status : 400)
            return
        }

        // find link
        def link = ShortenedLink.findByShortName(linkId)

        // link does not exist
        if (link == null){
            render (status : 404)
            return
        }

        // redirect to destination
        redirect(url: link.getDestination())
    }

    /**
     * Generates a random string
     * @param allowed   Characters that are allowed
     * @param n         Number of characters to be generated
     * @return          The string that was generated
     */
    static private String randomString(String allowed, int n) {
        return new Random().with {
            (1..n).collect { allowed[ nextInt( allowed.length() ) ] }.join()
        }.toString()
    }
}
