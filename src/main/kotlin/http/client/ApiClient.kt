package http.client

interface ApiClient {
    fun <T> get(uri: String, headers: Map<String, String>, queryParams: Map<String, String>?): T
    fun <T, B> post(uri: String, body: B, headers: Map<String, String>): T
    fun <T, B> patch(uri: String, body: B, headers: Map<String, String>): T
}
