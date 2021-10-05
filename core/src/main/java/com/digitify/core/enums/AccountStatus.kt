package com.digitify.core.enums

import androidx.annotation.Keep

@Keep
enum class AccountStatus {
    EID_PENDING,
    CAPTURED_EID,
    TRADE_LICENSE_PENDING,
    CAPTURED_TRADE_LICENSE,
    ADDRESS_CAPTURED,
    PARTNER_DETAIL_PENDING,
    ON_BOARDED,
    UNDER_AGE,
    EID_EXPIRED,
    PENDING
}
