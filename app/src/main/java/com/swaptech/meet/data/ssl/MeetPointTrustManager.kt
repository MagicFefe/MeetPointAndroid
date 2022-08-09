package com.swaptech.meet.data.ssl

import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

//TODO: TRUST ALL CERTIFICATES IS A BAD PRACTICE, REMOVE IT WHEN APP HAS BEEN RELEASED!!!
class MeetPointTrustManager: X509TrustManager {
    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) =
        Unit

    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) =
        Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> =
        arrayOf()
}
