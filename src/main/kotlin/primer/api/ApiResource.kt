package primer.api

import http.client.ApiClient
import primer.Primer

object ApiResource {

    private val client: ApiClient = TODO("Add implementation of ApiClient")

    fun <T> get(
        path: String,
        id: String? = null,
        params: Map<String, String>? = null,
    ): T {
        var resourseUrl = "${Primer.baseUrl}/$path"
        id?.let { resourseUrl += "/$id" }
        return client.get(
            uri = resourseUrl,
            headers = mapOf(
                PrimerHeader.X_API_KEY.value to Primer.apiKey,
            ),
            queryParams = params,
        )
    }

    fun <T, B> post(
        path: String,
        idempotencyKey: String? = null,
        body: B,
    ): T {
        val resourceUrl = "${Primer.baseUrl}/$path"
        val headers = mutableMapOf(PrimerHeader.X_API_KEY.value to Primer.apiKey)
        idempotencyKey?.let { headers.plus(PrimerHeader.IDEMPOTENCY_KEY.value to idempotencyKey) }
        return client.post(
            uri = resourceUrl,
            body = body,
            headers = headers,
        )
    }

    fun <T, B> patch(
        path: String,
        idempotencyKey: String?,
        body: B,
    ): T {
        val resourceUrl = "${Primer.baseUrl}/$path"
        val headers = mutableMapOf(PrimerHeader.X_API_KEY.value to Primer.apiKey)
        idempotencyKey?.let { headers.plus(PrimerHeader.IDEMPOTENCY_KEY.value to idempotencyKey) }
        return client.patch(
            uri = resourceUrl,
            body = body,
            headers = headers,
        )
    }

    enum class PrimerHeader(val value: String) {
        X_API_KEY("X-API-KEY"),
        IDEMPOTENCY_KEY("X-IDEMPOTENCY-KEY"),
    }
}
