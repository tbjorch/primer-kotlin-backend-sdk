package primer.api

import java.net.URL

interface ApiClient {
    fun <T> get(url: URL, headers: Map<String, String>, queryParams: Map<String, String>?, responseType: Class<T>): T
    fun <T, B> post(url: URL, body: B, headers: Map<String, String>, responseType: Class<T>): T
    fun <T, B> patch(url: URL, body: B, headers: Map<String, String>, responseType: Class<T>): T
    fun <T> delete(url: URL, headers: Map<String, String>, responseType: Class<T>): T
}
