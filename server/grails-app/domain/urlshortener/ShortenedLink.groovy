package urlshortener

/**
 * A link containing the shortened and original url
 */
class ShortenedLink {
    /**
     * Destination of shortened link
     * (URL that is shortened)
     */
    String destination

    /**
     * New short name of url
     */
    String shortName

    //TODO: turn shortName to an id map

    static constraints = {
        destination(nullable: false, blank: false, unique: true, url: true)
        shortName(nullable: false, blank: false, unique: true)
    }
}
