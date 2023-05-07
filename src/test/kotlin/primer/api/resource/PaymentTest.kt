package primer.api.resource

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import primer.Fixtures
import primer.Primer
import primer.api.request.dto.CreatePaymentDto
import primer.exception.PaymentException
import java.util.UUID
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PaymentTest {

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
    fun `Should create payment`() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(paymentCreateSuccessResponse))

        val payment = Payment.create(
            CreatePaymentDto(
                paymentMethodToken = "paymentMethodToken",
                amount = 10000,
                currencyCode = "SEK",
                orderId = UUID.randomUUID().toString(),
                customerId = "customerId",
            ),
        )

        assertEquals(payment.id, "kHdEw9EG")
        assertEquals(payment.paymentMethod.paymentMethodType, "PAYMENT_CARD")
    }

    @Test
    fun `Should retrieve an existing payment`() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(paymentCreateSuccessResponse))

        val payment = Payment.retrieve("existingPaymentId")

        assertEquals(payment.id, "kHdEw9EG")
    }

    @ParameterizedTest(name = "Should capture payment when status is {0}")
    @MethodSource("capturableStatuses")
    fun `Should capture payment`(status: Payment.Status) {
        server.enqueue(MockResponse().setResponseCode(200).setBody(paymentCreateSuccessResponse))
        val payment = Fixtures.payment(status = status)

        assertDoesNotThrow {
            payment.capture()
        }
    }

    @ParameterizedTest(name = "Should not capture payment when status is {0}")
    @MethodSource("nonCapturableStatuses")
    fun `Should not capture payment`(status: Payment.Status) {
        server.enqueue(MockResponse().setResponseCode(200).setBody(paymentCreateSuccessResponse))
        val payment = Fixtures.payment(status = status)

        assertThrows<PaymentException> {
            payment.capture()
        }.also { assertThat(it).hasMessageContaining("Capture not allowed for payment status $status") }
    }

    @ParameterizedTest(name = "Should cancel payment when status is {0}")
    @MethodSource("cancellableStatuses")
    fun `Should cancel payment`(status: Payment.Status) {
        server.enqueue(MockResponse().setResponseCode(200).setBody(paymentCreateSuccessResponse))
        val payment = Fixtures.payment(status = status)

        assertDoesNotThrow {
            payment.cancel()
        }
    }

    @ParameterizedTest(name = "Should not allow cancelling payment when status is {0}")
    @MethodSource("nonCancellableStatuses")
    fun `Should not cancel payment`(status: Payment.Status) {
        server.enqueue(MockResponse().setResponseCode(200).setBody(paymentCreateSuccessResponse))
        val payment = Fixtures.payment(status = status)

        assertThrows<PaymentException> {
            payment.cancel()
        }.also { assertThat(it).hasMessageContaining("Payment in status=$status is not cancellable") }
    }

    companion object {
        @JvmStatic
        fun capturableStatuses() = listOf(
            Payment.Status.AUTHORIZED,
        )

        @JvmStatic
        private fun nonCapturableStatuses() = Payment.Status.values().subtract(capturableStatuses().toSet())
        @JvmStatic
        fun cancellableStatuses() = listOf(
            Payment.Status.AUTHORIZED,
            Payment.Status.PENDING,
            Payment.Status.FAILED,
            Payment.Status.SETTLING,
            Payment.Status.DECLINED,
        )

        @JvmStatic
        private fun nonCancellableStatuses() = Payment.Status.values().subtract(cancellableStatuses().toSet())
    }

    private val paymentCreateSuccessResponse = """
        {
          "id": "kHdEw9EG",
          "date": "2021-02-21T15:36:16.367687",
          "status": "AUTHORIZED",
          "orderId": "order-abc",
          "currencyCode": "EUR",
          "amount": 42,
          "customerId": "customer-123",
          "customer": {
            "email": "customer123@gmail.com"
          },
          "paymentMethod": {
            "paymentType": "SUBSCRIPTION",
            "paymentMethodToken": "heNwnqaeRiqvY1UcslfQc3wxNjEzOTIxNjc4",
            "isVaulted": true,
            "descriptor": "Purchase: Socks",
            "analyticsId": "VtkMDAxZW5isH0HsbbNxZ3lo",
            "paymentMethodType": "PAYMENT_CARD",
            "paymentMethodData": {
              "first6Digits": "411111",
              "last4Digits": "1111",
              "expirationMonth": "12",
              "expirationYear": "2030",
              "cardholderName": "John Biggins",
              "network": "Visa",
              "isNetworkTokenized": false,
              "binData": {
                "network": "VISA",
                "regionalRestriction": "UNKNOWN",
                "accountNumberType": "UNKNOWN",
                "accountFundingType": "UNKNOWN",
                "prepaidReloadableIndicator": "NOT_APPLICABLE",
                "productUsageType": "UNKNOWN",
                "productCode": "VISA",
                "productName": "VISA"
              }
            }
          },
          "processor": {
            "name": "STRIPE",
            "processor_merchant_id": "acct_stripe_1234",
            "amountCaptured": 0,
            "amountRefunded": 0
          },
          "transactions": [
            {
              "type": "SALE",
              "processorStatus": "AUTHORIZED",
              "processorName": "STRIPE",
              "processorMerchantId": "acct_stripe_1234",
              "processorTransactionId": "54c4eb5b3ef8a"
            }
          ],
          "metadata": {
            "productId": 123,
            "merchantId": "a13bsd62s"
          }
        }
    """.trimIndent()

    private val paymentCreateSuccessResponse2 = """
        {"id":"tCXw51bG","date":"2023-02-22T08:29:08.670525","amount":10000,"currencyCode":"SEK","customerId":"customerId","orderId":"orderId","status":"SETTLED","order":{},"customer":{},"paymentMethod":{"paymentType":"UNSCHEDULED","paymentMethodToken":"paymentMethodToken","isVaulted":true,"analyticsId":"analyticsId","paymentMethodType":"PAYMENT_CARD","paymentMethodData":{"last4Digits":"4242","first6Digits":"424242","expirationMonth":"05","expirationYear":"2025","cardholderName":"Test Testsson","network":"Visa","binData":{"network":"VISA","issuerCountryCode":"US","regionalRestriction":"UNKNOWN","accountNumberType":"UNKNOWN","accountFundingType":"UNKNOWN","prepaidReloadableIndicator":"NOT_APPLICABLE","productUsageType":"UNKNOWN","productCode":"UNKNOWN","productName":"UNKNOWN"},"isNetworkTokenized":false},"threeDSecureAuthentication":{"responseCode":"NOT_PERFORMED"},"isVerified":true},"processor":{"name":"PRIMER_PROCESSOR","processorMerchantId":"online-store","amountCaptured":10000,"amountRefunded":0},"transactions":[{"date":"2023-02-22T08:29:08.903566","amount":10000,"currencyCode":"SEK","transactionType":"SALE","processorTransactionId":"aishdlaKAmkd","processorName":"PRIMER_PROCESSOR","processorMerchantId":"online-store","processorStatus":"SETTLED"}]}
    """.trimIndent()
}
