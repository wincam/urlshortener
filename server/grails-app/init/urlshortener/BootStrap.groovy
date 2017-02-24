package urlshortener

class BootStrap {

    def init = { servletContext ->
        // test link
        def link = new ShortenedLink(destination: "https://youtube.com", shortName: "abc")
        link.save()
    }
    def destroy = {
    }
}
