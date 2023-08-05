package com.example.donationsapp.datasource

class DataClassDonation {
    var dataTitle: String? = null
    var dataType: String? = null
    var dataCaption: String? = null
    var dataOrganization: String? = null
    var dataImage: String? = null

    constructor(dataTitle: String?, dataType: String?, dataCaption: String?, dataOrganization: String?, dataImage: String?){
        this.dataTitle = dataTitle
        this.dataType = dataType
        this.dataCaption = dataCaption
        this.dataOrganization = dataOrganization
        this.dataImage = dataImage
    }
    constructor()
    {}
}