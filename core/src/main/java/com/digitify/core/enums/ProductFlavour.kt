package com.digitify.core.enums


/**
Created by Faheem Riaz on 16/08/2021.
 **/

enum class ProductFlavour(val flavour: String) {
    PROD("live"),
    PREPROD("Preprod"),
    STG("stg"),
    INTERNAL("yapinternal"),
    QA("qa"),
    DEV("dev")
}