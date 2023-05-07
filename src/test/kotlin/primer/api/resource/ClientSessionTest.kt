package primer.api.resource

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import primer.Primer
import primer.api.request.dto.CreateClientSessionData

class ClientSessionTest {

    private lateinit var server: MockWebServer

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        Primer.apiKey = "primerApiKey"
        Primer.apiVersion = "2.2"
        server.start()
        val url = server.url("/")
        Primer.unitTestUrl = url.toUrl()
    }

    @Test
    fun `Should successfully create ClientSession object`() {
        // Arrange
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(clientSessionSuccessResponseJson),
        )

        // Act
        val clientSession = ClientSession.create(CreateClientSessionData())

        // Assert
        assertThat(clientSession.clientToken).isEqualTo("client-session-token")
    }

    val clientSessionSuccessResponseJson =
        """{
              "clientToken": "client-session-token",
              "clientExpirationDate": "2019-08-24T14:15:22Z",
              "customerId": "customer-123",
              "orderId": "order-abc",
              "currencyCode": "GBP",
              "metadata": {
                "productType": "Shoe"
              },
              "customer": {
                "emailAddress": "john@primer.io"
              },
              "amount": 20,
              "order": {
                "countryCode": "FR",
                "fees": [
                  {
                    "type": "SURCHARGE",
                    "amount": 20
                  }
                ]
              },
              "paymentMethod": {
                "vaultOnSuccess": true,
                "options": {
                  "PAYMENT_CARD": {
                    "networks": {
                      "VISA": {
                        "surcharge": {
                          "amount": 10
                        }
                      }
                    }
                  },
                  "GOOGLE_PAY": {
                    "surcharge": {
                      "amount": 20
                    }
                  }
                }
              },
              "warnings": [
                {
                  "type": "TAXJAR",
                  "code": "MISSING_DATA",
                  "message": "Shipping details are required for calculating tax."
                }
              ]
            }
        """.trimIndent()
}
