package com.tripled.talentlyapp.restControllers.response

import com.tripled.talentlyapp.core.network.ws.models.response.data.ErrorResponseData

class RestResponse (val data:Any?,val error:ErrorResponseData?=null)