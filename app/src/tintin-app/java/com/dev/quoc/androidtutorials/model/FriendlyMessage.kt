package com.dev.quoc.androidtutorials.model

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class FriendlyMessage {
    var id: String? = null
    var text: String? = null
    var name: String? = null

    constructor(id: String, text: String, name: String) {
        this.id = id
        this.text = text
        this.name = name
    }
}